package com.miu.ea.tripservice.repository;
import com.miu.ea.tripservice.domain.Trip;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 * Spring Data SQL repository for the Trip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    
    List<Trip> findTripByOwnerId(Long id);
}