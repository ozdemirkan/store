package com.ozdemirkan.store.product.entity;

import com.ozdemirkan.store.product.model.Currency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class Product {
    private String id;
    private String name;
    private BigDecimal price;
    private Currency currency;
    private Date createDate;
}
