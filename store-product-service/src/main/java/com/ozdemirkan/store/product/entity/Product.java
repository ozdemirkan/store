package com.ozdemirkan.store.product.entity;

import com.ozdemirkan.store.product.model.Currency;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT")
public class Product extends BaseEntity {
    private String name;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Currency currency;


}
