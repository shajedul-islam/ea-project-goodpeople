package com.miu.ea.goodpeople.web.rest;

import com.miu.ea.goodpeople.domain.Request;
import com.miu.ea.goodpeople.domain.User;
import com.miu.ea.goodpeople.repository.RequestRepository;
import com.miu.ea.goodpeople.service.RequestService;
import com.miu.ea.goodpeople.service.UserService;
import com.miu.ea.goodpeople.web.rest.errors.BadRequestAlertException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.miu.ea.goodpeople.domain.Request}.
 */
@RestController
@RequestMapping("/api")
public class RequestResource {

    private final Logger log = LoggerFactory.getLogger(RequestResource.class);

    private static final String ENTITY_NAME = "request";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequestService requestService;

    private final RequestRepository requestRepository;
    
    private final UserService userService;

    public RequestResource(RequestService requestService, RequestRepository requestRepository, UserService userService) {
        this.requestService = requestService;
        this.requestRepository = requestRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /requests} : Create a new request.
     *
     * @param request the request to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new request, or with status {@code 400 (Bad Request)} if the request has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/requests")
    public ResponseEntity<Request> createRequest(@Valid @RequestBody Request request) throws URISyntaxException {
        log.debug("REST request to save Request : {}", request);
        if (request.getId() != null) {
            throw new BadRequestAlertException("A new request cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        if (request.getRequester() == null) {
        	final User requester = userService.getUserWithAuthorities().get();
            request.setRequester(requester);
        }
        
        Request result = requestService.save(request);
        return ResponseEntity
            .created(new URI("/api/requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
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

        Request result = requestService.update(request);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, request.getId().toString()))
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

        Optional<Request> result = requestService.partialUpdate(request);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, request.getId().toString())
        );
    }

    /**
     * {@code GET  /requests} : get all the requests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requests in body.
     */
    @GetMapping("/requests")
    public ResponseEntity<List<Request>> getAllRequests(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Requests");
        Page<Request> page = requestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /requests/requester/:requesterId} : get all the requests by requester id
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requests in body.
     */
    @GetMapping("/requests/requester/{requesterId}")
    public ResponseEntity<List<Request>> getAllRequestsByRequester(@PathVariable Long requesterId) {
        log.debug("REST request to get a page of Requests by requester id ");
        List<Request> requests = requestService.findAllByRequesterId(requesterId);
        return ResponseEntity.ok().body(requests);
    }
    
    /**
     * {@code GET  /requests/trip/:tripId} : get all the requests by trip id
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requests in body.
     */
    @GetMapping("/requests/trip/{tripId}")
    public ResponseEntity<List<Request>> getAllRequestsByTripId(@PathVariable Long tripId) {
        log.debug("REST request to get a page of Requests by trip id");
        List<Request> requests = requestService.findAllByTripId(tripId);
        return ResponseEntity.ok().body(requests);
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
        Optional<Request> request = requestService.findOne(id);
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
        requestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
    
    //TODO: get all requests by a trip owner
    //TODO: get all requests by a requester
}
