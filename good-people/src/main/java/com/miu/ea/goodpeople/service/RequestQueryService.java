package com.miu.ea.goodpeople.service;

import com.miu.ea.goodpeople.domain.*; // for static metamodels
import com.miu.ea.goodpeople.domain.Request;
import com.miu.ea.goodpeople.repository.RequestRepository;
import com.miu.ea.goodpeople.service.criteria.RequestCriteria;
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
 * Service for executing complex queries for {@link Request} entities in the database.
 * The main input is a {@link RequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Request} or a {@link Page} of {@link Request} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RequestQueryService extends QueryService<Request> {

    private final Logger log = LoggerFactory.getLogger(RequestQueryService.class);

    private final RequestRepository requestRepository;

    public RequestQueryService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    /**
     * Return a {@link List} of {@link Request} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Request> findByCriteria(RequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Request> specification = createSpecification(criteria);
        return requestRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Request} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Request> findByCriteria(RequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Request> specification = createSpecification(criteria);
        return requestRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Request> specification = createSpecification(criteria);
        return requestRepository.count(specification);
    }

    /**
     * Function to convert {@link RequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Request> createSpecification(RequestCriteria criteria) {
        Specification<Request> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Request_.id));
            }
            if (criteria.getRequestType() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestType(), Request_.requestType));
            }
            if (criteria.getStartLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStartLocation(), Request_.startLocation));
            }
            if (criteria.getDestination() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDestination(), Request_.destination));
            }
            if (criteria.getNumberOfSeatsRequested() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getNumberOfSeatsRequested(), Request_.numberOfSeatsRequested));
            }
            if (criteria.getProduct() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProduct(), Request_.product));
            }
            if (criteria.getDeliveryLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeliveryLocation(), Request_.deliveryLocation));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Request_.status));
            }
            if (criteria.getRequesterId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRequesterId(), root -> root.join(Request_.requester, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getTripId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTripId(), root -> root.join(Request_.trip, JoinType.LEFT).get(Trip_.id))
                    );
            }
        }
        return specification;
    }
}
