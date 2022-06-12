package com.miu.ea.goodpeople.service;

import com.miu.ea.goodpeople.domain.Trip;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Trip}.
 */
public interface TripService {
    /**
     * Save a trip.
     *
     * @param trip the entity to save.
     * @return the persisted entity.
     */
    Trip save(Trip trip);

    /**
     * Updates a trip.
     *
     * @param trip the entity to update.
     * @return the persisted entity.
     */
    Trip update(Trip trip);

    /**
     * Partially updates a trip.
     *
     * @param trip the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Trip> partialUpdate(Trip trip);

    /**
     * Get all the trips.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Trip> findAll(Pageable pageable);

    /**
     * Get the "id" trip.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Trip> findOne(Long id);

    /**
     * Delete the "id" trip.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
