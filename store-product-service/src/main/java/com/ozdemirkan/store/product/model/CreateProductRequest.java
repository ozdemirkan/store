package com.ozdemirkan.store.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    @NotBlank
    @Size(min=1, max = 100)
    @Pattern(regexp = "^[a-zA-Z0-9 _.'-]*$")
    private String name;

    @NotNull
    private BigDecimal price;
    @NotNull
    Currency currency;

}
