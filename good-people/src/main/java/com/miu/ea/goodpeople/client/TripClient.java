package com.miu.ea.goodpeople.client;

import com.miu.ea.goodpeople.domain.Trip;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TripClient {

    @RequestLine("POST /trips")
    @Headers("Content-Type: application/json")
    Trip createTrip(Trip trip);

    @RequestLine("PUT /trips/{id}")
    @Headers("Content-Type: application/json")
    Trip updateTrip(@Param String id, Trip trip);

    @RequestLine("PATCH /trips/{id}")
    @Headers("Content-Type: application/json")
    Trip partialUpdateTrip(@Param String id, Trip trip);

    @RequestLine("GET /trips")
    List<Trip> getAllTrips();

    @RequestLine("GET /trips/{id}")
    List<Trip> getTrip(@Param String id);

    @RequestLine("DELETE /trips/{id}")
    List<Trip> deleteTrip(@Param String id);

}
