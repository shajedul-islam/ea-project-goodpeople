package com.miu.ea.goodpeople.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.miu.ea.goodpeople.domain.Trip} entity. This class is used
 * in {@link com.miu.ea.goodpeople.web.rest.TripResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /trips?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class TripCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter startLocation;

    private StringFilter destination;

    private ZonedDateTimeFilter startTime;

    private BooleanFilter canOfferRide;

    private BooleanFilter canBringProduct;

    private IntegerFilter numberOfSeatsOffered;

    private IntegerFilter numberOfSeatsRemaining;

    private LongFilter ownerId;

    private LongFilter requestId;

    private Boolean distinct;

    public TripCriteria() {}

    public TripCriteria(TripCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.startLocation = other.startLocation == null ? null : other.startLocation.copy();
        this.destination = other.destination == null ? null : other.destination.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.canOfferRide = other.canOfferRide == null ? null : other.canOfferRide.copy();
        this.canBringProduct = other.canBringProduct == null ? null : other.canBringProduct.copy();
        this.numberOfSeatsOffered = other.numberOfSeatsOffered == null ? null : other.numberOfSeatsOffered.copy();
        this.numberOfSeatsRemaining = other.numberOfSeatsRemaining == null ? null : other.numberOfSeatsRemaining.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
        this.requestId = other.requestId == null ? null : other.requestId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TripCriteria copy() {
        return new TripCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getStartLocation() {
        return startLocation;
    }

    public StringFilter startLocation() {
        if (startLocation == null) {
            startLocation = new StringFilter();
        }
        return startLocation;
    }

    public void setStartLocation(StringFilter startLocation) {
        this.startLocation = startLocation;
    }

    public StringFilter getDestination() {
        return destination;
    }

    public StringFilter destination() {
        if (destination == null) {
            destination = new StringFilter();
        }
        return destination;
    }

    public void setDestination(StringFilter destination) {
        this.destination = destination;
    }

    public ZonedDateTimeFilter getStartTime() {
        return startTime;
    }

    public ZonedDateTimeFilter startTime() {
        if (startTime == null) {
            startTime = new ZonedDateTimeFilter();
        }
        return startTime;
    }

    public void setStartTime(ZonedDateTimeFilter startTime) {
        this.startTime = startTime;
    }

    public BooleanFilter getCanOfferRide() {
        return canOfferRide;
    }

    public BooleanFilter canOfferRide() {
        if (canOfferRide == null) {
            canOfferRide = new BooleanFilter();
        }
        return canOfferRide;
    }

    public void setCanOfferRide(BooleanFilter canOfferRide) {
        this.canOfferRide = canOfferRide;
    }

    public BooleanFilter getCanBringProduct() {
        return canBringProduct;
    }

    public BooleanFilter canBringProduct() {
        if (canBringProduct == null) {
            canBringProduct = new BooleanFilter();
        }
        return canBringProduct;
    }

    public void setCanBringProduct(BooleanFilter canBringProduct) {
        this.canBringProduct = canBringProduct;
    }

    public IntegerFilter getNumberOfSeatsOffered() {
        return numberOfSeatsOffered;
    }

    public IntegerFilter numberOfSeatsOffered() {
        if (numberOfSeatsOffered == null) {
            numberOfSeatsOffered = new IntegerFilter();
        }
        return numberOfSeatsOffered;
    }

    public void setNumberOfSeatsOffered(IntegerFilter numberOfSeatsOffered) {
        this.numberOfSeatsOffered = numberOfSeatsOffered;
    }

    public IntegerFilter getNumberOfSeatsRemaining() {
        return numberOfSeatsRemaining;
    }

    public IntegerFilter numberOfSeatsRemaining() {
        if (numberOfSeatsRemaining == null) {
            numberOfSeatsRemaining = new IntegerFilter();
        }
        return numberOfSeatsRemaining;
    }

    public void setNumberOfSeatsRemaining(IntegerFilter numberOfSeatsRemaining) {
        this.numberOfSeatsRemaining = numberOfSeatsRemaining;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public LongFilter ownerId() {
        if (ownerId == null) {
            ownerId = new LongFilter();
        }
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public LongFilter getRequestId() {
        return requestId;
    }

    public LongFilter requestId() {
        if (requestId == null) {
            requestId = new LongFilter();
        }
        return requestId;
    }

    public void setRequestId(LongFilter requestId) {
        this.requestId = requestId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TripCriteria that = (TripCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startLocation, that.startLocation) &&
            Objects.equals(destination, that.destination) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(canOfferRide, that.canOfferRide) &&
            Objects.equals(canBringProduct, that.canBringProduct) &&
            Objects.equals(numberOfSeatsOffered, that.numberOfSeatsOffered) &&
            Objects.equals(numberOfSeatsRemaining, that.numberOfSeatsRemaining) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(requestId, that.requestId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            startLocation,
            destination,
            startTime,
            canOfferRide,
            canBringProduct,
            numberOfSeatsOffered,
            numberOfSeatsRemaining,
            ownerId,
            requestId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (startLocation != null ? "startLocation=" + startLocation + ", " : "") +
            (destination != null ? "destination=" + destination + ", " : "") +
            (startTime != null ? "startTime=" + startTime + ", " : "") +
            (canOfferRide != null ? "canOfferRide=" + canOfferRide + ", " : "") +
            (canBringProduct != null ? "canBringProduct=" + canBringProduct + ", " : "") +
            (numberOfSeatsOffered != null ? "numberOfSeatsOffered=" + numberOfSeatsOffered + ", " : "") +
            (numberOfSeatsRemaining != null ? "numberOfSeatsRemaining=" + numberOfSeatsRemaining + ", " : "") +
            (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            (requestId != null ? "requestId=" + requestId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
