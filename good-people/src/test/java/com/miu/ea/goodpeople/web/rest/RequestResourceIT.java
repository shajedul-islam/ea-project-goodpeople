package com.miu.ea.goodpeople.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.miu.ea.goodpeople.IntegrationTest;
import com.miu.ea.goodpeople.domain.Request;
import com.miu.ea.goodpeople.domain.Trip;
import com.miu.ea.goodpeople.domain.User;
import com.miu.ea.goodpeople.domain.enumeration.RequestStatus;
import com.miu.ea.goodpeople.domain.enumeration.RequestType;
import com.miu.ea.goodpeople.repository.RequestRepository;
import com.miu.ea.goodpeople.service.criteria.RequestCriteria;
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
 * Integration tests for the {@link RequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RequestResourceIT {

    private static final RequestType DEFAULT_REQUEST_TYPE = RequestType.RIDE;
    private static final RequestType UPDATED_REQUEST_TYPE = RequestType.PRODUCT;

    private static final String DEFAULT_START_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_START_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_SEATS_REQUESTED = 1;
    private static final Integer UPDATED_NUMBER_OF_SEATS_REQUESTED = 2;
    private static final Integer SMALLER_NUMBER_OF_SEATS_REQUESTED = 1 - 1;

    private static final String DEFAULT_PRODUCT = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_LOCATION = "BBBBBBBBBB";

    private static final RequestStatus DEFAULT_STATUS = RequestStatus.ACCEPTED;
    private static final RequestStatus UPDATED_STATUS = RequestStatus.REJECTED;

    private static final String ENTITY_API_URL = "/api/requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRequestMockMvc;

    private Request request;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Request createEntity(EntityManager em) {
        Request request = new Request()
            .requestType(DEFAULT_REQUEST_TYPE)
            .startLocation(DEFAULT_START_LOCATION)
            .destination(DEFAULT_DESTINATION)
            .numberOfSeatsRequested(DEFAULT_NUMBER_OF_SEATS_REQUESTED)
            .product(DEFAULT_PRODUCT)
            .deliveryLocation(DEFAULT_DELIVERY_LOCATION)
            .status(DEFAULT_STATUS);
        return request;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Request createUpdatedEntity(EntityManager em) {
        Request request = new Request()
            .requestType(UPDATED_REQUEST_TYPE)
            .startLocation(UPDATED_START_LOCATION)
            .destination(UPDATED_DESTINATION)
            .numberOfSeatsRequested(UPDATED_NUMBER_OF_SEATS_REQUESTED)
            .product(UPDATED_PRODUCT)
            .deliveryLocation(UPDATED_DELIVERY_LOCATION)
            .status(UPDATED_STATUS);
        return request;
    }

    @BeforeEach
    public void initTest() {
        request = createEntity(em);
    }

    @Test
    @Transactional
    void createRequest() throws Exception {
        int databaseSizeBeforeCreate = requestRepository.findAll().size();
        // Create the Request
        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isCreated());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeCreate + 1);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getRequestType()).isEqualTo(DEFAULT_REQUEST_TYPE);
        assertThat(testRequest.getStartLocation()).isEqualTo(DEFAULT_START_LOCATION);
        assertThat(testRequest.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testRequest.getNumberOfSeatsRequested()).isEqualTo(DEFAULT_NUMBER_OF_SEATS_REQUESTED);
        assertThat(testRequest.getProduct()).isEqualTo(DEFAULT_PRODUCT);
        assertThat(testRequest.getDeliveryLocation()).isEqualTo(DEFAULT_DELIVERY_LOCATION);
        assertThat(testRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createRequestWithExistingId() throws Exception {
        // Create the Request with an existing ID
        request.setId(1L);

        int databaseSizeBeforeCreate = requestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRequestTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRepository.findAll().size();
        // set the field null
        request.setRequestType(null);

        // Create the Request, which fails.

        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isBadRequest());

        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRepository.findAll().size();
        // set the field null
        request.setStartLocation(null);

        // Create the Request, which fails.

        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isBadRequest());

        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDestinationIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRepository.findAll().size();
        // set the field null
        request.setDestination(null);

        // Create the Request, which fails.

        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isBadRequest());

        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRequests() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList
        restRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(request.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestType").value(hasItem(DEFAULT_REQUEST_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startLocation").value(hasItem(DEFAULT_START_LOCATION)))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].numberOfSeatsRequested").value(hasItem(DEFAULT_NUMBER_OF_SEATS_REQUESTED)))
            .andExpect(jsonPath("$.[*].product").value(hasItem(DEFAULT_PRODUCT)))
            .andExpect(jsonPath("$.[*].deliveryLocation").value(hasItem(DEFAULT_DELIVERY_LOCATION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get the request
        restRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, request.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(request.getId().intValue()))
            .andExpect(jsonPath("$.requestType").value(DEFAULT_REQUEST_TYPE.toString()))
            .andExpect(jsonPath("$.startLocation").value(DEFAULT_START_LOCATION))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION))
            .andExpect(jsonPath("$.numberOfSeatsRequested").value(DEFAULT_NUMBER_OF_SEATS_REQUESTED))
            .andExpect(jsonPath("$.product").value(DEFAULT_PRODUCT))
            .andExpect(jsonPath("$.deliveryLocation").value(DEFAULT_DELIVERY_LOCATION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getRequestsByIdFiltering() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        Long id = request.getId();

        defaultRequestShouldBeFound("id.equals=" + id);
        defaultRequestShouldNotBeFound("id.notEquals=" + id);

        defaultRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRequestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRequestsByRequestTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where requestType equals to DEFAULT_REQUEST_TYPE
        defaultRequestShouldBeFound("requestType.equals=" + DEFAULT_REQUEST_TYPE);

        // Get all the requestList where requestType equals to UPDATED_REQUEST_TYPE
        defaultRequestShouldNotBeFound("requestType.equals=" + UPDATED_REQUEST_TYPE);
    }

    @Test
    @Transactional
    void getAllRequestsByRequestTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where requestType not equals to DEFAULT_REQUEST_TYPE
        defaultRequestShouldNotBeFound("requestType.notEquals=" + DEFAULT_REQUEST_TYPE);

        // Get all the requestList where requestType not equals to UPDATED_REQUEST_TYPE
        defaultRequestShouldBeFound("requestType.notEquals=" + UPDATED_REQUEST_TYPE);
    }

    @Test
    @Transactional
    void getAllRequestsByRequestTypeIsInShouldWork() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where requestType in DEFAULT_REQUEST_TYPE or UPDATED_REQUEST_TYPE
        defaultRequestShouldBeFound("requestType.in=" + DEFAULT_REQUEST_TYPE + "," + UPDATED_REQUEST_TYPE);

        // Get all the requestList where requestType equals to UPDATED_REQUEST_TYPE
        defaultRequestShouldNotBeFound("requestType.in=" + UPDATED_REQUEST_TYPE);
    }

    @Test
    @Transactional
    void getAllRequestsByRequestTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where requestType is not null
        defaultRequestShouldBeFound("requestType.specified=true");

        // Get all the requestList where requestType is null
        defaultRequestShouldNotBeFound("requestType.specified=false");
    }

    @Test
    @Transactional
    void getAllRequestsByStartLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where startLocation equals to DEFAULT_START_LOCATION
        defaultRequestShouldBeFound("startLocation.equals=" + DEFAULT_START_LOCATION);

        // Get all the requestList where startLocation equals to UPDATED_START_LOCATION
        defaultRequestShouldNotBeFound("startLocation.equals=" + UPDATED_START_LOCATION);
    }

    @Test
    @Transactional
    void getAllRequestsByStartLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where startLocation not equals to DEFAULT_START_LOCATION
        defaultRequestShouldNotBeFound("startLocation.notEquals=" + DEFAULT_START_LOCATION);

        // Get all the requestList where startLocation not equals to UPDATED_START_LOCATION
        defaultRequestShouldBeFound("startLocation.notEquals=" + UPDATED_START_LOCATION);
    }

    @Test
    @Transactional
    void getAllRequestsByStartLocationIsInShouldWork() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where startLocation in DEFAULT_START_LOCATION or UPDATED_START_LOCATION
        defaultRequestShouldBeFound("startLocation.in=" + DEFAULT_START_LOCATION + "," + UPDATED_START_LOCATION);

        // Get all the requestList where startLocation equals to UPDATED_START_LOCATION
        defaultRequestShouldNotBeFound("startLocation.in=" + UPDATED_START_LOCATION);
    }

    @Test
    @Transactional
    void getAllRequestsByStartLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where startLocation is not null
        defaultRequestShouldBeFound("startLocation.specified=true");

        // Get all the requestList where startLocation is null
        defaultRequestShouldNotBeFound("startLocation.specified=false");
    }

    @Test
    @Transactional
    void getAllRequestsByStartLocationContainsSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where startLocation contains DEFAULT_START_LOCATION
        defaultRequestShouldBeFound("startLocation.contains=" + DEFAULT_START_LOCATION);

        // Get all the requestList where startLocation contains UPDATED_START_LOCATION
        defaultRequestShouldNotBeFound("startLocation.contains=" + UPDATED_START_LOCATION);
    }

    @Test
    @Transactional
    void getAllRequestsByStartLocationNotContainsSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where startLocation does not contain DEFAULT_START_LOCATION
        defaultRequestShouldNotBeFound("startLocation.doesNotContain=" + DEFAULT_START_LOCATION);

        // Get all the requestList where startLocation does not contain UPDATED_START_LOCATION
        defaultRequestShouldBeFound("startLocation.doesNotContain=" + UPDATED_START_LOCATION);
    }

    @Test
    @Transactional
    void getAllRequestsByDestinationIsEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where destination equals to DEFAULT_DESTINATION
        defaultRequestShouldBeFound("destination.equals=" + DEFAULT_DESTINATION);

        // Get all the requestList where destination equals to UPDATED_DESTINATION
        defaultRequestShouldNotBeFound("destination.equals=" + UPDATED_DESTINATION);
    }

    @Test
    @Transactional
    void getAllRequestsByDestinationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where destination not equals to DEFAULT_DESTINATION
        defaultRequestShouldNotBeFound("destination.notEquals=" + DEFAULT_DESTINATION);

        // Get all the requestList where destination not equals to UPDATED_DESTINATION
        defaultRequestShouldBeFound("destination.notEquals=" + UPDATED_DESTINATION);
    }

    @Test
    @Transactional
    void getAllRequestsByDestinationIsInShouldWork() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where destination in DEFAULT_DESTINATION or UPDATED_DESTINATION
        defaultRequestShouldBeFound("destination.in=" + DEFAULT_DESTINATION + "," + UPDATED_DESTINATION);

        // Get all the requestList where destination equals to UPDATED_DESTINATION
        defaultRequestShouldNotBeFound("destination.in=" + UPDATED_DESTINATION);
    }

    @Test
    @Transactional
    void getAllRequestsByDestinationIsNullOrNotNull() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where destination is not null
        defaultRequestShouldBeFound("destination.specified=true");

        // Get all the requestList where destination is null
        defaultRequestShouldNotBeFound("destination.specified=false");
    }

    @Test
    @Transactional
    void getAllRequestsByDestinationContainsSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where destination contains DEFAULT_DESTINATION
        defaultRequestShouldBeFound("destination.contains=" + DEFAULT_DESTINATION);

        // Get all the requestList where destination contains UPDATED_DESTINATION
        defaultRequestShouldNotBeFound("destination.contains=" + UPDATED_DESTINATION);
    }

    @Test
    @Transactional
    void getAllRequestsByDestinationNotContainsSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where destination does not contain DEFAULT_DESTINATION
        defaultRequestShouldNotBeFound("destination.doesNotContain=" + DEFAULT_DESTINATION);

        // Get all the requestList where destination does not contain UPDATED_DESTINATION
        defaultRequestShouldBeFound("destination.doesNotContain=" + UPDATED_DESTINATION);
    }

    @Test
    @Transactional
    void getAllRequestsByNumberOfSeatsRequestedIsEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where numberOfSeatsRequested equals to DEFAULT_NUMBER_OF_SEATS_REQUESTED
        defaultRequestShouldBeFound("numberOfSeatsRequested.equals=" + DEFAULT_NUMBER_OF_SEATS_REQUESTED);

        // Get all the requestList where numberOfSeatsRequested equals to UPDATED_NUMBER_OF_SEATS_REQUESTED
        defaultRequestShouldNotBeFound("numberOfSeatsRequested.equals=" + UPDATED_NUMBER_OF_SEATS_REQUESTED);
    }

    @Test
    @Transactional
    void getAllRequestsByNumberOfSeatsRequestedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where numberOfSeatsRequested not equals to DEFAULT_NUMBER_OF_SEATS_REQUESTED
        defaultRequestShouldNotBeFound("numberOfSeatsRequested.notEquals=" + DEFAULT_NUMBER_OF_SEATS_REQUESTED);

        // Get all the requestList where numberOfSeatsRequested not equals to UPDATED_NUMBER_OF_SEATS_REQUESTED
        defaultRequestShouldBeFound("numberOfSeatsRequested.notEquals=" + UPDATED_NUMBER_OF_SEATS_REQUESTED);
    }

    @Test
    @Transactional
    void getAllRequestsByNumberOfSeatsRequestedIsInShouldWork() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where numberOfSeatsRequested in DEFAULT_NUMBER_OF_SEATS_REQUESTED or UPDATED_NUMBER_OF_SEATS_REQUESTED
        defaultRequestShouldBeFound(
            "numberOfSeatsRequested.in=" + DEFAULT_NUMBER_OF_SEATS_REQUESTED + "," + UPDATED_NUMBER_OF_SEATS_REQUESTED
        );

        // Get all the requestList where numberOfSeatsRequested equals to UPDATED_NUMBER_OF_SEATS_REQUESTED
        defaultRequestShouldNotBeFound("numberOfSeatsRequested.in=" + UPDATED_NUMBER_OF_SEATS_REQUESTED);
    }

    @Test
    @Transactional
    void getAllRequestsByNumberOfSeatsRequestedIsNullOrNotNull() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where numberOfSeatsRequested is not null
        defaultRequestShouldBeFound("numberOfSeatsRequested.specified=true");

        // Get all the requestList where numberOfSeatsRequested is null
        defaultRequestShouldNotBeFound("numberOfSeatsRequested.specified=false");
    }

    @Test
    @Transactional
    void getAllRequestsByNumberOfSeatsRequestedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where numberOfSeatsRequested is greater than or equal to DEFAULT_NUMBER_OF_SEATS_REQUESTED
        defaultRequestShouldBeFound("numberOfSeatsRequested.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_SEATS_REQUESTED);

        // Get all the requestList where numberOfSeatsRequested is greater than or equal to UPDATED_NUMBER_OF_SEATS_REQUESTED
        defaultRequestShouldNotBeFound("numberOfSeatsRequested.greaterThanOrEqual=" + UPDATED_NUMBER_OF_SEATS_REQUESTED);
    }

    @Test
    @Transactional
    void getAllRequestsByNumberOfSeatsRequestedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where numberOfSeatsRequested is less than or equal to DEFAULT_NUMBER_OF_SEATS_REQUESTED
        defaultRequestShouldBeFound("numberOfSeatsRequested.lessThanOrEqual=" + DEFAULT_NUMBER_OF_SEATS_REQUESTED);

        // Get all the requestList where numberOfSeatsRequested is less than or equal to SMALLER_NUMBER_OF_SEATS_REQUESTED
        defaultRequestShouldNotBeFound("numberOfSeatsRequested.lessThanOrEqual=" + SMALLER_NUMBER_OF_SEATS_REQUESTED);
    }

    @Test
    @Transactional
    void getAllRequestsByNumberOfSeatsRequestedIsLessThanSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where numberOfSeatsRequested is less than DEFAULT_NUMBER_OF_SEATS_REQUESTED
        defaultRequestShouldNotBeFound("numberOfSeatsRequested.lessThan=" + DEFAULT_NUMBER_OF_SEATS_REQUESTED);

        // Get all the requestList where numberOfSeatsRequested is less than UPDATED_NUMBER_OF_SEATS_REQUESTED
        defaultRequestShouldBeFound("numberOfSeatsRequested.lessThan=" + UPDATED_NUMBER_OF_SEATS_REQUESTED);
    }

    @Test
    @Transactional
    void getAllRequestsByNumberOfSeatsRequestedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where numberOfSeatsRequested is greater than DEFAULT_NUMBER_OF_SEATS_REQUESTED
        defaultRequestShouldNotBeFound("numberOfSeatsRequested.greaterThan=" + DEFAULT_NUMBER_OF_SEATS_REQUESTED);

        // Get all the requestList where numberOfSeatsRequested is greater than SMALLER_NUMBER_OF_SEATS_REQUESTED
        defaultRequestShouldBeFound("numberOfSeatsRequested.greaterThan=" + SMALLER_NUMBER_OF_SEATS_REQUESTED);
    }

    @Test
    @Transactional
    void getAllRequestsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where product equals to DEFAULT_PRODUCT
        defaultRequestShouldBeFound("product.equals=" + DEFAULT_PRODUCT);

        // Get all the requestList where product equals to UPDATED_PRODUCT
        defaultRequestShouldNotBeFound("product.equals=" + UPDATED_PRODUCT);
    }

    @Test
    @Transactional
    void getAllRequestsByProductIsNotEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where product not equals to DEFAULT_PRODUCT
        defaultRequestShouldNotBeFound("product.notEquals=" + DEFAULT_PRODUCT);

        // Get all the requestList where product not equals to UPDATED_PRODUCT
        defaultRequestShouldBeFound("product.notEquals=" + UPDATED_PRODUCT);
    }

    @Test
    @Transactional
    void getAllRequestsByProductIsInShouldWork() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where product in DEFAULT_PRODUCT or UPDATED_PRODUCT
        defaultRequestShouldBeFound("product.in=" + DEFAULT_PRODUCT + "," + UPDATED_PRODUCT);

        // Get all the requestList where product equals to UPDATED_PRODUCT
        defaultRequestShouldNotBeFound("product.in=" + UPDATED_PRODUCT);
    }

    @Test
    @Transactional
    void getAllRequestsByProductIsNullOrNotNull() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where product is not null
        defaultRequestShouldBeFound("product.specified=true");

        // Get all the requestList where product is null
        defaultRequestShouldNotBeFound("product.specified=false");
    }

    @Test
    @Transactional
    void getAllRequestsByProductContainsSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where product contains DEFAULT_PRODUCT
        defaultRequestShouldBeFound("product.contains=" + DEFAULT_PRODUCT);

        // Get all the requestList where product contains UPDATED_PRODUCT
        defaultRequestShouldNotBeFound("product.contains=" + UPDATED_PRODUCT);
    }

    @Test
    @Transactional
    void getAllRequestsByProductNotContainsSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where product does not contain DEFAULT_PRODUCT
        defaultRequestShouldNotBeFound("product.doesNotContain=" + DEFAULT_PRODUCT);

        // Get all the requestList where product does not contain UPDATED_PRODUCT
        defaultRequestShouldBeFound("product.doesNotContain=" + UPDATED_PRODUCT);
    }

    @Test
    @Transactional
    void getAllRequestsByDeliveryLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where deliveryLocation equals to DEFAULT_DELIVERY_LOCATION
        defaultRequestShouldBeFound("deliveryLocation.equals=" + DEFAULT_DELIVERY_LOCATION);

        // Get all the requestList where deliveryLocation equals to UPDATED_DELIVERY_LOCATION
        defaultRequestShouldNotBeFound("deliveryLocation.equals=" + UPDATED_DELIVERY_LOCATION);
    }

    @Test
    @Transactional
    void getAllRequestsByDeliveryLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where deliveryLocation not equals to DEFAULT_DELIVERY_LOCATION
        defaultRequestShouldNotBeFound("deliveryLocation.notEquals=" + DEFAULT_DELIVERY_LOCATION);

        // Get all the requestList where deliveryLocation not equals to UPDATED_DELIVERY_LOCATION
        defaultRequestShouldBeFound("deliveryLocation.notEquals=" + UPDATED_DELIVERY_LOCATION);
    }

    @Test
    @Transactional
    void getAllRequestsByDeliveryLocationIsInShouldWork() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where deliveryLocation in DEFAULT_DELIVERY_LOCATION or UPDATED_DELIVERY_LOCATION
        defaultRequestShouldBeFound("deliveryLocation.in=" + DEFAULT_DELIVERY_LOCATION + "," + UPDATED_DELIVERY_LOCATION);

        // Get all the requestList where deliveryLocation equals to UPDATED_DELIVERY_LOCATION
        defaultRequestShouldNotBeFound("deliveryLocation.in=" + UPDATED_DELIVERY_LOCATION);
    }

    @Test
    @Transactional
    void getAllRequestsByDeliveryLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where deliveryLocation is not null
        defaultRequestShouldBeFound("deliveryLocation.specified=true");

        // Get all the requestList where deliveryLocation is null
        defaultRequestShouldNotBeFound("deliveryLocation.specified=false");
    }

    @Test
    @Transactional
    void getAllRequestsByDeliveryLocationContainsSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where deliveryLocation contains DEFAULT_DELIVERY_LOCATION
        defaultRequestShouldBeFound("deliveryLocation.contains=" + DEFAULT_DELIVERY_LOCATION);

        // Get all the requestList where deliveryLocation contains UPDATED_DELIVERY_LOCATION
        defaultRequestShouldNotBeFound("deliveryLocation.contains=" + UPDATED_DELIVERY_LOCATION);
    }

    @Test
    @Transactional
    void getAllRequestsByDeliveryLocationNotContainsSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where deliveryLocation does not contain DEFAULT_DELIVERY_LOCATION
        defaultRequestShouldNotBeFound("deliveryLocation.doesNotContain=" + DEFAULT_DELIVERY_LOCATION);

        // Get all the requestList where deliveryLocation does not contain UPDATED_DELIVERY_LOCATION
        defaultRequestShouldBeFound("deliveryLocation.doesNotContain=" + UPDATED_DELIVERY_LOCATION);
    }

    @Test
    @Transactional
    void getAllRequestsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where status equals to DEFAULT_STATUS
        defaultRequestShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the requestList where status equals to UPDATED_STATUS
        defaultRequestShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllRequestsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where status not equals to DEFAULT_STATUS
        defaultRequestShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the requestList where status not equals to UPDATED_STATUS
        defaultRequestShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllRequestsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultRequestShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the requestList where status equals to UPDATED_STATUS
        defaultRequestShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllRequestsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requestList where status is not null
        defaultRequestShouldBeFound("status.specified=true");

        // Get all the requestList where status is null
        defaultRequestShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllRequestsByRequesterIsEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);
        User requester;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            requester = UserResourceIT.createEntity(em);
            em.persist(requester);
            em.flush();
        } else {
            requester = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(requester);
        em.flush();
        request.setRequester(requester);
        requestRepository.saveAndFlush(request);
        Long requesterId = requester.getId();

        // Get all the requestList where requester equals to requesterId
        defaultRequestShouldBeFound("requesterId.equals=" + requesterId);

        // Get all the requestList where requester equals to (requesterId + 1)
        defaultRequestShouldNotBeFound("requesterId.equals=" + (requesterId + 1));
    }

    @Test
    @Transactional
    void getAllRequestsByTripIsEqualToSomething() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);
        Trip trip;
        if (TestUtil.findAll(em, Trip.class).isEmpty()) {
            trip = TripResourceIT.createEntity(em);
            em.persist(trip);
            em.flush();
        } else {
            trip = TestUtil.findAll(em, Trip.class).get(0);
        }
        em.persist(trip);
        em.flush();
        request.setTrip(trip);
        requestRepository.saveAndFlush(request);
        Long tripId = trip.getId();

        // Get all the requestList where trip equals to tripId
        defaultRequestShouldBeFound("tripId.equals=" + tripId);

        // Get all the requestList where trip equals to (tripId + 1)
        defaultRequestShouldNotBeFound("tripId.equals=" + (tripId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRequestShouldBeFound(String filter) throws Exception {
        restRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(request.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestType").value(hasItem(DEFAULT_REQUEST_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startLocation").value(hasItem(DEFAULT_START_LOCATION)))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].numberOfSeatsRequested").value(hasItem(DEFAULT_NUMBER_OF_SEATS_REQUESTED)))
            .andExpect(jsonPath("$.[*].product").value(hasItem(DEFAULT_PRODUCT)))
            .andExpect(jsonPath("$.[*].deliveryLocation").value(hasItem(DEFAULT_DELIVERY_LOCATION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRequestShouldNotBeFound(String filter) throws Exception {
        restRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRequest() throws Exception {
        // Get the request
        restRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        int databaseSizeBeforeUpdate = requestRepository.findAll().size();

        // Update the request
        Request updatedRequest = requestRepository.findById(request.getId()).get();
        // Disconnect from session so that the updates on updatedRequest are not directly saved in db
        em.detach(updatedRequest);
        updatedRequest
            .requestType(UPDATED_REQUEST_TYPE)
            .startLocation(UPDATED_START_LOCATION)
            .destination(UPDATED_DESTINATION)
            .numberOfSeatsRequested(UPDATED_NUMBER_OF_SEATS_REQUESTED)
            .product(UPDATED_PRODUCT)
            .deliveryLocation(UPDATED_DELIVERY_LOCATION)
            .status(UPDATED_STATUS);

        restRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRequest))
            )
            .andExpect(status().isOk());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getRequestType()).isEqualTo(UPDATED_REQUEST_TYPE);
        assertThat(testRequest.getStartLocation()).isEqualTo(UPDATED_START_LOCATION);
        assertThat(testRequest.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testRequest.getNumberOfSeatsRequested()).isEqualTo(UPDATED_NUMBER_OF_SEATS_REQUESTED);
        assertThat(testRequest.getProduct()).isEqualTo(UPDATED_PRODUCT);
        assertThat(testRequest.getDeliveryLocation()).isEqualTo(UPDATED_DELIVERY_LOCATION);
        assertThat(testRequest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, request.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(request))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(request))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRequestWithPatch() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        int databaseSizeBeforeUpdate = requestRepository.findAll().size();

        // Update the request using partial update
        Request partialUpdatedRequest = new Request();
        partialUpdatedRequest.setId(request.getId());

        partialUpdatedRequest
            .requestType(UPDATED_REQUEST_TYPE)
            .startLocation(UPDATED_START_LOCATION)
            .numberOfSeatsRequested(UPDATED_NUMBER_OF_SEATS_REQUESTED)
            .product(UPDATED_PRODUCT)
            .deliveryLocation(UPDATED_DELIVERY_LOCATION);

        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequest))
            )
            .andExpect(status().isOk());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getRequestType()).isEqualTo(UPDATED_REQUEST_TYPE);
        assertThat(testRequest.getStartLocation()).isEqualTo(UPDATED_START_LOCATION);
        assertThat(testRequest.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testRequest.getNumberOfSeatsRequested()).isEqualTo(UPDATED_NUMBER_OF_SEATS_REQUESTED);
        assertThat(testRequest.getProduct()).isEqualTo(UPDATED_PRODUCT);
        assertThat(testRequest.getDeliveryLocation()).isEqualTo(UPDATED_DELIVERY_LOCATION);
        assertThat(testRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateRequestWithPatch() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        int databaseSizeBeforeUpdate = requestRepository.findAll().size();

        // Update the request using partial update
        Request partialUpdatedRequest = new Request();
        partialUpdatedRequest.setId(request.getId());

        partialUpdatedRequest
            .requestType(UPDATED_REQUEST_TYPE)
            .startLocation(UPDATED_START_LOCATION)
            .destination(UPDATED_DESTINATION)
            .numberOfSeatsRequested(UPDATED_NUMBER_OF_SEATS_REQUESTED)
            .product(UPDATED_PRODUCT)
            .deliveryLocation(UPDATED_DELIVERY_LOCATION)
            .status(UPDATED_STATUS);

        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequest))
            )
            .andExpect(status().isOk());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
        Request testRequest = requestList.get(requestList.size() - 1);
        assertThat(testRequest.getRequestType()).isEqualTo(UPDATED_REQUEST_TYPE);
        assertThat(testRequest.getStartLocation()).isEqualTo(UPDATED_START_LOCATION);
        assertThat(testRequest.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testRequest.getNumberOfSeatsRequested()).isEqualTo(UPDATED_NUMBER_OF_SEATS_REQUESTED);
        assertThat(testRequest.getProduct()).isEqualTo(UPDATED_PRODUCT);
        assertThat(testRequest.getDeliveryLocation()).isEqualTo(UPDATED_DELIVERY_LOCATION);
        assertThat(testRequest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, request.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(request))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(request))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRequest() throws Exception {
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();
        request.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Request in the database
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        int databaseSizeBeforeDelete = requestRepository.findAll().size();

        // Delete the request
        restRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, request.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Request> requestList = requestRepository.findAll();
        assertThat(requestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
