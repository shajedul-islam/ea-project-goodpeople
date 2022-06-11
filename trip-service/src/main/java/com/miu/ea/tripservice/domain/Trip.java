package com.miu.ea.tripservice.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Trip.
 */
@Entity
@Table(name = "trip")
public class Trip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @NotNull
    @Column(name = "start_location", nullable = false)
    private String startLocation;

    @NotNull
    @Column(name = "destination", nullable = false)
    private String destination;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    @Column(name = "can_offer_ride")
    private Boolean canOfferRide;

    @Column(name = "can_bring_product")
    private Boolean canBringProduct;

    @NotNull
    @Column(name = "number_of_offered_seats", nullable = false)
    private Integer numberOfOfferedSeats;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trip id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public Trip ownerId(Long ownerId) {
        this.setOwnerId(ownerId);
        return this;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getStartLocation() {
        return this.startLocation;
    }

    public Trip startLocation(String startLocation) {
        this.setStartLocation(startLocation);
        return this;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getDestination() {
        return this.destination;
    }

    public Trip destination(String destination) {
        this.setDestination(destination);
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    public Trip startTime(ZonedDateTime startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public Boolean getCanOfferRide() {
        return this.canOfferRide;
    }

    public Trip canOfferRide(Boolean canOfferRide) {
        this.setCanOfferRide(canOfferRide);
        return this;
    }

    public void setCanOfferRide(Boolean canOfferRide) {
        this.canOfferRide = canOfferRide;
    }

    public Boolean getCanBringProduct() {
        return this.canBringProduct;
    }

    public Trip canBringProduct(Boolean canBringProduct) {
        this.setCanBringProduct(canBringProduct);
        return this;
    }

    public void setCanBringProduct(Boolean canBringProduct) {
        this.canBringProduct = canBringProduct;
    }

    public Integer getNumberOfOfferedSeats() {
        return this.numberOfOfferedSeats;
    }

    public Trip numberOfOfferedSeats(Integer numberOfOfferedSeats) {
        this.setNumberOfOfferedSeats(numberOfOfferedSeats);
        return this;
    }

    public void setNumberOfOfferedSeats(Integer numberOfOfferedSeats) {
        this.numberOfOfferedSeats = numberOfOfferedSeats;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trip)) {
            return false;
        }
        return id != null && id.equals(((Trip) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trip{" +
            "id=" + getId() +
            ", ownerId=" + getOwnerId() +
            ", startLocation='" + getStartLocation() + "'" +
            ", destination='" + getDestination() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", canOfferRide='" + getCanOfferRide() + "'" +
            ", canBringProduct='" + getCanBringProduct() + "'" +
            ", numberOfOfferedSeats=" + getNumberOfOfferedSeats() +
            "}";
    }
}
