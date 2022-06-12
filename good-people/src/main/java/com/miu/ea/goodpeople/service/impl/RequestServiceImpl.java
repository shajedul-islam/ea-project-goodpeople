package com.miu.ea.goodpeople.service.impl;

import com.miu.ea.goodpeople.domain.Request;
import com.miu.ea.goodpeople.repository.RequestRepository;
import com.miu.ea.goodpeople.service.RequestService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Request}.
 */
@Service
@Transactional
public class RequestServiceImpl implements RequestService {

    private final Logger log = LoggerFactory.getLogger(RequestServiceImpl.class);

    private final RequestRepository requestRepository;

    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Request save(Request request) {
        log.debug("Request to save Request : {}", request);
        return requestRepository.save(request);
    }

    @Override
    public Request update(Request request) {
        log.debug("Request to save Request : {}", request);
        return requestRepository.save(request);
    }

    @Override
    public Optional<Request> partialUpdate(Request request) {
        log.debug("Request to partially update Request : {}", request);

        return requestRepository
            .findById(request.getId())
            .map(existingRequest -> {
                if (request.getRequestType() != null) {
                    existingRequest.setRequestType(request.getRequestType());
                }
                if (request.getStartLocation() != null) {
                    existingRequest.setStartLocation(request.getStartLocation());
                }
                if (request.getDestination() != null) {
                    existingRequest.setDestination(request.getDestination());
                }
                if (request.getNumberOfSeatsRequested() != null) {
                    existingRequest.setNumberOfSeatsRequested(request.getNumberOfSeatsRequested());
                }
                if (request.getProduct() != null) {
                    existingRequest.setProduct(request.getProduct());
                }
                if (request.getDeliveryLocation() != null) {
                    existingRequest.setDeliveryLocation(request.getDeliveryLocation());
                }
                if (request.getStatus() != null) {
                    existingRequest.setStatus(request.getStatus());
                }

                return existingRequest;
            })
            .map(requestRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Request> findAll(Pageable pageable) {
        log.debug("Request to get all Requests");
        return requestRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Request> findOne(Long id) {
        log.debug("Request to get Request : {}", id);
        return requestRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Request : {}", id);
        requestRepository.deleteById(id);
    }
}
