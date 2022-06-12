package com.miu.ea.goodpeople.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
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
    @Size(min = 2)
    @Column(name = "start_location", nullable = false)
    private String startLocation;

    @NotNull
    @Size(min = 2)
    @Column(name = "destination", nullable = false)
    private String destination;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    @Column(name = "can_offer_ride")
    private Boolean canOfferRide;

    @Column(name = "can_bring_product")
    private Boolean canBringProduct;

    @Column(name = "number_of_seats_offered")
    private Integer numberOfSeatsOffered;

    @Column(name = "number_of_seats_remaining")
    private Integer numberOfSeatsRemaining;

    @OneToMany(mappedBy = "trip")
    @JsonIgnoreProperties(value = { "requester", "trip" }, allowSetters = true)
    private Set<Request> requests = new HashSet<>();

    @ManyToOne
    private User owner;

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

    public Integer getNumberOfSeatsOffered() {
        return this.numberOfSeatsOffered;
    }

    public Trip numberOfSeatsOffered(Integer numberOfSeatsOffered) {
        this.setNumberOfSeatsOffered(numberOfSeatsOffered);
        return this;
    }

    public void setNumberOfSeatsOffered(Integer numberOfSeatsOffered) {
        this.numberOfSeatsOffered = numberOfSeatsOffered;
    }

    public Integer getNumberOfSeatsRemaining() {
        return this.numberOfSeatsRemaining;
    }

    public Trip numberOfSeatsRemaining(Integer numberOfSeatsRemaining) {
        this.setNumberOfSeatsRemaining(numberOfSeatsRemaining);
        return this;
    }

    public void setNumberOfSeatsRemaining(Integer numberOfSeatsRemaining) {
        this.numberOfSeatsRemaining = numberOfSeatsRemaining;
    }

    public Set<Request> getRequests() {
        return this.requests;
    }

    public void setRequests(Set<Request> requests) {
        if (this.requests != null) {
            this.requests.forEach(i -> i.setTrip(null));
        }
        if (requests != null) {
            requests.forEach(i -> i.setTrip(this));
        }
        this.requests = requests;
    }

    public Trip requests(Set<Request> requests) {
        this.setRequests(requests);
        return this;
    }

    public Trip addRequest(Request request) {
        this.requests.add(request);
        request.setTrip(this);
        return this;
    }

    public Trip removeRequest(Request request) {
        this.requests.remove(request);
        request.setTrip(null);
        return this;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Trip owner(User user) {
        this.setOwner(user);
        return this;
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
            ", startLocation='" + getStartLocation() + "'" +
            ", destination='" + getDestination() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", canOfferRide='" + getCanOfferRide() + "'" +
            ", canBringProduct='" + getCanBringProduct() + "'" +
            ", numberOfSeatsOffered=" + getNumberOfSeatsOffered() +
            ", numberOfSeatsRemaining=" + getNumberOfSeatsRemaining() +
            "}";
    }
}
