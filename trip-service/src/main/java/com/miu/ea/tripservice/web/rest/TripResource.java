package com.miu.ea.tripservice.web.rest;

import com.miu.ea.tripservice.repository.TripRepository;
import com.miu.ea.tripservice.service.TripService;
import com.miu.ea.tripservice.service.dto.TripDTO;
import com.miu.ea.tripservice.web.rest.errors.BadRequestAlertException;
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
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.miu.ea.tripservice.domain.Trip}.
 */
@RestController
@RequestMapping("/api")
public class TripResource {

    private final Logger log = LoggerFactory.getLogger(TripResource.class);

    private static final String ENTITY_NAME = "trip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripService tripService;

    private final TripRepository tripRepository;

    public TripResource(TripService tripService, TripRepository tripRepository) {
        this.tripService = tripService;
        this.tripRepository = tripRepository;
    }

    /**
     * {@code POST  /trips} : Create a new trip.
     *
     * @param tripDTO the tripDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tripDTO, or with status {@code 400 (Bad Request)} if the trip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trips")
    public ResponseEntity<TripDTO> createTrip(@Valid @RequestBody TripDTO tripDTO) throws URISyntaxException {
        log.debug("REST request to save Trip : {}", tripDTO);
        if (tripDTO.getId() != null) {
            throw new BadRequestAlertException("A new trip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TripDTO result = tripService.save(tripDTO);
        return ResponseEntity
            .created(new URI("/api/trips/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trips/:id} : Updates an existing trip.
     *
     * @param id the id of the tripDTO to save.
     * @param tripDTO the tripDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripDTO,
     * or with status {@code 400 (Bad Request)} if the tripDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tripDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trips/{id}")
    public ResponseEntity<TripDTO> updateTrip(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TripDTO tripDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Trip : {}, {}", id, tripDTO);
        if (tripDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TripDTO result = tripService.update(tripDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tripDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trips/:id} : Partial updates given fields of an existing trip, field will ignore if it is null
     *
     * @param id the id of the tripDTO to save.
     * @param tripDTO the tripDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripDTO,
     * or with status {@code 400 (Bad Request)} if the tripDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tripDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tripDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trips/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TripDTO> partialUpdateTrip(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TripDTO tripDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Trip partially : {}, {}", id, tripDTO);
        if (tripDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TripDTO> result = tripService.partialUpdate(tripDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tripDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trips} : get all the trips.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trips in body.
     */
    @GetMapping("/trips")
    public List<TripDTO> getAllTrips() {
        log.debug("REST request to get all Trips");
        return tripService.findAll();
    }

    /**
     * {@code GET  /trips/:id} : get the "id" trip.
     *
     * @param id the id of the tripDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tripDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trips/{id}")
    public ResponseEntity<TripDTO> getTrip(@PathVariable Long id) {
        log.debug("REST request to get Trip : {}", id);
        Optional<TripDTO> tripDTO = tripService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tripDTO);
    }

    /**
     * {@code DELETE  /trips/:id} : delete the "id" trip.
     *
     * @param id the id of the tripDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trips/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        log.debug("REST request to delete Trip : {}", id);
        tripService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
