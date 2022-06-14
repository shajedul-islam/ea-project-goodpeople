package com.miu.ea.goodpeople.client;

import com.miu.ea.goodpeople.domain.Trip;
import com.miu.ea.goodpeople.service.dto.TripDTO;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TripClient {

    @RequestLine("POST /trips")
    @Headers("Content-Type: application/json")
    TripDTO createTrip(TripDTO trip);

    @RequestLine("PUT /trips/{id}")
    @Headers("Content-Type: application/json")
    TripDTO updateTrip(@Param("id") Long id, TripDTO trip);

    @RequestLine("PATCH /trips/{id}")
    @Headers("Content-Type: application/json")
    TripDTO partialUpdateTrip(@Param("id") Long id, TripDTO trip);

    @RequestLine("GET /trips")
    List<TripDTO> getAllTrips();

    @RequestLine("GET /trips/{id}")
    TripDTO getTrip(@Param("id") Long id);

    @RequestLine("DELETE /trips/{id}")
    List<TripDTO> deleteTrip(@Param("id") Long id);
    
    @RequestLine("GET /trips/owner/{id}")
    List<TripDTO> getTripByOwnerId(@Param("id") Long id);

}
