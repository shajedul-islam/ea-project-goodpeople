package com.miu.ea.requestservice.domain;

import com.miu.ea.requestservice.domain.enumeration.RequestStatus;
import com.miu.ea.requestservice.domain.enumeration.RequestType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Request.
 */
@Entity
@Table(name = "request")
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "request_id", nullable = false)
    private Long requestId;

    @NotNull
    @Column(name = "trip_id", nullable = false)
    private Long tripId;

    @NotNull
    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false)
    private RequestType requestType;

    @NotNull
    @Column(name = "start_location", nullable = false)
    private String startLocation;

    @NotNull
    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "number_of_seats_requested")
    private Integer numberOfSeatsRequested;

    @Column(name = "product")
    private String product;

    @Column(name = "delivery_location")
    private String deliveryLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Request id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestId() {
        return this.requestId;
    }

    public Request requestId(Long requestId) {
        this.setRequestId(requestId);
        return this;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getTripId() {
        return this.tripId;
    }

    public Request tripId(Long tripId) {
        this.setTripId(tripId);
        return this;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getRequesterId() {
        return this.requesterId;
    }

    public Request requesterId(Long requesterId) {
        this.setRequesterId(requesterId);
        return this;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public RequestType getRequestType() {
        return this.requestType;
    }

    public Request requestType(RequestType requestType) {
        this.setRequestType(requestType);
        return this;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getStartLocation() {
        return this.startLocation;
    }

    public Request startLocation(String startLocation) {
        this.setStartLocation(startLocation);
        return this;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getDestination() {
        return this.destination;
    }

    public Request destination(String destination) {
        this.setDestination(destination);
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getNumberOfSeatsRequested() {
        return this.numberOfSeatsRequested;
    }

    public Request numberOfSeatsRequested(Integer numberOfSeatsRequested) {
        this.setNumberOfSeatsRequested(numberOfSeatsRequested);
        return this;
    }

    public void setNumberOfSeatsRequested(Integer numberOfSeatsRequested) {
        this.numberOfSeatsRequested = numberOfSeatsRequested;
    }

    public String getProduct() {
        return this.product;
    }

    public Request product(String product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDeliveryLocation() {
        return this.deliveryLocation;
    }

    public Request deliveryLocation(String deliveryLocation) {
        this.setDeliveryLocation(deliveryLocation);
        return this;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public RequestStatus getStatus() {
        return this.status;
    }

    public Request status(RequestStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Request)) {
            return false;
        }
        return id != null && id.equals(((Request) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Request{" +
            "id=" + getId() +
            ", requestId=" + getRequestId() +
            ", tripId=" + getTripId() +
            ", requesterId=" + getRequesterId() +
            ", requestType='" + getRequestType() + "'" +
            ", startLocation='" + getStartLocation() + "'" +
            ", destination='" + getDestination() + "'" +
            ", numberOfSeatsRequested=" + getNumberOfSeatsRequested() +
            ", product='" + getProduct() + "'" +
            ", deliveryLocation='" + getDeliveryLocation() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }


	public Request() {
		super();
	}

	public Request(Long id2, Long id3, Long tripId2, Long requesterId2, RequestType valueOf, String startLocation2,
			String destination2, Integer numberOfSeatsRequested2, String product2, String deliveryLocation2,
			RequestStatus valueOf2) {
		// TODO Auto-generated constructor stub
		super();
		this.id = id2;
		this.requestId = id3;
		this.tripId = tripId2;
		this.requesterId = requesterId2;
		this.requestType = valueOf;
		this.startLocation = startLocation2;
		this.destination = destination2;
		this.numberOfSeatsRequested = numberOfSeatsRequested2;
		this.product = product2;
		this.deliveryLocation = deliveryLocation2;
		this.status = valueOf2;
	}
    
}
