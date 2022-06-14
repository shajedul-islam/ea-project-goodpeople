package com.miu.ea.goodpeople.service.impl;

import com.miu.ea.goodpeople.client.RequestClient;
import com.miu.ea.goodpeople.domain.Request;
import com.miu.ea.goodpeople.domain.Trip;
import com.miu.ea.goodpeople.domain.User;
import com.miu.ea.goodpeople.domain.enumeration.RequestStatus;
import com.miu.ea.goodpeople.domain.enumeration.RequestType;
import com.miu.ea.goodpeople.repository.RequestRepository;
import com.miu.ea.goodpeople.repository.TripRepository;
import com.miu.ea.goodpeople.repository.UserRepository;
import com.miu.ea.goodpeople.service.RequestService;
import com.miu.ea.goodpeople.service.dto.RequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Request}.
 */
@Service
@Transactional
public class RequestServiceImpl implements RequestService {

    private final Logger log = LoggerFactory.getLogger(RequestServiceImpl.class);

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final RequestClient requestClient;

    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository, TripRepository tripRepository, RequestClient requestClient) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.requestClient = requestClient;
    }

    @Override
    public Request save(Request request) {
        log.debug("Request to save Request : {}", request);

        RequestDTO requestDTO = toRequestDto(request);

        requestClient.createRequest(requestDTO);

        return requestRepository.save(request);
    }

    private RequestDTO toRequestDto(Request request) {
        return new RequestDTO(
            null,
            request.getId(),
            request.getTrip().getId(),
            request.getRequester().getId(),
            request.getRequestType().name(),
            request.getStartLocation(),
            request.getDestination(),
            request.getNumberOfSeatsRequested(),
            request.getProduct(),
            request.getDeliveryLocation(),
            request.getStatus().name()
        );
    }

    private Request toRequest(RequestDTO requestDTO) {
        return new Request(
            requestDTO.getId(),
            RequestType.valueOf(requestDTO.getRequestType()),
            requestDTO.getStartLocation(),
            requestDTO.getDestination(),
            requestDTO.getNumberOfSeatsRequested(),
            requestDTO.getProduct(),
            requestDTO.getDeliveryLocation(),
            RequestStatus.valueOf(requestDTO.getStatus()),
            userRepository.findById(requestDTO.getRequesterId()).orElse(null),
            tripRepository.findById(requestDTO.getTripId()).orElse(null)
        );
    }

    @Override
    public Request update(Request request) {
        log.debug("Request to save Request : {}", request);
        RequestDTO requestDTO = toRequestDto(request);
        RequestDTO apiResponse = requestClient.updateRequest(requestDTO.getRequestId(), requestDTO);
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

        List<Request> collect = requestClient.getAllRequests()
            .stream()
            .map(this::toRequest)
            .collect(Collectors.toList());

        return requestRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Request> findAllByRequesterId(Long requesterId) {
        log.debug("Request to get all Requests by requester id");
        User requester = userRepository.getById(requesterId);
        List<Request> apiRes = requestClient.findByRequesterId(requesterId)
            .stream()
            .map(this::toRequest)
            .collect(Collectors.toList());
        return requestRepository.findAllByRequester(requester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Request> findAllByTripId(Long tripId) {
        log.debug("Request to get all Requests by trip id");
        Trip trip = tripRepository.getById(tripId);
        List<Request> apiRes = requestClient.findByTripId(tripId)
            .stream()
            .map(this::toRequest)
            .collect(Collectors.toList());
        return requestRepository.findAllByTrip(trip);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Request> findOne(Long id) {
        log.debug("Request to get Request : {}", id);
        RequestDTO apiRes = requestClient.getRequest(id);
        return requestRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Request : {}", id);
        requestClient.deleteRequest(id);
        requestRepository.deleteById(id);
    }
}
