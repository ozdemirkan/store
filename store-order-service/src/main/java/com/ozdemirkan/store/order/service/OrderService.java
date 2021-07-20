package com.ozdemirkan.store.order.service;

import com.ozdemirkan.store.order.client.ProductServiceClient;
import com.ozdemirkan.store.order.client.model.GetProductDetailsResponse;
import com.ozdemirkan.store.order.entity.Order;
import com.ozdemirkan.store.order.entity.OrderProduct;
import com.ozdemirkan.store.order.exception.BusinessException;
import com.ozdemirkan.store.order.exception.ErrorType;
import com.ozdemirkan.store.order.model.CreateOrderRequest;
import com.ozdemirkan.store.order.model.Currency;
import com.ozdemirkan.store.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final  ProductServiceClient productServiceClient;

    @Transactional
    public Order createOrder(CreateOrderRequest request) throws BusinessException {

        //Retrieve price information for the ordered products
        GetProductDetailsResponse productDetails = getProductDetails(request);

        //Prepare id-price map to be used during total price calculation
        Map<String, BigDecimal> productPriceMap = retrieveProductPriceMap(productDetails);

        //Save order
        List<OrderProduct> orderProducts= request.getProducts()
                .stream()
                .map(product ->
                        OrderProduct.builder()
                                .productId(product.getId())
                                .price(productPriceMap.get(product.getId()))
                                .currency(Currency.EUR)
                                .count(product.getCount())
                                .build())
                .collect(Collectors.toList());


        Order order = Order.buildOrder(request.getEmail(), orderProducts, productPriceMap);

        orderRepository.save(order);

        return order;
    }

    private Map<String, BigDecimal> retrieveProductPriceMap(GetProductDetailsResponse productDetails) {
        return productDetails.getProductDetails().stream()
                .collect(Collectors.toMap(GetProductDetailsResponse.ProductDetail::getId, GetProductDetailsResponse.ProductDetail::getPrice));
    }

    private GetProductDetailsResponse getProductDetails(CreateOrderRequest request) throws BusinessException {
        GetProductDetailsResponse productDetails = productServiceClient.getProductDetails(request.getProducts().stream().map(CreateOrderRequest.Product::getId).collect(Collectors.toList()));
        //If there is a missing product price information, reject request
        if(productDetails.getProductDetails().size()!= request.getProducts().size()){
            throw new BusinessException(ErrorType.NON_EXISTING_PRODUCT);
        }

        if(productDetails.getProductDetails().stream().anyMatch(productDetail -> productDetail.getCurrency()!= GetProductDetailsResponse.Currency.EUR)){
            throw new BusinessException(ErrorType.UNSUPPORTED_CURRENCY);
        }

        return productDetails;
    }

    public Page<Order> findAllOrdersByEmail(String email, Pageable pageable) {
        return orderRepository.findByEmailContainsIgnoreCase(email == null ? "" : email, pageable);
    }

}
