package com.miu.ea.tripservice.service.dto;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

public class TripDTO {

    private Long id;

    private Long tripId;

    private Long ownerId;

    private String startLocation;

    private String destination;

    private String startTime;

    private Boolean canOfferRide;

    private Boolean canBringProduct;

    private Integer numberOfOfferedSeats;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTripId() {
		return tripId;
	}

	public void setTripId(Long tripId) {
		this.tripId = tripId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Boolean getCanOfferRide() {
		return canOfferRide;
	}

	public void setCanOfferRide(Boolean canOfferRide) {
		this.canOfferRide = canOfferRide;
	}

	public Boolean getCanBringProduct() {
		return canBringProduct;
	}

	public void setCanBringProduct(Boolean canBringProduct) {
		this.canBringProduct = canBringProduct;
	}

	public Integer getNumberOfOfferedSeats() {
		return numberOfOfferedSeats;
	}

	public void setNumberOfOfferedSeats(Integer numberOfOfferedSeats) {
		this.numberOfOfferedSeats = numberOfOfferedSeats;
	}

	public TripDTO() {
		super();
	}

	public TripDTO(Long id, Long tripId, Long ownerId, String startLocation, String destination,
			String startTime, Boolean canOfferRide, Boolean canBringProduct, Integer numberOfOfferedSeats) {
		super();
		this.id = id;
		this.tripId = tripId;
		this.ownerId = ownerId;
		this.startLocation = startLocation;
		this.destination = destination;
		this.startTime = startTime;
		this.canOfferRide = canOfferRide;
		this.canBringProduct = canBringProduct;
		this.numberOfOfferedSeats = numberOfOfferedSeats;
	}
    
    
}
