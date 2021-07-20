package com.ozdemirkan.store.order.controller;

import com.ozdemirkan.store.order.entity.Order;
import com.ozdemirkan.store.order.exception.BusinessException;
import com.ozdemirkan.store.order.model.CreateOrderRequest;
import com.ozdemirkan.store.order.model.GenericResponse;
import com.ozdemirkan.store.order.service.OrderService;
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

import static com.ozdemirkan.store.order.util.RegexUtil.REGEXP_MAIL;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<Void> createOrder(@RequestBody @Valid CreateOrderRequest request,
                                            UriComponentsBuilder ucb) throws BusinessException {
        Order order = orderService.createOrder(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucb.path("/api/order/{id}").buildAndExpand(order.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/order")
    public ResponseEntity<GenericResponse<Page<Order>>> getOrders(@RequestParam(required = false) @Valid @Size(min=1, max = 100)  @Pattern(regexp = REGEXP_MAIL) String email,
                                                                  Pageable pageable){
        return ResponseEntity.ok(new GenericResponse<>(orderService.findAllOrdersByEmail(email, pageable)));
    }
}
