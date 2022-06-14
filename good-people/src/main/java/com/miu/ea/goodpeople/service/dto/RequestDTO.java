package com.miu.ea.goodpeople.service.dto;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Request.
 */

public class RequestDTO {

    private Long id;

    private Long requestId;

    private Long tripId;

    private Long requesterId;

    private String requestType;

    private String startLocation;

    private String destination;

    private Integer numberOfSeatsRequested;

    private String product;

    private String deliveryLocation;

    private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Long getTripId() {
		return tripId;
	}

	public void setTripId(Long tripId) {
		this.tripId = tripId;
	}

	public Long getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Integer getNumberOfSeatsRequested() {
		return numberOfSeatsRequested;
	}

	public void setNumberOfSeatsRequested(Integer numberOfSeatsRequested) {
		this.numberOfSeatsRequested = numberOfSeatsRequested;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getDeliveryLocation() {
		return deliveryLocation;
	}

	public void setDeliveryLocation(String deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public RequestDTO(Long id, Long requestId, Long tripId, Long requesterId, String requestType, String startLocation,
			String destination, Integer numberOfSeatsRequested, String product, String deliveryLocation,
			String status) {
		super();
		this.id = id;
		this.requestId = requestId;
		this.tripId = tripId;
		this.requesterId = requesterId;
		this.requestType = requestType;
		this.startLocation = startLocation;
		this.destination = destination;
		this.numberOfSeatsRequested = numberOfSeatsRequested;
		this.product = product;
		this.deliveryLocation = deliveryLocation;
		this.status = status;
	}

	public RequestDTO() {
		super();
	}

    
}
