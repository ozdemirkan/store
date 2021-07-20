package com.ozdemirkan.store.order.entity;

import com.ozdemirkan.store.order.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDER_INFO")
public class Order extends BaseEntity{
    @Column(length = 100)
    private String email;
    @Column(nullable= false, precision=10, scale=2)
    private BigDecimal totalPrice;
    @Column(length = 3)
    private Currency currency;

    @OneToMany
    @JoinColumn(name="orderId")
    @Lazy
    private List<OrderProduct> products;
}
