package com.ozdemirkan.store.product.entity;

import com.ozdemirkan.store.product.model.Currency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class Product {
    private String id;
    private String name;
    private BigDecimal price;
    private Currency currency;
    private Date createDate;

    public static Product createInstance(String name, BigDecimal price, Currency currency) {
        return Product.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .price(price)
                .currency(currency)
                .createDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }
}
