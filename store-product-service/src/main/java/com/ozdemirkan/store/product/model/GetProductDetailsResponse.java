package com.ozdemirkan.store.product.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProductDetailsResponse {
    private List<ProductDetail> productDetails;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductDetail{
        private String id;
        private BigDecimal price;
        private Currency currency;
    }
}
