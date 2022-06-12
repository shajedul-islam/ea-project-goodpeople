import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './request.reducer';

export const RequestDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const requestEntity = useAppSelector(state => state.request.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="requestDetailsHeading">
          <Translate contentKey="goodpeopleApp.request.detail.title">Request</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{requestEntity.id}</dd>
          <dt>
            <span id="requestType">
              <Translate contentKey="goodpeopleApp.request.requestType">Request Type</Translate>
            </span>
          </dt>
          <dd>{requestEntity.requestType}</dd>
          <dt>
            <span id="startLocation">
              <Translate contentKey="goodpeopleApp.request.startLocation">Start Location</Translate>
            </span>
          </dt>
          <dd>{requestEntity.startLocation}</dd>
          <dt>
            <span id="destination">
              <Translate contentKey="goodpeopleApp.request.destination">Destination</Translate>
            </span>
          </dt>
          <dd>{requestEntity.destination}</dd>
          <dt>
            <span id="numberOfSeatsRequested">
              <Translate contentKey="goodpeopleApp.request.numberOfSeatsRequested">Number Of Seats Requested</Translate>
            </span>
          </dt>
          <dd>{requestEntity.numberOfSeatsRequested}</dd>
          <dt>
            <span id="product">
              <Translate contentKey="goodpeopleApp.request.product">Product</Translate>
            </span>
          </dt>
          <dd>{requestEntity.product}</dd>
          <dt>
            <span id="deliveryLocation">
              <Translate contentKey="goodpeopleApp.request.deliveryLocation">Delivery Location</Translate>
            </span>
          </dt>
          <dd>{requestEntity.deliveryLocation}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="goodpeopleApp.request.status">Status</Translate>
            </span>
          </dt>
          <dd>{requestEntity.status}</dd>
          <dt>
            <Translate contentKey="goodpeopleApp.request.requester">Requester</Translate>
          </dt>
          <dd>{requestEntity.requester ? requestEntity.requester.firstName : ''}</dd>
          <dt>
            <Translate contentKey="goodpeopleApp.request.trip">Trip</Translate>
          </dt>
          <dd>{requestEntity.trip ? requestEntity.trip.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/request" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/request/${requestEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RequestDetail;
