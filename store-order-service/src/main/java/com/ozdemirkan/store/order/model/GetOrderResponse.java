package com.ozdemirkan.store.order.model;

import com.ozdemirkan.store.order.entity.Order;
import com.ozdemirkan.store.order.entity.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderResponse {
    private Order order;
    private List<OrderProduct> orderProducts;
}
