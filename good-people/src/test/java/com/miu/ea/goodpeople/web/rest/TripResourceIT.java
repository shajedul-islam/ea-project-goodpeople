package com.miu.ea.goodpeople.web.rest;

import static com.miu.ea.goodpeople.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.miu.ea.goodpeople.IntegrationTest;
import com.miu.ea.goodpeople.domain.Request;
import com.miu.ea.goodpeople.domain.Trip;
import com.miu.ea.goodpeople.domain.User;
import com.miu.ea.goodpeople.repository.TripRepository;
import com.miu.ea.goodpeople.service.criteria.TripCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TripResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TripResourceIT {

    private static final String DEFAULT_START_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_START_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_CAN_OFFER_RIDE = false;
    private static final Boolean UPDATED_CAN_OFFER_RIDE = true;

    private static final Boolean DEFAULT_CAN_BRING_PRODUCT = false;
    private static final Boolean UPDATED_CAN_BRING_PRODUCT = true;

    private static final Integer DEFAULT_NUMBER_OF_SEATS_OFFERED = 1;
    private static final Integer UPDATED_NUMBER_OF_SEATS_OFFERED = 2;
    private static final Integer SMALLER_NUMBER_OF_SEATS_OFFERED = 1 - 1;

    private static final Integer DEFAULT_NUMBER_OF_SEATS_REMAINING = 1;
    private static final Integer UPDATED_NUMBER_OF_SEATS_REMAINING = 2;
    private static final Integer SMALLER_NUMBER_OF_SEATS_REMAINING = 1 - 1;

    private static final String ENTITY_API_URL = "/api/trips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripMockMvc;

    private Trip trip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trip createEntity(EntityManager em) {
        Trip trip = new Trip()
            .startLocation(DEFAULT_START_LOCATION)
            .destination(DEFAULT_DESTINATION)
            .startTime(DEFAULT_START_TIME)
            .canOfferRide(DEFAULT_CAN_OFFER_RIDE)
            .canBringProduct(DEFAULT_CAN_BRING_PRODUCT)
            .numberOfSeatsOffered(DEFAULT_NUMBER_OF_SEATS_OFFERED)
            .numberOfSeatsRemaining(DEFAULT_NUMBER_OF_SEATS_REMAINING);
        return trip;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trip createUpdatedEntity(EntityManager em) {
        Trip trip = new Trip()
            .startLocation(UPDATED_START_LOCATION)
            .destination(UPDATED_DESTINATION)
            .startTime(UPDATED_START_TIME)
            .canOfferRide(UPDATED_CAN_OFFER_RIDE)
            .canBringProduct(UPDATED_CAN_BRING_PRODUCT)
            .numberOfSeatsOffered(UPDATED_NUMBER_OF_SEATS_OFFERED)
            .numberOfSeatsRemaining(UPDATED_NUMBER_OF_SEATS_REMAINING);
        return trip;
    }

    @BeforeEach
    public void initTest() {
        trip = createEntity(em);
    }

    @Test
    @Transactional
    void createTrip() throws Exception {
        int databaseSizeBeforeCreate = tripRepository.findAll().size();
        // Create the Trip
        restTripMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trip)))
            .andExpect(status().isCreated());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeCreate + 1);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getStartLocation()).isEqualTo(DEFAULT_START_LOCATION);
        assertThat(testTrip.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testTrip.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testTrip.getCanOfferRide()).isEqualTo(DEFAULT_CAN_OFFER_RIDE);
        assertThat(testTrip.getCanBringProduct()).isEqualTo(DEFAULT_CAN_BRING_PRODUCT);
        assertThat(testTrip.getNumberOfSeatsOffered()).isEqualTo(DEFAULT_NUMBER_OF_SEATS_OFFERED);
        assertThat(testTrip.getNumberOfSeatsRemaining()).isEqualTo(DEFAULT_NUMBER_OF_SEATS_REMAINING);
    }

    @Test
    @Transactional
    void createTripWithExistingId() throws Exception {
        // Create the Trip with an existing ID
        trip.setId(1L);

        int databaseSizeBeforeCreate = tripRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trip)))
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripRepository.findAll().size();
        // set the field null
        trip.setStartLocation(null);

        // Create the Trip, which fails.

        restTripMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trip)))
            .andExpect(status().isBadRequest());

        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDestinationIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripRepository.findAll().size();
        // set the field null
        trip.setDestination(null);

        // Create the Trip, which fails.

        restTripMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trip)))
            .andExpect(status().isBadRequest());

        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripRepository.findAll().size();
        // set the field null
        trip.setStartTime(null);

        // Create the Trip, which fails.

        restTripMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trip)))
            .andExpect(status().isBadRequest());

        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberOfSeatsOfferedIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripRepository.findAll().size();
        // set the field null
        trip.setNumberOfSeatsOffered(null);

        // Create the Trip, which fails.

        restTripMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trip)))
            .andExpect(status().isBadRequest());

        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrips() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trip.getId().intValue())))
            .andExpect(jsonPath("$.[*].startLocation").value(hasItem(DEFAULT_START_LOCATION)))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].canOfferRide").value(hasItem(DEFAULT_CAN_OFFER_RIDE.booleanValue())))
            .andExpect(jsonPath("$.[*].canBringProduct").value(hasItem(DEFAULT_CAN_BRING_PRODUCT.booleanValue())))
            .andExpect(jsonPath("$.[*].numberOfSeatsOffered").value(hasItem(DEFAULT_NUMBER_OF_SEATS_OFFERED)))
            .andExpect(jsonPath("$.[*].numberOfSeatsRemaining").value(hasItem(DEFAULT_NUMBER_OF_SEATS_REMAINING)));
    }

    @Test
    @Transactional
    void getTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get the trip
        restTripMockMvc
            .perform(get(ENTITY_API_URL_ID, trip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trip.getId().intValue()))
            .andExpect(jsonPath("$.startLocation").value(DEFAULT_START_LOCATION))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.canOfferRide").value(DEFAULT_CAN_OFFER_RIDE.booleanValue()))
            .andExpect(jsonPath("$.canBringProduct").value(DEFAULT_CAN_BRING_PRODUCT.booleanValue()))
            .andExpect(jsonPath("$.numberOfSeatsOffered").value(DEFAULT_NUMBER_OF_SEATS_OFFERED))
            .andExpect(jsonPath("$.numberOfSeatsRemaining").value(DEFAULT_NUMBER_OF_SEATS_REMAINING));
    }

    @Test
    @Transactional
    void getTripsByIdFiltering() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        Long id = trip.getId();

        defaultTripShouldBeFound("id.equals=" + id);
        defaultTripShouldNotBeFound("id.notEquals=" + id);

        defaultTripShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTripShouldNotBeFound("id.greaterThan=" + id);

        defaultTripShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTripShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTripsByStartLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startLocation equals to DEFAULT_START_LOCATION
        defaultTripShouldBeFound("startLocation.equals=" + DEFAULT_START_LOCATION);

        // Get all the tripList where startLocation equals to UPDATED_START_LOCATION
        defaultTripShouldNotBeFound("startLocation.equals=" + UPDATED_START_LOCATION);
    }

    @Test
    @Transactional
    void getAllTripsByStartLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startLocation not equals to DEFAULT_START_LOCATION
        defaultTripShouldNotBeFound("startLocation.notEquals=" + DEFAULT_START_LOCATION);

        // Get all the tripList where startLocation not equals to UPDATED_START_LOCATION
        defaultTripShouldBeFound("startLocation.notEquals=" + UPDATED_START_LOCATION);
    }

    @Test
    @Transactional
    void getAllTripsByStartLocationIsInShouldWork() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startLocation in DEFAULT_START_LOCATION or UPDATED_START_LOCATION
        defaultTripShouldBeFound("startLocation.in=" + DEFAULT_START_LOCATION + "," + UPDATED_START_LOCATION);

        // Get all the tripList where startLocation equals to UPDATED_START_LOCATION
        defaultTripShouldNotBeFound("startLocation.in=" + UPDATED_START_LOCATION);
    }

    @Test
    @Transactional
    void getAllTripsByStartLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startLocation is not null
        defaultTripShouldBeFound("startLocation.specified=true");

        // Get all the tripList where startLocation is null
        defaultTripShouldNotBeFound("startLocation.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByStartLocationContainsSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startLocation contains DEFAULT_START_LOCATION
        defaultTripShouldBeFound("startLocation.contains=" + DEFAULT_START_LOCATION);

        // Get all the tripList where startLocation contains UPDATED_START_LOCATION
        defaultTripShouldNotBeFound("startLocation.contains=" + UPDATED_START_LOCATION);
    }

    @Test
    @Transactional
    void getAllTripsByStartLocationNotContainsSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startLocation does not contain DEFAULT_START_LOCATION
        defaultTripShouldNotBeFound("startLocation.doesNotContain=" + DEFAULT_START_LOCATION);

        // Get all the tripList where startLocation does not contain UPDATED_START_LOCATION
        defaultTripShouldBeFound("startLocation.doesNotContain=" + UPDATED_START_LOCATION);
    }

    @Test
    @Transactional
    void getAllTripsByDestinationIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where destination equals to DEFAULT_DESTINATION
        defaultTripShouldBeFound("destination.equals=" + DEFAULT_DESTINATION);

        // Get all the tripList where destination equals to UPDATED_DESTINATION
        defaultTripShouldNotBeFound("destination.equals=" + UPDATED_DESTINATION);
    }

    @Test
    @Transactional
    void getAllTripsByDestinationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where destination not equals to DEFAULT_DESTINATION
        defaultTripShouldNotBeFound("destination.notEquals=" + DEFAULT_DESTINATION);

        // Get all the tripList where destination not equals to UPDATED_DESTINATION
        defaultTripShouldBeFound("destination.notEquals=" + UPDATED_DESTINATION);
    }

    @Test
    @Transactional
    void getAllTripsByDestinationIsInShouldWork() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where destination in DEFAULT_DESTINATION or UPDATED_DESTINATION
        defaultTripShouldBeFound("destination.in=" + DEFAULT_DESTINATION + "," + UPDATED_DESTINATION);

        // Get all the tripList where destination equals to UPDATED_DESTINATION
        defaultTripShouldNotBeFound("destination.in=" + UPDATED_DESTINATION);
    }

    @Test
    @Transactional
    void getAllTripsByDestinationIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where destination is not null
        defaultTripShouldBeFound("destination.specified=true");

        // Get all the tripList where destination is null
        defaultTripShouldNotBeFound("destination.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByDestinationContainsSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where destination contains DEFAULT_DESTINATION
        defaultTripShouldBeFound("destination.contains=" + DEFAULT_DESTINATION);

        // Get all the tripList where destination contains UPDATED_DESTINATION
        defaultTripShouldNotBeFound("destination.contains=" + UPDATED_DESTINATION);
    }

    @Test
    @Transactional
    void getAllTripsByDestinationNotContainsSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where destination does not contain DEFAULT_DESTINATION
        defaultTripShouldNotBeFound("destination.doesNotContain=" + DEFAULT_DESTINATION);

        // Get all the tripList where destination does not contain UPDATED_DESTINATION
        defaultTripShouldBeFound("destination.doesNotContain=" + UPDATED_DESTINATION);
    }

    @Test
    @Transactional
    void getAllTripsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startTime equals to DEFAULT_START_TIME
        defaultTripShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the tripList where startTime equals to UPDATED_START_TIME
        defaultTripShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllTripsByStartTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startTime not equals to DEFAULT_START_TIME
        defaultTripShouldNotBeFound("startTime.notEquals=" + DEFAULT_START_TIME);

        // Get all the tripList where startTime not equals to UPDATED_START_TIME
        defaultTripShouldBeFound("startTime.notEquals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllTripsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultTripShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the tripList where startTime equals to UPDATED_START_TIME
        defaultTripShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllTripsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startTime is not null
        defaultTripShouldBeFound("startTime.specified=true");

        // Get all the tripList where startTime is null
        defaultTripShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByStartTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startTime is greater than or equal to DEFAULT_START_TIME
        defaultTripShouldBeFound("startTime.greaterThanOrEqual=" + DEFAULT_START_TIME);

        // Get all the tripList where startTime is greater than or equal to UPDATED_START_TIME
        defaultTripShouldNotBeFound("startTime.greaterThanOrEqual=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllTripsByStartTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startTime is less than or equal to DEFAULT_START_TIME
        defaultTripShouldBeFound("startTime.lessThanOrEqual=" + DEFAULT_START_TIME);

        // Get all the tripList where startTime is less than or equal to SMALLER_START_TIME
        defaultTripShouldNotBeFound("startTime.lessThanOrEqual=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    void getAllTripsByStartTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startTime is less than DEFAULT_START_TIME
        defaultTripShouldNotBeFound("startTime.lessThan=" + DEFAULT_START_TIME);

        // Get all the tripList where startTime is less than UPDATED_START_TIME
        defaultTripShouldBeFound("startTime.lessThan=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllTripsByStartTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where startTime is greater than DEFAULT_START_TIME
        defaultTripShouldNotBeFound("startTime.greaterThan=" + DEFAULT_START_TIME);

        // Get all the tripList where startTime is greater than SMALLER_START_TIME
        defaultTripShouldBeFound("startTime.greaterThan=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    void getAllTripsByCanOfferRideIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where canOfferRide equals to DEFAULT_CAN_OFFER_RIDE
        defaultTripShouldBeFound("canOfferRide.equals=" + DEFAULT_CAN_OFFER_RIDE);

        // Get all the tripList where canOfferRide equals to UPDATED_CAN_OFFER_RIDE
        defaultTripShouldNotBeFound("canOfferRide.equals=" + UPDATED_CAN_OFFER_RIDE);
    }

    @Test
    @Transactional
    void getAllTripsByCanOfferRideIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where canOfferRide not equals to DEFAULT_CAN_OFFER_RIDE
        defaultTripShouldNotBeFound("canOfferRide.notEquals=" + DEFAULT_CAN_OFFER_RIDE);

        // Get all the tripList where canOfferRide not equals to UPDATED_CAN_OFFER_RIDE
        defaultTripShouldBeFound("canOfferRide.notEquals=" + UPDATED_CAN_OFFER_RIDE);
    }

    @Test
    @Transactional
    void getAllTripsByCanOfferRideIsInShouldWork() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where canOfferRide in DEFAULT_CAN_OFFER_RIDE or UPDATED_CAN_OFFER_RIDE
        defaultTripShouldBeFound("canOfferRide.in=" + DEFAULT_CAN_OFFER_RIDE + "," + UPDATED_CAN_OFFER_RIDE);

        // Get all the tripList where canOfferRide equals to UPDATED_CAN_OFFER_RIDE
        defaultTripShouldNotBeFound("canOfferRide.in=" + UPDATED_CAN_OFFER_RIDE);
    }

    @Test
    @Transactional
    void getAllTripsByCanOfferRideIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where canOfferRide is not null
        defaultTripShouldBeFound("canOfferRide.specified=true");

        // Get all the tripList where canOfferRide is null
        defaultTripShouldNotBeFound("canOfferRide.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByCanBringProductIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where canBringProduct equals to DEFAULT_CAN_BRING_PRODUCT
        defaultTripShouldBeFound("canBringProduct.equals=" + DEFAULT_CAN_BRING_PRODUCT);

        // Get all the tripList where canBringProduct equals to UPDATED_CAN_BRING_PRODUCT
        defaultTripShouldNotBeFound("canBringProduct.equals=" + UPDATED_CAN_BRING_PRODUCT);
    }

    @Test
    @Transactional
    void getAllTripsByCanBringProductIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where canBringProduct not equals to DEFAULT_CAN_BRING_PRODUCT
        defaultTripShouldNotBeFound("canBringProduct.notEquals=" + DEFAULT_CAN_BRING_PRODUCT);

        // Get all the tripList where canBringProduct not equals to UPDATED_CAN_BRING_PRODUCT
        defaultTripShouldBeFound("canBringProduct.notEquals=" + UPDATED_CAN_BRING_PRODUCT);
    }

    @Test
    @Transactional
    void getAllTripsByCanBringProductIsInShouldWork() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where canBringProduct in DEFAULT_CAN_BRING_PRODUCT or UPDATED_CAN_BRING_PRODUCT
        defaultTripShouldBeFound("canBringProduct.in=" + DEFAULT_CAN_BRING_PRODUCT + "," + UPDATED_CAN_BRING_PRODUCT);

        // Get all the tripList where canBringProduct equals to UPDATED_CAN_BRING_PRODUCT
        defaultTripShouldNotBeFound("canBringProduct.in=" + UPDATED_CAN_BRING_PRODUCT);
    }

    @Test
    @Transactional
    void getAllTripsByCanBringProductIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where canBringProduct is not null
        defaultTripShouldBeFound("canBringProduct.specified=true");

        // Get all the tripList where canBringProduct is null
        defaultTripShouldNotBeFound("canBringProduct.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByNumberOfSeatsOfferedIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where numberOfSeatsOffered equals to DEFAULT_NUMBER_OF_SEATS_OFFERED
        defaultTripShouldBeFound("numberOfSeatsOffered.equals=" + DEFAULT_NUMBER_OF_SEATS_OFFERED);

        // Get all the tripList where numberOfSeatsOffered equals to UPDATED_NUMBER_OF_SEATS_OFFERED
        defaultTripShouldNotBeFound("numberOfSeatsOffered.equals=" + UPDATED_NUMBER_OF_SEATS_OFFERED);
    }

    @Test
    @Transactional
    void getAllTripsByNumberOfSeatsOfferedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where numberOfSeatsOffered not equals to DEFAULT_NUMBER_OF_SEATS_OFFERED
        defaultTripShouldNotBeFound("numberOfSeatsOffered.notEquals=" + DEFAULT_NUMBER_OF_SEATS_OFFERED);

        // Get all the tripList where numberOfSeatsOffered not equals to UPDATED_NUMBER_OF_SEATS_OFFERED
        defaultTripShouldBeFound("numberOfSeatsOffered.notEquals=" + UPDATED_NUMBER_OF_SEATS_OFFERED);
    }

    @Test
    @Transactional
    void getAllTripsByNumberOfSeatsOfferedIsInShouldWork() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where numberOfSeatsOffered in DEFAULT_NUMBER_OF_SEATS_OFFERED or UPDATED_NUMBER_OF_SEATS_OFFERED
        defaultTripShouldBeFound("numberOfSeatsOffered.in=" + DEFAULT_NUMBER_OF_SEATS_OFFERED + "," + UPDATED_NUMBER_OF_SEATS_OFFERED);

        // Get all the tripList where numberOfSeatsOffered equals to UPDATED_NUMBER_OF_SEATS_OFFERED
        defaultTripShouldNotBeFound("numberOfSeatsOffered.in=" + UPDATED_NUMBER_OF_SEATS_OFFERED);
    }

    @Test
    @Transactional
    void getAllTripsByNumberOfSeatsOfferedIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where numberOfSeatsOffered is not null
        defaultTripShouldBeFound("numberOfSeatsOffered.specified=true");

        // Get all the tripList where numberOfSeatsOffered is null
        defaultTripShouldNotBeFound("numberOfSeatsOffered.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByNumberOfSeatsOfferedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where numberOfSeatsOffered is greater than or equal to DEFAULT_NUMBER_OF_SEATS_OFFERED
        defaultTripShouldBeFound("numberOfSeatsOffered.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_SEATS_OFFERED);

        // Get all the tripList where numberOfSeatsOffered is greater than or equal to UPDATED_NUMBER_OF_SEATS_OFFERED
        defaultTripShouldNotBeFound("numberOfSeatsOffered.greaterThanOrEqual=" + UPDATED_NUMBER_OF_SEATS_OFFERED);
    }

    @Test
    @Transactional
    void getAllTripsByNumberOfSeatsOfferedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where numberOfSeatsOffered is less than or equal to DEFAULT_NUMBER_OF_SEATS_OFFERED
        defaultTripShouldBeFound("numberOfSeatsOffered.lessThanOrEqual=" + DEFAULT_NUMBER_OF_SEATS_OFFERED);

        // Get all the tripList where numberOfSeatsOffered is less than or equal to SMALLER_NUMBER_OF_SEATS_OFFERED
        defaultTripShouldNotBeFound("numberOfSeatsOffered.lessThanOrEqual=" + SMALLER_NUMBER_OF_SEATS_OFFERED);
    }

    @Test
    @Transactional
    void getAllTripsByNumberOfSeatsOfferedIsLessThanSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where numberOfSeatsOffered is less than DEFAULT_NUMBER_OF_SEATS_OFFERED
        defaultTripShouldNotBeFound("numberOfSeatsOffered.lessThan=" + DEFAULT_NUMBER_OF_SEATS_OFFERED);

        // Get all the tripList where numberOfSeatsOffered is less than UPDATED_NUMBER_OF_SEATS_OFFERED
        defaultTripShouldBeFound("numberOfSeatsOffered.lessThan=" + UPDATED_NUMBER_OF_SEATS_OFFERED);
    }

    @Test
    @Transactional
    void getAllTripsByNumberOfSeatsOfferedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where numberOfSeatsOffered is greater than DEFAULT_NUMBER_OF_SEATS_OFFERED
        defaultTripShouldNotBeFound("numberOfSeatsOffered.greaterThan=" + DEFAULT_NUMBER_OF_SEATS_OFFERED);

        // Get all the tripList where numberOfSeatsOffered is greater than SMALLER_NUMBER_OF_SEATS_OFFERED
        defaultTripShouldBeFound("numberOfSeatsOffered.greaterThan=" + SMALLER_NUMBER_OF_SEATS_OFFERED);
    }

    @Test
    @Transactional
    void getAllTripsByNumberOfSeatsRemainingIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where numberOfSeatsRemaining equals to DEFAULT_NUMBER_OF_SEATS_REMAINING
        defaultTripShouldBeFound("numberOfSeatsRemaining.equals=" + DEFAULT_NUMBER_OF_SEATS_REMAINING);

        // Get all the tripList where numberOfSeatsRemaining equals to UPDATED_NUMBER_OF_SEATS_REMAINING
        defaultTripShouldNotBeFound("numberOfSeatsRemaining.equals=" + UPDATED_NUMBER_OF_SEATS_REMAINING);
    }

    @Test
    @Transactional
    void getAllTripsByNumberOfSeatsRemainingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where numberOfSeatsRemaining not equals to DEFAULT_NUMBER_OF_SEATS_REMAINING
        defaultTripShouldNotBeFound("numberOfSeatsRemaining.notEquals=" + DEFAULT_NUMBER_OF_SEATS_REMAINING);

        // Get all the tripList where numberOfSeatsRemaining not equals to UPDATED_NUMBER_OF_SEATS_REMAINING
        defaultTripShouldBeFound("numberOfSeatsRemaining.notEquals=" + UPDATED_NUMBER_OF_SEATS_REMAINING);
    }

    @Test
    @Transactional
    void getAllTripsByNumberOfSeatsRemainingIsInShouldWork() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where numberOfSeatsRemaining in DEFAULT_NUMBER_OF_SEATS_REMAINING or UPDATED_NUMBER_OF_SEATS_REMAINING
        defaultTripShouldBeFound(
            "numberOfSeatsRemaining.in=" + DEFAULT_NUMBER_OF_SEATS_REMAINING + "," + UPDATED_NUMBER_OF_SEATS_REMAINING
        );

        // Get all the tripList where numberOfSeatsRemaining equals to UPDATED_NUMBER_OF_SEATS_REMAINING
        defaultTripShouldNotBeFound("numberOfSeatsRemaining.in=" + UPDATED_NUMBER_OF_SEATS_REMAINING);
    }

    @Test
    @Transactional
    void getAllTripsByNumberOfSeatsRemainingIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where numberOfSeatsRemaining is not null
        defaultTripShouldBeFound("numberOfSeatsRemaining.specified=true");

        // Get all the tripList where numberOfSeatsRemaining is null
        defaultTripShouldNotBeFound("numberOfSeatsRemaining.specified=false");
    }

    @Test
    @Transactional
    void getAllTripsByNumberOfSeatsRemainingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where numberOfSeatsRemaining is greater than or equal to DEFAULT_NUMBER_OF_SEATS_REMAINING
        defaultTripShouldBeFound("numberOfSeatsRemaining.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_SEATS_REMAINING);

        // Get all the tripList where numberOfSeatsRemaining is greater than or equal to UPDATED_NUMBER_OF_SEATS_REMAINING
        defaultTripShouldNotBeFound("numberOfSeatsRemaining.greaterThanOrEqual=" + UPDATED_NUMBER_OF_SEATS_REMAINING);
    }

    @Test
    @Transactional
    void getAllTripsByNumberOfSeatsRemainingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where numberOfSeatsRemaining is less than or equal to DEFAULT_NUMBER_OF_SEATS_REMAINING
        defaultTripShouldBeFound("numberOfSeatsRemaining.lessThanOrEqual=" + DEFAULT_NUMBER_OF_SEATS_REMAINING);

        // Get all the tripList where numberOfSeatsRemaining is less than or equal to SMALLER_NUMBER_OF_SEATS_REMAINING
        defaultTripShouldNotBeFound("numberOfSeatsRemaining.lessThanOrEqual=" + SMALLER_NUMBER_OF_SEATS_REMAINING);
    }

    @Test
    @Transactional
    void getAllTripsByNumberOfSeatsRemainingIsLessThanSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where numberOfSeatsRemaining is less than DEFAULT_NUMBER_OF_SEATS_REMAINING
        defaultTripShouldNotBeFound("numberOfSeatsRemaining.lessThan=" + DEFAULT_NUMBER_OF_SEATS_REMAINING);

        // Get all the tripList where numberOfSeatsRemaining is less than UPDATED_NUMBER_OF_SEATS_REMAINING
        defaultTripShouldBeFound("numberOfSeatsRemaining.lessThan=" + UPDATED_NUMBER_OF_SEATS_REMAINING);
    }

    @Test
    @Transactional
    void getAllTripsByNumberOfSeatsRemainingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where numberOfSeatsRemaining is greater than DEFAULT_NUMBER_OF_SEATS_REMAINING
        defaultTripShouldNotBeFound("numberOfSeatsRemaining.greaterThan=" + DEFAULT_NUMBER_OF_SEATS_REMAINING);

        // Get all the tripList where numberOfSeatsRemaining is greater than SMALLER_NUMBER_OF_SEATS_REMAINING
        defaultTripShouldBeFound("numberOfSeatsRemaining.greaterThan=" + SMALLER_NUMBER_OF_SEATS_REMAINING);
    }

    @Test
    @Transactional
    void getAllTripsByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);
        User owner;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            owner = UserResourceIT.createEntity(em);
            em.persist(owner);
            em.flush();
        } else {
            owner = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(owner);
        em.flush();
        trip.setOwner(owner);
        tripRepository.saveAndFlush(trip);
        Long ownerId = owner.getId();

        // Get all the tripList where owner equals to ownerId
        defaultTripShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the tripList where owner equals to (ownerId + 1)
        defaultTripShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    @Test
    @Transactional
    void getAllTripsByRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);
        Request request;
        if (TestUtil.findAll(em, Request.class).isEmpty()) {
            request = RequestResourceIT.createEntity(em);
            em.persist(request);
            em.flush();
        } else {
            request = TestUtil.findAll(em, Request.class).get(0);
        }
        em.persist(request);
        em.flush();
        trip.addRequest(request);
        tripRepository.saveAndFlush(trip);
        Long requestId = request.getId();

        // Get all the tripList where request equals to requestId
        defaultTripShouldBeFound("requestId.equals=" + requestId);

        // Get all the tripList where request equals to (requestId + 1)
        defaultTripShouldNotBeFound("requestId.equals=" + (requestId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTripShouldBeFound(String filter) throws Exception {
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trip.getId().intValue())))
            .andExpect(jsonPath("$.[*].startLocation").value(hasItem(DEFAULT_START_LOCATION)))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].canOfferRide").value(hasItem(DEFAULT_CAN_OFFER_RIDE.booleanValue())))
            .andExpect(jsonPath("$.[*].canBringProduct").value(hasItem(DEFAULT_CAN_BRING_PRODUCT.booleanValue())))
            .andExpect(jsonPath("$.[*].numberOfSeatsOffered").value(hasItem(DEFAULT_NUMBER_OF_SEATS_OFFERED)))
            .andExpect(jsonPath("$.[*].numberOfSeatsRemaining").value(hasItem(DEFAULT_NUMBER_OF_SEATS_REMAINING)));

        // Check, that the count call also returns 1
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTripShouldNotBeFound(String filter) throws Exception {
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTrip() throws Exception {
        // Get the trip
        restTripMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        int databaseSizeBeforeUpdate = tripRepository.findAll().size();

        // Update the trip
        Trip updatedTrip = tripRepository.findById(trip.getId()).get();
        // Disconnect from session so that the updates on updatedTrip are not directly saved in db
        em.detach(updatedTrip);
        updatedTrip
            .startLocation(UPDATED_START_LOCATION)
            .destination(UPDATED_DESTINATION)
            .startTime(UPDATED_START_TIME)
            .canOfferRide(UPDATED_CAN_OFFER_RIDE)
            .canBringProduct(UPDATED_CAN_BRING_PRODUCT)
            .numberOfSeatsOffered(UPDATED_NUMBER_OF_SEATS_OFFERED)
            .numberOfSeatsRemaining(UPDATED_NUMBER_OF_SEATS_REMAINING);

        restTripMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrip.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTrip))
            )
            .andExpect(status().isOk());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getStartLocation()).isEqualTo(UPDATED_START_LOCATION);
        assertThat(testTrip.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testTrip.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testTrip.getCanOfferRide()).isEqualTo(UPDATED_CAN_OFFER_RIDE);
        assertThat(testTrip.getCanBringProduct()).isEqualTo(UPDATED_CAN_BRING_PRODUCT);
        assertThat(testTrip.getNumberOfSeatsOffered()).isEqualTo(UPDATED_NUMBER_OF_SEATS_OFFERED);
        assertThat(testTrip.getNumberOfSeatsRemaining()).isEqualTo(UPDATED_NUMBER_OF_SEATS_REMAINING);
    }

    @Test
    @Transactional
    void putNonExistingTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trip.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trip))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trip))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trip)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTripWithPatch() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        int databaseSizeBeforeUpdate = tripRepository.findAll().size();

        // Update the trip using partial update
        Trip partialUpdatedTrip = new Trip();
        partialUpdatedTrip.setId(trip.getId());

        partialUpdatedTrip.startLocation(UPDATED_START_LOCATION).canBringProduct(UPDATED_CAN_BRING_PRODUCT);

        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrip))
            )
            .andExpect(status().isOk());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getStartLocation()).isEqualTo(UPDATED_START_LOCATION);
        assertThat(testTrip.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testTrip.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testTrip.getCanOfferRide()).isEqualTo(DEFAULT_CAN_OFFER_RIDE);
        assertThat(testTrip.getCanBringProduct()).isEqualTo(UPDATED_CAN_BRING_PRODUCT);
        assertThat(testTrip.getNumberOfSeatsOffered()).isEqualTo(DEFAULT_NUMBER_OF_SEATS_OFFERED);
        assertThat(testTrip.getNumberOfSeatsRemaining()).isEqualTo(DEFAULT_NUMBER_OF_SEATS_REMAINING);
    }

    @Test
    @Transactional
    void fullUpdateTripWithPatch() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        int databaseSizeBeforeUpdate = tripRepository.findAll().size();

        // Update the trip using partial update
        Trip partialUpdatedTrip = new Trip();
        partialUpdatedTrip.setId(trip.getId());

        partialUpdatedTrip
            .startLocation(UPDATED_START_LOCATION)
            .destination(UPDATED_DESTINATION)
            .startTime(UPDATED_START_TIME)
            .canOfferRide(UPDATED_CAN_OFFER_RIDE)
            .canBringProduct(UPDATED_CAN_BRING_PRODUCT)
            .numberOfSeatsOffered(UPDATED_NUMBER_OF_SEATS_OFFERED)
            .numberOfSeatsRemaining(UPDATED_NUMBER_OF_SEATS_REMAINING);

        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrip))
            )
            .andExpect(status().isOk());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getStartLocation()).isEqualTo(UPDATED_START_LOCATION);
        assertThat(testTrip.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testTrip.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testTrip.getCanOfferRide()).isEqualTo(UPDATED_CAN_OFFER_RIDE);
        assertThat(testTrip.getCanBringProduct()).isEqualTo(UPDATED_CAN_BRING_PRODUCT);
        assertThat(testTrip.getNumberOfSeatsOffered()).isEqualTo(UPDATED_NUMBER_OF_SEATS_OFFERED);
        assertThat(testTrip.getNumberOfSeatsRemaining()).isEqualTo(UPDATED_NUMBER_OF_SEATS_REMAINING);
    }

    @Test
    @Transactional
    void patchNonExistingTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trip))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trip))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(trip)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        int databaseSizeBeforeDelete = tripRepository.findAll().size();

        // Delete the trip
        restTripMockMvc
            .perform(delete(ENTITY_API_URL_ID, trip.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
