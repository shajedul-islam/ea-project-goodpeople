package com.miu.ea.goodpeople.client;

import com.miu.ea.goodpeople.domain.Request;
import com.miu.ea.goodpeople.service.dto.RequestDTO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RequestClient {

    @RequestLine("POST /requests")
    @Headers("Content-Type: application/json")
    RequestDTO createRequest(RequestDTO request);

    @RequestLine("PUT /requests/{id}")
    @Headers("Content-Type: application/json")
    RequestDTO updateRequest(@Param("id") Long id, RequestDTO request);

    @RequestLine("PATCH /requests/{id}")
    @Headers("Content-Type: application/json")
    RequestDTO partialUpdateRequest(@Param("id") Long id, RequestDTO request);

    @RequestLine("GET /requests")
    List<RequestDTO> getAllRequests();

    @RequestLine("GET /requests/{id}")
    RequestDTO getRequest(@Param("id") Long id);

    @RequestLine("DELETE /requests/{id}")
    void deleteRequest(@Param("id") Long id);

    @RequestLine("GET /requests/requester/{id}")
    List<RequestDTO> findByRequesterId(@Param("id") Long id);

    @RequestLine("GET /requests/trip/{id}")
    List<RequestDTO> findByTripId(@Param("id") Long id);
}
