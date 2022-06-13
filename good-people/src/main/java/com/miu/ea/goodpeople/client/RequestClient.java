package com.miu.ea.goodpeople.client;

import com.miu.ea.goodpeople.domain.Request;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RequestClient {

    @RequestLine("POST /requests")
    @Headers("Content-Type: application/json")
    Request createRequest(Request request);

    @RequestLine("PUT /requests/{id}")
    @Headers("Content-Type: application/json")
    Request updateRequest(@Param String id, Request request);

    @RequestLine("PATCH /requests/{id}")
    @Headers("Content-Type: application/json")
    Request partialUpdateRequest(@Param String id, Request request);

    @RequestLine("GET /requests")
    List<Request> getAllRequests();

    @RequestLine("GET /requests/{id}")
    List<Request> getRequest(@Param String id);

    @RequestLine("DELETE /requests/{id}")
    List<Request> deleteRequest(@Param String id);
}
