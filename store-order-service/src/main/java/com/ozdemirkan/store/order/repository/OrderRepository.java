package com.ozdemirkan.store.order.repository;

import com.ozdemirkan.store.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    Page<Order> findByEmailContainsIgnoreCase(String email, Pageable pageable);
}
