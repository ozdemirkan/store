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
import java.util.Map;

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

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name="orderId")
    @Lazy
    private List<OrderProduct> products;

    public static Order buildOrder(String email, List<OrderProduct> products, Map<String, BigDecimal> productPriceMap) {
        return new Order(email, calculateTotalPrice(products, productPriceMap), Currency.EUR, products);
    }

    private static BigDecimal calculateTotalPrice(List<OrderProduct> products, Map<String, BigDecimal> productPriceMap) {
        return products
                .stream()
                .map(product -> productPriceMap.get(product.getProductId()).multiply(BigDecimal.valueOf(product.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
