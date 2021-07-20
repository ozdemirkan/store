package com.ozdemirkan.store.order.client;

import com.ozdemirkan.store.order.client.model.GetProductDetailsRequest;
import com.ozdemirkan.store.order.client.model.GetProductDetailsResponse;
import com.ozdemirkan.store.order.exception.BusinessException;
import com.ozdemirkan.store.order.exception.ErrorType;
import com.ozdemirkan.store.order.model.GenericResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductServiceClient {
    private final RestTemplate restTemplate;
    @Value("${mw.service.product.url:http://localhost:8081}")
    private String productServiceUrl;

    public GetProductDetailsResponse getProductDetails(List<String> ids) throws BusinessException {
        GenericResponse<GetProductDetailsResponse> response;
        try {
            response = restTemplate.exchange(
                    productServiceUrl + "/api/product/details",
                    HttpMethod.POST,
                    new HttpEntity<Object>(GetProductDetailsRequest.builder().ids(ids).build()),
                    new ParameterizedTypeReference<GenericResponse<GetProductDetailsResponse>>() {})
                    .getBody();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(ErrorType.UNEXPECTED_ERROR);
        }

        return response != null ? response.getData() : null;
    }

}
