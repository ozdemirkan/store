package com.ozdemirkan.store.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDER_PRODUCT")
public class OrderProduct extends BaseEntity {
    private String orderId;
    private String productId;
    private int count;

}
