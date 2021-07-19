package com.ozdemirkan.store.product.controller;

import com.ozdemirkan.store.product.entity.Product;
import com.ozdemirkan.store.product.exception.BusinessException;
import com.ozdemirkan.store.product.model.CreateProductRequest;
import com.ozdemirkan.store.product.model.GenericResponse;
import com.ozdemirkan.store.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class ProductController {

    private final ProductService productService;

    @PostMapping("/product")
    public ResponseEntity<Void> createProduct(@RequestBody @Valid CreateProductRequest request,
                                              UriComponentsBuilder ucb) throws BusinessException {
        Product product = productService.createProduct(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucb.path("/api/product/{id}").buildAndExpand(product.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/product")
    public ResponseEntity<GenericResponse<List<Product>>> getProducts(@RequestParam(required = false) @Valid @Size(min=1, max = 100)  @Pattern(regexp = "^[a-zA-Z0-9 _.'-]*$") String name){
        return ResponseEntity.ok(new GenericResponse<>(productService.findAllProductsByName(name).orElse(List.of())));
    }

}
