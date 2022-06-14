package com.miu.ea.requestservice.service.dto;

import com.miu.ea.requestservice.domain.enumeration.RequestStatus;
import com.miu.ea.requestservice.domain.enumeration.RequestType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Request.
 */

public class RequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;


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
        return this.id;
    }

    public RequestDTO id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestId() {
        return this.requestId;
    }

    public RequestDTO requestId(Long requestId) {
        this.setRequestId(requestId);
        return this;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getTripId() {
        return this.tripId;
    }

    public RequestDTO tripId(Long tripId) {
        this.setTripId(tripId);
        return this;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getRequesterId() {
        return this.requesterId;
    }

    public RequestDTO requesterId(Long requesterId) {
        this.setRequesterId(requesterId);
        return this;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequestType() {
        return this.requestType;
    }

    public RequestDTO requestType(String requestType) {
        this.setRequestType(requestType);
        return this;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getStartLocation() {
        return this.startLocation;
    }

    public RequestDTO startLocation(String startLocation) {
        this.setStartLocation(startLocation);
        return this;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getDestination() {
        return this.destination;
    }

    public RequestDTO destination(String destination) {
        this.setDestination(destination);
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getNumberOfSeatsRequested() {
        return this.numberOfSeatsRequested;
    }

    public RequestDTO numberOfSeatsRequested(Integer numberOfSeatsRequested) {
        this.setNumberOfSeatsRequested(numberOfSeatsRequested);
        return this;
    }

    public void setNumberOfSeatsRequested(Integer numberOfSeatsRequested) {
        this.numberOfSeatsRequested = numberOfSeatsRequested;
    }

    public String getProduct() {
        return this.product;
    }

    public RequestDTO product(String product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDeliveryLocation() {
        return this.deliveryLocation;
    }

    public RequestDTO deliveryLocation(String deliveryLocation) {
        this.setDeliveryLocation(deliveryLocation);
        return this;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public String getStatus() {
        return this.status;
    }

    public RequestDTO status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestDTO)) {
            return false;
        }
        return id != null && id.equals(((RequestDTO) o).id);
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
}
