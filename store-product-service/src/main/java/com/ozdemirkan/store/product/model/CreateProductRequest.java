package com.ozdemirkan.store.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
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
    @Min(0)
    private BigDecimal price;
    @NotNull
    Currency currency;

}
