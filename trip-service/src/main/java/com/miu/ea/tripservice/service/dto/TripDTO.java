package com.miu.ea.tripservice.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.miu.ea.tripservice.domain.Trip} entity.
 */
public class TripDTO implements Serializable {

    private Long id;

    @NotNull
    private Long ownerId;

    @NotNull
    private String startLocation;

    @NotNull
    private String destination;

    @NotNull
    private ZonedDateTime startTime;

    private Boolean canOfferRide;

    private Boolean canBringProduct;

    @NotNull
    private Integer numberOfOfferedSeats;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripDTO)) {
            return false;
        }

        TripDTO tripDTO = (TripDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tripDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripDTO{" +
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
