package com.miu.ea.tripservice.service;

import com.miu.ea.tripservice.service.dto.TripDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.miu.ea.tripservice.domain.Trip}.
 */
public interface TripService {
    /**
     * Save a trip.
     *
     * @param tripDTO the entity to save.
     * @return the persisted entity.
     */
    TripDTO save(TripDTO tripDTO);

    /**
     * Updates a trip.
     *
     * @param tripDTO the entity to update.
     * @return the persisted entity.
     */
    TripDTO update(TripDTO tripDTO);

    /**
     * Partially updates a trip.
     *
     * @param tripDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TripDTO> partialUpdate(TripDTO tripDTO);

    /**
     * Get all the trips.
     *
     * @return the list of entities.
     */
    List<TripDTO> findAll();

    /**
     * Get the "id" trip.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TripDTO> findOne(Long id);

    /**
     * Delete the "id" trip.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
