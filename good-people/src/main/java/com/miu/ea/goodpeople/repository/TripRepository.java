package com.miu.ea.goodpeople.repository;

import com.miu.ea.goodpeople.domain.Trip;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Trip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    @Query("select trip from Trip trip where trip.owner.login = ?#{principal.username}")
    List<Trip> findByOwnerIsCurrentUser();
}
