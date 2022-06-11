package com.miu.ea.goodpeople.service;

import com.miu.ea.goodpeople.domain.*; // for static metamodels
import com.miu.ea.goodpeople.domain.Trip;
import com.miu.ea.goodpeople.repository.TripRepository;
import com.miu.ea.goodpeople.service.criteria.TripCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Trip} entities in the database.
 * The main input is a {@link TripCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Trip} or a {@link Page} of {@link Trip} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TripQueryService extends QueryService<Trip> {

    private final Logger log = LoggerFactory.getLogger(TripQueryService.class);

    private final TripRepository tripRepository;

    public TripQueryService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    /**
     * Return a {@link List} of {@link Trip} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Trip> findByCriteria(TripCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Trip> specification = createSpecification(criteria);
        return tripRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Trip} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Trip> findByCriteria(TripCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Trip> specification = createSpecification(criteria);
        return tripRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TripCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Trip> specification = createSpecification(criteria);
        return tripRepository.count(specification);
    }

    /**
     * Function to convert {@link TripCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Trip> createSpecification(TripCriteria criteria) {
        Specification<Trip> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Trip_.id));
            }
            if (criteria.getStartLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStartLocation(), Trip_.startLocation));
            }
            if (criteria.getDestination() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDestination(), Trip_.destination));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), Trip_.startTime));
            }
            if (criteria.getCanOfferRide() != null) {
                specification = specification.and(buildSpecification(criteria.getCanOfferRide(), Trip_.canOfferRide));
            }
            if (criteria.getCanBringProduct() != null) {
                specification = specification.and(buildSpecification(criteria.getCanBringProduct(), Trip_.canBringProduct));
            }
            if (criteria.getNumberOfSeatsOffered() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfSeatsOffered(), Trip_.numberOfSeatsOffered));
            }
            if (criteria.getNumberOfSeatsRemaining() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getNumberOfSeatsRemaining(), Trip_.numberOfSeatsRemaining));
            }
            if (criteria.getOwnerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOwnerId(), root -> root.join(Trip_.owner, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getRequestId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRequestId(), root -> root.join(Trip_.requests, JoinType.LEFT).get(Request_.id))
                    );
            }
        }
        return specification;
    }
}
