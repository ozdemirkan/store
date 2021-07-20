package com.ozdemirkan.store.order.repository;

import com.ozdemirkan.store.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, String> {

    Optional<OrderProduct> findById(String id);
    Optional<OrderProduct> findByOrderId(String orderId);

}
