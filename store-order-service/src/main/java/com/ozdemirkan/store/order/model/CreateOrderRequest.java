package com.ozdemirkan.store.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

import static com.ozdemirkan.store.order.util.RegexUtil.REGEXP_MAIL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    @NotBlank
    @Size(min=1, max = 100)
    @Pattern(regexp = REGEXP_MAIL)
    private String email;

    @NotNull
    private List<Product> products;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product {
        private String id;
        private int count;
    }

}
