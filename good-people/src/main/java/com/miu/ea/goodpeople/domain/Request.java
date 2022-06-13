package com.miu.ea.goodpeople.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.miu.ea.goodpeople.domain.enumeration.RequestStatus;
import com.miu.ea.goodpeople.domain.enumeration.RequestType;
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
    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false)
    private RequestType requestType;

    @Column(name = "start_location")
    private String startLocation;

    @Column(name = "destination")
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

    @ManyToOne
    private User requester;

    @ManyToOne
    @JsonIgnoreProperties(value = { "requests", "owner" }, allowSetters = true)
    private Trip trip;

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

    public User getRequester() {
        return this.requester;
    }

    public void setRequester(User user) {
        this.requester = user;
    }

    public Request requester(User user) {
        this.setRequester(user);
        return this;
    }

    public Trip getTrip() {
        return this.trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Request trip(Trip trip) {
        this.setTrip(trip);
        return this;
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
            ", requestType='" + getRequestType() + "'" +
            ", startLocation='" + getStartLocation() + "'" +
            ", destination='" + getDestination() + "'" +
            ", numberOfSeatsRequested=" + getNumberOfSeatsRequested() +
            ", product='" + getProduct() + "'" +
            ", deliveryLocation='" + getDeliveryLocation() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
