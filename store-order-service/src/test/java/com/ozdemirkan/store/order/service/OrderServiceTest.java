package com.ozdemirkan.store.order.service;

import com.ozdemirkan.store.order.client.ProductServiceClient;
import com.ozdemirkan.store.order.client.model.GetProductDetailsResponse;
import com.ozdemirkan.store.order.entity.Order;
import com.ozdemirkan.store.order.entity.OrderProduct;
import com.ozdemirkan.store.order.exception.BusinessException;
import com.ozdemirkan.store.order.exception.ErrorType;
import com.ozdemirkan.store.order.model.CreateOrderRequest;
import com.ozdemirkan.store.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class OrderServiceTest {

    public static final String EMAIL = "user_create_service@example.com";
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductServiceClient productServiceClient;

    @BeforeEach
    public void init() {
    }

    //CREATE ORDER//
    @Test
    void createOrderSuccess() throws BusinessException {
        //given
        String productId = UUID.randomUUID().toString();
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(EMAIL, List.of(new CreateOrderRequest.Product(productId,5)));

        //when
        when(orderRepository.save(any(Order.class))).thenReturn(Order.builder()
                .email(createOrderRequest.getEmail())
                .products(List.of(
                        OrderProduct.builder()
                                .orderId(UUID.randomUUID().toString())
                                .productId(createOrderRequest.getProducts().get(0).getId())
                                .build()))
                .build());

        when(productServiceClient.getProductDetails(any()))
                .thenReturn(
                        new GetProductDetailsResponse(
                                List.of(
                                        new GetProductDetailsResponse.ProductDetail(productId, BigDecimal.valueOf(100), GetProductDetailsResponse.Currency.EUR))));
        //then
        Order newOrder = orderService.createOrder(createOrderRequest);
        assertEquals(newOrder.getEmail(), createOrderRequest.getEmail());
    }

    @Test
    void createOrderFailsWhenProductCurrencyNotEuro() throws BusinessException {
        //given
        String productId = UUID.randomUUID().toString();
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(EMAIL, List.of(new CreateOrderRequest.Product(productId,5)));

        //when
        when(productServiceClient.getProductDetails(any()))
                .thenReturn(
                        new GetProductDetailsResponse(
                                List.of(
                                        new GetProductDetailsResponse.ProductDetail(productId, BigDecimal.valueOf(100), GetProductDetailsResponse.Currency.USD))));
        //then
        BusinessException exception = assertThrows(BusinessException.class, () -> orderService.createOrder(createOrderRequest));
        assertEquals(ErrorType.UNSUPPORTED_CURRENCY.getCode(), exception.getErrorType().getCode());
    }

}
