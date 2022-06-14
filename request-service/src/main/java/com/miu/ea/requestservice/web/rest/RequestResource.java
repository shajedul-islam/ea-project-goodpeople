package com.miu.ea.requestservice.web.rest;


import com.miu.ea.requestservice.domain.Request;
import com.miu.ea.requestservice.domain.enumeration.RequestStatus;
import com.miu.ea.requestservice.domain.enumeration.RequestType;
import com.miu.ea.requestservice.repository.RequestRepository;
import com.miu.ea.requestservice.service.dto.RequestDTO;
import com.miu.ea.requestservice.web.rest.errors.BadRequestAlertException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.miu.ea.requestservice.domain.Request}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RequestResource {

    private final Logger log = LoggerFactory.getLogger(RequestResource.class);

    private static final String ENTITY_NAME = "request";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequestRepository requestRepository;

    public RequestResource(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    /**
     * {@code POST  /requests} : Create a new request.
     *
     * @param request the request to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new request, or with status {@code 400 (Bad Request)} if the request has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/requests")
    public ResponseEntity<Request> createRequest(@Valid @RequestBody RequestDTO requestDTO) throws URISyntaxException {
        log.debug("REST request to save Request : {}", requestDTO);
		/*
		 * if (request.getId() != null) { throw new
		 * BadRequestAlertException("A new request cannot already have an ID",
		 * ENTITY_NAME, "idexists"); }
		 */
        
        Request request = toRequest(requestDTO);
        Request result = requestRepository.save(request);
        return ResponseEntity
            .created(new URI("/api/requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    private Request toRequest(RequestDTO requestDTO) {
        return new Request(
            requestDTO.getId(),
            requestDTO.getId(),
            requestDTO.getTripId(),
            requestDTO.getRequesterId(),     
            RequestType.valueOf(requestDTO.getRequestType()),
            requestDTO.getStartLocation(),
            requestDTO.getDestination(),
            requestDTO.getNumberOfSeatsRequested(),
            requestDTO.getProduct(),
            requestDTO.getDeliveryLocation(),
            RequestStatus.valueOf(requestDTO.getStatus())
        );
    }

    /**
     * {@code PUT  /requests/:id} : Updates an existing request.
     *
     * @param id the id of the request to save.
     * @param request the request to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated request,
     * or with status {@code 400 (Bad Request)} if the request is not valid,
     * or with status {@code 500 (Internal Server Error)} if the request couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/requests/{id}")
    public ResponseEntity<Request> updateRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Request request
    ) throws URISyntaxException {
        log.debug("REST request to update Request : {}, {}", id, request);
        if (request.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, request.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Request result = requestRepository.save(request);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, request.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /requests/:id} : Partial updates given fields of an existing request, field will ignore if it is null
     *
     * @param id the id of the request to save.
     * @param request the request to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated request,
     * or with status {@code 400 (Bad Request)} if the request is not valid,
     * or with status {@code 404 (Not Found)} if the request is not found,
     * or with status {@code 500 (Internal Server Error)} if the request couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Request> partialUpdateRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Request request
    ) throws URISyntaxException {
        log.debug("REST request to partial update Request partially : {}, {}", id, request);
        if (request.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, request.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Request> result = requestRepository
            .findById(request.getId())
            .map(existingRequest -> {
                if (request.getRequestId() != null) {
                    existingRequest.setRequestId(request.getRequestId());
                }
                if (request.getTripId() != null) {
                    existingRequest.setTripId(request.getTripId());
                }
                if (request.getRequesterId() != null) {
                    existingRequest.setRequesterId(request.getRequesterId());
                }
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

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, request.getId().toString())
        );
    }

    /**
     * {@code GET  /requests} : get all the requests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requests in body.
     */
    @GetMapping("/requests")
    public List<Request> getAllRequests() {
        log.debug("REST request to get all Requests");
        return requestRepository.findAll();
    }

    /**
     * {@code GET  /requests/:id} : get the "id" request.
     *
     * @param id the id of the request to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the request, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/requests/{id}")
    public ResponseEntity<Request> getRequest(@PathVariable Long id) {
        log.debug("REST request to get Request : {}", id);
        Optional<Request> request = requestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(request);
    }

    /**
     * {@code DELETE  /requests/:id} : delete the "id" request.
     *
     * @param id the id of the request to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/requests/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        log.debug("REST request to delete Request : {}", id);
        requestRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
    
    static class StatusUpdateReq {
        String status;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public StatusUpdateReq() {
			super();
		}
        
        
        
    }

    @PutMapping("/requests/{id}/status-update")
    public ResponseEntity<Void> statusUpdate(@PathVariable("id") Long id, @RequestBody  StatusUpdateReq req) {
        Request byId = requestRepository.findById(id).orElseThrow(RuntimeException::new);
        byId.setStatus(RequestStatus.valueOf(req.status));
        requestRepository.save(byId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("requests/trip/{id}")
    public List<Request> requestByTripId(@PathVariable("id") Long id) {
        return requestRepository.findRequestsByTripId(id);
    }

    @GetMapping("requests/requester/{id}")
    public List<Request> requestsByRequesterId(@PathVariable("id") Long id) {
        return requestRepository.findRequestsByRequesterId(id);
    }
}
