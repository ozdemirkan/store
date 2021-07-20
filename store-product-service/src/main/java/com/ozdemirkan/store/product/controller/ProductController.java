package com.ozdemirkan.store.product.controller;

import com.ozdemirkan.store.product.entity.Product;
import com.ozdemirkan.store.product.exception.BusinessException;
import com.ozdemirkan.store.product.model.*;
import com.ozdemirkan.store.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import static util.RegexUtil.REGEXP_UUID;

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
    public ResponseEntity<GenericResponse<Page<Product>>> getProducts(@RequestParam(required = false) @Valid @Size(min=1, max = 100)  @Pattern(regexp = "^[a-zA-Z0-9 _.'-]*$") String name, Pageable pageable){
        Page<Product> products = productService.findAllProductsByName(name,pageable);
        return ResponseEntity.ok(new GenericResponse<>(products));
    }

    @PostMapping("/product/details")
    public ResponseEntity<GenericResponse<GetProductDetailsResponse>> getProductDetails(@RequestBody(required = false) @Valid GetProductDetailsRequest getProductDetailsRequest){
        List<GetProductDetailsResponse.ProductDetail> productDetails = productService.getProductDetails(getProductDetailsRequest.getIds());
        return ResponseEntity.ok(new GenericResponse<>(GetProductDetailsResponse.builder().productDetails(productDetails).build()));
    }

    @PatchMapping("/product/{id}")
    public ResponseEntity<GenericResponse<Void>> updateProduct(@PathVariable @Pattern(regexp = REGEXP_UUID) String id,
                                                               @RequestBody(required = false) @Valid UpdateProductRequest updateProductRequest) throws BusinessException {
        productService.updateProduct(id, updateProductRequest);
        return ResponseEntity.ok(new GenericResponse<>(null));
    }
}
