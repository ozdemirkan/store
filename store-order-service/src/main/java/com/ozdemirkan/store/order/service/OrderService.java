package com.ozdemirkan.store.order.service;

import com.ozdemirkan.store.order.entity.Order;
import com.ozdemirkan.store.order.entity.OrderProduct;
import com.ozdemirkan.store.order.exception.BusinessException;
import com.ozdemirkan.store.order.model.CreateOrderRequest;
import com.ozdemirkan.store.order.model.Currency;
import com.ozdemirkan.store.order.repository.OrderProductRepository;
import com.ozdemirkan.store.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    @Transactional
    public Order createOrder(CreateOrderRequest request) throws BusinessException {

        Order newOrder = Order.builder()
                .email(request.getEmail())
                //TODO : calculate total price
                .totalPrice(BigDecimal.valueOf(0.00))
                .currency(Currency.EUR)
                .build();

        Order savedOrder = orderRepository.saveAndFlush(newOrder);

        request.getProducts()
                .forEach(product -> orderProductRepository.save(
                        OrderProduct.builder()
                                .orderId(savedOrder.getId())
                                .productId(product.getId())
                                .count(product.getCount())
                                .build()));

        return newOrder;
    }

    public Page<Order> findAllOrdersByEmail(String email, Pageable pageable) {
        return orderRepository.findByEmailContainsIgnoreCase(email == null ? "" : email, pageable);
    }

}
