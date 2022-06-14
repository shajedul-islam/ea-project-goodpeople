package com.miu.ea.tripservice.service.impl;

import com.miu.ea.tripservice.domain.Trip;
import com.miu.ea.tripservice.repository.TripRepository;
import com.miu.ea.tripservice.service.TripService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Trip}.
 */
@Service
@Transactional
public class TripServiceImpl implements TripService {

    private final Logger log = LoggerFactory.getLogger(TripServiceImpl.class);

    private final TripRepository tripRepository;

    public TripServiceImpl(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public Trip save(Trip trip) {
        log.debug("Request to save Trip : {}", trip);
        return tripRepository.save(trip);
    }

    @Override
    public Trip update(Trip trip) {
        log.debug("Request to save Trip : {}", trip);
        return tripRepository.save(trip);
    }

    @Override
    public Optional<Trip> partialUpdate(Trip trip) {
        log.debug("Request to partially update Trip : {}", trip);

        return tripRepository
            .findById(trip.getId())
            .map(existingTrip -> {
                if (trip.getTripId() != null) {
                    existingTrip.setTripId(trip.getTripId());
                }
                if (trip.getOwnerId() != null) {
                    existingTrip.setOwnerId(trip.getOwnerId());
                }
                if (trip.getStartLocation() != null) {
                    existingTrip.setStartLocation(trip.getStartLocation());
                }
                if (trip.getDestination() != null) {
                    existingTrip.setDestination(trip.getDestination());
                }
                if (trip.getStartTime() != null) {
                    existingTrip.setStartTime(trip.getStartTime());
                }
                if (trip.getCanOfferRide() != null) {
                    existingTrip.setCanOfferRide(trip.getCanOfferRide());
                }
                if (trip.getCanBringProduct() != null) {
                    existingTrip.setCanBringProduct(trip.getCanBringProduct());
                }
                if (trip.getNumberOfOfferedSeats() != null) {
                    existingTrip.setNumberOfOfferedSeats(trip.getNumberOfOfferedSeats());
                }

                return existingTrip;
            })
            .map(tripRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trip> findAll() {
        log.debug("Request to get all Trips");
        return tripRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trip> findOne(Long id) {
        log.debug("Request to get Trip : {}", id);
        return tripRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Trip : {}", id);
        tripRepository.deleteById(id);
    }
    
    @Override
    public List<Trip> findByOwnerId(Long id) {
        return tripRepository.findTripByOwnerId(id);
    }
}
