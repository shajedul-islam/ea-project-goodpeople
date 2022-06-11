package com.miu.ea.goodpeople.service.criteria;

import com.miu.ea.goodpeople.domain.enumeration.RequestStatus;
import com.miu.ea.goodpeople.domain.enumeration.RequestType;
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

/**
 * Criteria class for the {@link com.miu.ea.goodpeople.domain.Request} entity. This class is used
 * in {@link com.miu.ea.goodpeople.web.rest.RequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class RequestCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RequestType
     */
    public static class RequestTypeFilter extends Filter<RequestType> {

        public RequestTypeFilter() {}

        public RequestTypeFilter(RequestTypeFilter filter) {
            super(filter);
        }

        @Override
        public RequestTypeFilter copy() {
            return new RequestTypeFilter(this);
        }
    }

    /**
     * Class for filtering RequestStatus
     */
    public static class RequestStatusFilter extends Filter<RequestStatus> {

        public RequestStatusFilter() {}

        public RequestStatusFilter(RequestStatusFilter filter) {
            super(filter);
        }

        @Override
        public RequestStatusFilter copy() {
            return new RequestStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private RequestTypeFilter requestType;

    private StringFilter startLocation;

    private StringFilter destination;

    private IntegerFilter numberOfSeatsRequested;

    private StringFilter product;

    private StringFilter deliveryLocation;

    private RequestStatusFilter status;

    private LongFilter requesterId;

    private LongFilter tripId;

    private Boolean distinct;

    public RequestCriteria() {}

    public RequestCriteria(RequestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.requestType = other.requestType == null ? null : other.requestType.copy();
        this.startLocation = other.startLocation == null ? null : other.startLocation.copy();
        this.destination = other.destination == null ? null : other.destination.copy();
        this.numberOfSeatsRequested = other.numberOfSeatsRequested == null ? null : other.numberOfSeatsRequested.copy();
        this.product = other.product == null ? null : other.product.copy();
        this.deliveryLocation = other.deliveryLocation == null ? null : other.deliveryLocation.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.requesterId = other.requesterId == null ? null : other.requesterId.copy();
        this.tripId = other.tripId == null ? null : other.tripId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RequestCriteria copy() {
        return new RequestCriteria(this);
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

    public RequestTypeFilter getRequestType() {
        return requestType;
    }

    public RequestTypeFilter requestType() {
        if (requestType == null) {
            requestType = new RequestTypeFilter();
        }
        return requestType;
    }

    public void setRequestType(RequestTypeFilter requestType) {
        this.requestType = requestType;
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

    public IntegerFilter getNumberOfSeatsRequested() {
        return numberOfSeatsRequested;
    }

    public IntegerFilter numberOfSeatsRequested() {
        if (numberOfSeatsRequested == null) {
            numberOfSeatsRequested = new IntegerFilter();
        }
        return numberOfSeatsRequested;
    }

    public void setNumberOfSeatsRequested(IntegerFilter numberOfSeatsRequested) {
        this.numberOfSeatsRequested = numberOfSeatsRequested;
    }

    public StringFilter getProduct() {
        return product;
    }

    public StringFilter product() {
        if (product == null) {
            product = new StringFilter();
        }
        return product;
    }

    public void setProduct(StringFilter product) {
        this.product = product;
    }

    public StringFilter getDeliveryLocation() {
        return deliveryLocation;
    }

    public StringFilter deliveryLocation() {
        if (deliveryLocation == null) {
            deliveryLocation = new StringFilter();
        }
        return deliveryLocation;
    }

    public void setDeliveryLocation(StringFilter deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public RequestStatusFilter getStatus() {
        return status;
    }

    public RequestStatusFilter status() {
        if (status == null) {
            status = new RequestStatusFilter();
        }
        return status;
    }

    public void setStatus(RequestStatusFilter status) {
        this.status = status;
    }

    public LongFilter getRequesterId() {
        return requesterId;
    }

    public LongFilter requesterId() {
        if (requesterId == null) {
            requesterId = new LongFilter();
        }
        return requesterId;
    }

    public void setRequesterId(LongFilter requesterId) {
        this.requesterId = requesterId;
    }

    public LongFilter getTripId() {
        return tripId;
    }

    public LongFilter tripId() {
        if (tripId == null) {
            tripId = new LongFilter();
        }
        return tripId;
    }

    public void setTripId(LongFilter tripId) {
        this.tripId = tripId;
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
        final RequestCriteria that = (RequestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(requestType, that.requestType) &&
            Objects.equals(startLocation, that.startLocation) &&
            Objects.equals(destination, that.destination) &&
            Objects.equals(numberOfSeatsRequested, that.numberOfSeatsRequested) &&
            Objects.equals(product, that.product) &&
            Objects.equals(deliveryLocation, that.deliveryLocation) &&
            Objects.equals(status, that.status) &&
            Objects.equals(requesterId, that.requesterId) &&
            Objects.equals(tripId, that.tripId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            requestType,
            startLocation,
            destination,
            numberOfSeatsRequested,
            product,
            deliveryLocation,
            status,
            requesterId,
            tripId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (requestType != null ? "requestType=" + requestType + ", " : "") +
            (startLocation != null ? "startLocation=" + startLocation + ", " : "") +
            (destination != null ? "destination=" + destination + ", " : "") +
            (numberOfSeatsRequested != null ? "numberOfSeatsRequested=" + numberOfSeatsRequested + ", " : "") +
            (product != null ? "product=" + product + ", " : "") +
            (deliveryLocation != null ? "deliveryLocation=" + deliveryLocation + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (requesterId != null ? "requesterId=" + requesterId + ", " : "") +
            (tripId != null ? "tripId=" + tripId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
