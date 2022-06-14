package com.miu.ea.tripservice.web.rest;

import com.miu.ea.tripservice.domain.Trip;
import com.miu.ea.tripservice.repository.TripRepository;
import com.miu.ea.tripservice.service.TripService;
import com.miu.ea.tripservice.service.dto.TripDTO;
import com.miu.ea.tripservice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
     * @param tripDTO the trip to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trip, or with status {@code 400 (Bad Request)} if the trip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trips")
    public ResponseEntity<Trip> createTrip(@Valid @RequestBody TripDTO tripDTO) throws URISyntaxException {
        log.debug("REST request to save Trip : {}", tripDTO);
        if (tripDTO.getId() != null) {
            throw new BadRequestAlertException("A new trip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
		/*
		 * DateTimeFormatter formatter =
		 * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
		 * 
		 * ZonedDateTime zdtWithZoneOffset = ZonedDateTime.parse(tripDTO.getStartTime(),
		 * formatter); System.out.println("********************** time: " +
		 * zdtWithZoneOffset);
		 */
        
        Trip trip = tripDTOtoTrip(tripDTO);
        
        Trip result = tripService.save(trip);
        return ResponseEntity
            .created(new URI("/api/trips/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    private Trip tripDTOtoTrip(TripDTO tripDTO) {
    	return new Trip(
        		null, 
        		tripDTO.getTripId(),
        		tripDTO.getOwnerId(),
        		tripDTO.getStartLocation(),
        		tripDTO.getDestination(),
        		ZonedDateTime.now(),
        		tripDTO.getCanOfferRide(),
        		tripDTO.getCanBringProduct(),
        		tripDTO.getNumberOfOfferedSeats()
        		);
    }

    /**
     * {@code PUT  /trips/:id} : Updates an existing trip.
     *
     * @param id the id of the trip to save.
     * @param trip the trip to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trip,
     * or with status {@code 400 (Bad Request)} if the trip is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trip couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trips/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody TripDTO trip)
        throws URISyntaxException {
        log.debug("REST request to update Trip : {}, {}", id, trip);
        if (trip.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trip.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Trip result = tripService.update(tripDTOtoTrip(trip));
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trip.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trips/:id} : Partial updates given fields of an existing trip, field will ignore if it is null
     *
     * @param id the id of the trip to save.
     * @param trip the trip to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trip,
     * or with status {@code 400 (Bad Request)} if the trip is not valid,
     * or with status {@code 404 (Not Found)} if the trip is not found,
     * or with status {@code 500 (Internal Server Error)} if the trip couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trips/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Trip> partialUpdateTrip(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Trip trip
    ) throws URISyntaxException {
        log.debug("REST request to partial update Trip partially : {}, {}", id, trip);
        if (trip.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trip.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Trip> result = tripService.partialUpdate(trip);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trip.getId().toString())
        );
    }

    /**
     * {@code GET  /trips} : get all the trips.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trips in body.
     */
    @GetMapping("/trips")
    public List<Trip> getAllTrips() {
        log.debug("REST request to get all Trips");
        return tripService.findAll();
    }

    /**
     * {@code GET  /trips/:id} : get the "id" trip.
     *
     * @param id the id of the trip to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trip, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trips/{id}")
    public ResponseEntity<Trip> getTrip(@PathVariable Long id) {
        log.debug("REST request to get Trip : {}", id);
        Optional<Trip> trip = tripService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trip);
    }

    /**
     * {@code DELETE  /trips/:id} : delete the "id" trip.
     *
     * @param id the id of the trip to delete.
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
