package com.miu.ea.tripservice.service.impl;

import com.miu.ea.tripservice.domain.Trip;
import com.miu.ea.tripservice.repository.TripRepository;
import com.miu.ea.tripservice.service.TripService;
import com.miu.ea.tripservice.service.dto.TripDTO;
import com.miu.ea.tripservice.service.mapper.TripMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    private final TripMapper tripMapper;

    public TripServiceImpl(TripRepository tripRepository, TripMapper tripMapper) {
        this.tripRepository = tripRepository;
        this.tripMapper = tripMapper;
    }

    @Override
    public TripDTO save(TripDTO tripDTO) {
        log.debug("Request to save Trip : {}", tripDTO);
        Trip trip = tripMapper.toEntity(tripDTO);
        trip = tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    @Override
    public TripDTO update(TripDTO tripDTO) {
        log.debug("Request to save Trip : {}", tripDTO);
        Trip trip = tripMapper.toEntity(tripDTO);
        trip = tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    @Override
    public Optional<TripDTO> partialUpdate(TripDTO tripDTO) {
        log.debug("Request to partially update Trip : {}", tripDTO);

        return tripRepository
            .findById(tripDTO.getId())
            .map(existingTrip -> {
                tripMapper.partialUpdate(existingTrip, tripDTO);

                return existingTrip;
            })
            .map(tripRepository::save)
            .map(tripMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripDTO> findAll() {
        log.debug("Request to get all Trips");
        return tripRepository.findAll().stream().map(tripMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TripDTO> findOne(Long id) {
        log.debug("Request to get Trip : {}", id);
        return tripRepository.findById(id).map(tripMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Trip : {}", id);
        tripRepository.deleteById(id);
    }
}
