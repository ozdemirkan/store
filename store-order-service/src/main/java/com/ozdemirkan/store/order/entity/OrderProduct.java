package com.ozdemirkan.store.order.entity;

import com.ozdemirkan.store.order.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDER_PRODUCT")
public class OrderProduct extends BaseEntity {
    private String orderId;
    private String productId;
    private BigDecimal price;
    private Currency currency;
    private int count;

}
