package com.miu.ea.goodpeople.service.impl;

import com.miu.ea.goodpeople.domain.Trip;
import com.miu.ea.goodpeople.domain.User;
import com.miu.ea.goodpeople.repository.TripRepository;
import com.miu.ea.goodpeople.repository.UserRepository;
import com.miu.ea.goodpeople.service.TripService;
import com.miu.ea.goodpeople.service.UserService;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    private final UserRepository userRepository;

    public TripServiceImpl(TripRepository tripRepository, UserRepository userRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
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
                if (trip.getNumberOfSeatsOffered() != null) {
                    existingTrip.setNumberOfSeatsOffered(trip.getNumberOfSeatsOffered());
                }
                if (trip.getNumberOfSeatsRemaining() != null) {
                    existingTrip.setNumberOfSeatsRemaining(trip.getNumberOfSeatsRemaining());
                }

                return existingTrip;
            })
            .map(tripRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Trip> findAll(Pageable pageable) {
        log.debug("Request to get all Trips");
        return tripRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Trip> findAllByOwnerId(Long ownerId) {
        log.debug("Request to get all Trips");
        User owner = userRepository.getById(ownerId);
        return tripRepository.findAllByOwner(owner);
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
}
