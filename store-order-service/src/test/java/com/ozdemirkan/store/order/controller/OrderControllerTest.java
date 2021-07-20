package com.ozdemirkan.store.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozdemirkan.store.order.client.model.GetProductDetailsResponse;
import com.ozdemirkan.store.order.exception.BusinessException;
import com.ozdemirkan.store.order.model.CreateOrderRequest;
import com.ozdemirkan.store.order.model.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class OrderControllerTest {
    public static final String API_ORDER = "/api/order";
    public static final String USER_CREATE_EMAIL = "user_create@example.com";
    public static final String USER_LIST_EMAIL = "user_list@example.com";
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    private MockRestServiceServer mockServer;
    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    //CREATE ORDER//
    @Test
    void createOrderSuccess() throws BusinessException, Exception {
        //given
        String product_id = UUID.randomUUID().toString();
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(USER_CREATE_EMAIL,
                List.of(new CreateOrderRequest.Product(product_id,2)));

        //when
        GenericResponse<GetProductDetailsResponse> response = new GenericResponse<>(GetProductDetailsResponse
                .builder()
                .productDetails(List.of(new GetProductDetailsResponse.ProductDetail(product_id, BigDecimal.valueOf(100.00), GetProductDetailsResponse.Currency.EUR)))
                .build());

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://localhost:8081/api/product/details")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(response))
                );

        //then
        mvc.perform(
                post(API_ORDER)
                        .content(mapper.writeValueAsString(createOrderRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        mvc.perform(
                get(API_ORDER +"?email="+ USER_CREATE_EMAIL))
                .andDo(print())
                .andExpect(jsonPath("$.data.content[0].totalPrice").value("200.0"))
                .andExpect(status().isOk());


    }

    @Test
    void createOrderWithInvalidBodyReturns400() throws BusinessException, Exception {

        //then
        //Empty order email
        mvc.perform(
                post(API_ORDER)
                        .content(mapper.writeValueAsString(new CreateOrderRequest("",
                                List.of(new CreateOrderRequest.Product(UUID.randomUUID().toString(),2)))))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //null products
        mvc.perform(
                post(API_ORDER)
                        .content(mapper.writeValueAsString(new CreateOrderRequest(USER_CREATE_EMAIL, null)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    //FIND ORDER//
    @Test
    void findOrdersByEmailSuccess() throws BusinessException, Exception {

        //given
        String firstProductId = UUID.randomUUID().toString();
        String secondProductId = UUID.randomUUID().toString();

        //when
        GenericResponse<GetProductDetailsResponse> response = new GenericResponse<>(GetProductDetailsResponse
                .builder()
                .productDetails(
                        List.of(
                                new GetProductDetailsResponse.ProductDetail(firstProductId, BigDecimal.valueOf(100.00), GetProductDetailsResponse.Currency.EUR),
                                new GetProductDetailsResponse.ProductDetail(secondProductId, BigDecimal.valueOf(100.00), GetProductDetailsResponse.Currency.EUR)))
                .build());

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://localhost:8081/api/product/details")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(response))
                );

        //then
        //Create Order
        mvc.perform(post(API_ORDER)
                .content(mapper.writeValueAsString(new CreateOrderRequest(USER_LIST_EMAIL,
                        List.of(
                                new CreateOrderRequest.Product(firstProductId.toString(),2),
                                new CreateOrderRequest.Product(secondProductId.toString(),5))
                        )))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        //Find newly created Order
        mvc.perform(
                get(API_ORDER +"?email="+ USER_LIST_EMAIL))
                .andDo(print())
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.content[0].totalPrice").value("700.0"))
                .andExpect(status().isOk());
    }

    @Test
    void findOrdersByWrongEmailReturnsEmptyList() throws BusinessException, Exception {
        //given
        String naUserEmail = "user_na@example.com";

        //then
        mvc.perform(
                get(API_ORDER + "?email=" + naUserEmail))
                .andDo(print())
                .andExpect(jsonPath("$.data.totalElements").value(0))
                .andExpect(status().isOk());
    }

    @Test
    void findOrdersWithInvalidNameReturns400() throws BusinessException, Exception {

        mvc.perform(
                get(API_ORDER + "?email="))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
