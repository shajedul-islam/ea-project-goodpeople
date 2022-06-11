import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './trip.reducer';

export const TripDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const tripEntity = useAppSelector(state => state.trip.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tripDetailsHeading">
          <Translate contentKey="goodpeopleApp.trip.detail.title">Trip</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tripEntity.id}</dd>
          <dt>
            <span id="startLocation">
              <Translate contentKey="goodpeopleApp.trip.startLocation">Start Location</Translate>
            </span>
          </dt>
          <dd>{tripEntity.startLocation}</dd>
          <dt>
            <span id="destination">
              <Translate contentKey="goodpeopleApp.trip.destination">Destination</Translate>
            </span>
          </dt>
          <dd>{tripEntity.destination}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="goodpeopleApp.trip.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>{tripEntity.startTime ? <TextFormat value={tripEntity.startTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="canOfferRide">
              <Translate contentKey="goodpeopleApp.trip.canOfferRide">Can Offer Ride</Translate>
            </span>
          </dt>
          <dd>{tripEntity.canOfferRide ? 'true' : 'false'}</dd>
          <dt>
            <span id="canBringProduct">
              <Translate contentKey="goodpeopleApp.trip.canBringProduct">Can Bring Product</Translate>
            </span>
          </dt>
          <dd>{tripEntity.canBringProduct ? 'true' : 'false'}</dd>
          <dt>
            <span id="numberOfSeatsOffered">
              <Translate contentKey="goodpeopleApp.trip.numberOfSeatsOffered">Number Of Seats Offered</Translate>
            </span>
          </dt>
          <dd>{tripEntity.numberOfSeatsOffered}</dd>
          <dt>
            <span id="numberOfSeatsRemaining">
              <Translate contentKey="goodpeopleApp.trip.numberOfSeatsRemaining">Number Of Seats Remaining</Translate>
            </span>
          </dt>
          <dd>{tripEntity.numberOfSeatsRemaining}</dd>
          <dt>
            <Translate contentKey="goodpeopleApp.trip.owner">Owner</Translate>
          </dt>
          <dd>{tripEntity.owner ? tripEntity.owner.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/trip" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trip/${tripEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TripDetail;
