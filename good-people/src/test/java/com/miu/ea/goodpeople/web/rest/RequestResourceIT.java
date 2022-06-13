package com.miu.ea.goodpeople.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.miu.ea.goodpeople.IntegrationTest;
import com.miu.ea.goodpeople.domain.Request;
import com.miu.ea.goodpeople.domain.enumeration.RequestStatus;
import com.miu.ea.goodpeople.domain.enumeration.RequestType;
import com.miu.ea.goodpeople.repository.RequestRepository;
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
