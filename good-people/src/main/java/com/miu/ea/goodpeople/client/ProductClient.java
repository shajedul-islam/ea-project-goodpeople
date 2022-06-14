package com.miu.ea.goodpeople.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.stereotype.Component;

import com.miu.ea.goodpeople.service.dto.ProductResponse;
import com.miu.ea.goodpeople.service.dto.ProductSaveRequest;
import com.miu.ea.goodpeople.service.dto.Products;

import java.util.List;

@Component
public interface ProductClient {

    @RequestLine("POST")
    @Headers("Content-Type: application/json")
    void save(ProductSaveRequest request);

    @RequestLine("GET /{requestId}")
    ProductResponse findByRequestId(@Param("requestId") Long requestId);

}
