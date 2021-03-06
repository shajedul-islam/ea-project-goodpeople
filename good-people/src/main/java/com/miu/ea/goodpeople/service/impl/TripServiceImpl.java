package com.miu.ea.goodpeople.service.impl;

import com.miu.ea.goodpeople.client.TripClient;
import com.miu.ea.goodpeople.domain.Trip;
import com.miu.ea.goodpeople.domain.User;
import com.miu.ea.goodpeople.repository.TripRepository;
import com.miu.ea.goodpeople.repository.UserRepository;
import com.miu.ea.goodpeople.service.TripService;
import com.miu.ea.goodpeople.service.UserService;
import com.miu.ea.goodpeople.service.dto.TripDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    private final TripClient tripClient;

    public TripServiceImpl(TripRepository tripRepository, UserRepository userRepository, TripClient tripClient) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
		this.tripClient = tripClient;
    }

    private TripDTO toTripDTO(Trip trip) {
    	return new TripDTO(trip.getId(), trip.getId(), trip.getOwner().getId(), trip.getStartLocation(),
				trip.getDestination(), trip.getStartTime().toString(), trip.getCanOfferRide(), trip.getCanBringProduct(),
				trip.getNumberOfSeatsOffered());
    }

    private Trip toTrip(Trip trip, TripDTO tripDTO) {
    	User owner = userRepository.findById(tripDTO.getOwnerId()).get();
    	return new Trip(tripDTO.getId(), tripDTO.getStartLocation(), tripDTO.getDestination(), tripDTO.getStartTime(),
    			tripDTO.getCanOfferRide(), tripDTO.getCanBringProduct(), tripDTO.getNumberOfOfferedSeats(),
    			trip.getNumberOfSeatsRemaining(), trip.getRequests(), owner);
    }

    @Override
    public Trip save(Trip trip) {
        log.debug("Request to save Trip : {}", trip);
        // Trip savedTrip = tripRepository.save(trip);

        trip.setId(new Double(Math.random()).longValue());
		TripDTO tripDTO = toTripDTO(trip);

		tripDTO = tripClient.createTrip(tripDTO);

        return toTrip(trip, tripDTO);
    }

    @Override
    public Trip update(Trip trip) {
        log.debug("Request to save Trip : {}", trip);
        //Trip updatedTrip = tripRepository.save(trip);

        TripDTO tripDTO = toTripDTO(trip);

		tripDTO = tripClient.updateTrip(trip.getId(), tripDTO);

		return toTrip(trip, tripDTO);
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

        List<TripDTO> tripDTOs = tripClient.getAllTrips();
        
        List<Trip> trips = new ArrayList<Trip>();
        for (TripDTO tripDTO : tripDTOs) {
        	trips.add(toTrip(new Trip(), tripDTO));
        }

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), trips.size());
        final Page<Trip> page = new PageImpl<>(trips.subList(start, end), pageable, trips.size());

        return page;

    }

    @Override
    @Transactional(readOnly = true)
    public List<Trip> findAllByOwnerId(Long ownerId) {
        log.debug("Request to get all Trips");
        //User owner = userRepository.getById(ownerId);
        //return tripRepository.findAllByOwner(owner);
        List<TripDTO> tripDTOs = tripClient.getTripByOwnerId(ownerId);
        
        List<Trip> trips = new ArrayList<Trip>();
        for (TripDTO tripDTO : tripDTOs) {
        	trips.add(toTrip(new Trip(), tripDTO));
        }
        return trips;
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Trip> findOne(Long id) {
        log.debug("Request to get Trip : {}", id);
        // return tripRepository.findById(id);
        TripDTO tripDTO = tripClient.getTrip(id);
        return Optional.of(toTrip(new Trip(), tripDTO));
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Trip : {}", id);
        // tripRepository.deleteById(id);
        tripClient.deleteTrip(id);
    }
}
