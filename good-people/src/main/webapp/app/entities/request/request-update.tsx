import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ITrip } from 'app/shared/model/trip.model';
import { getEntities as getTrips } from 'app/entities/trip/trip.reducer';
import { IRequest } from 'app/shared/model/request.model';
import { RequestType } from 'app/shared/model/enumerations/request-type.model';
import { RequestStatus } from 'app/shared/model/enumerations/request-status.model';
import { getEntity, updateEntity, createEntity, reset } from './request.reducer';

export const RequestUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const trips = useAppSelector(state => state.trip.entities);
  const requestEntity = useAppSelector(state => state.request.entity);
  const loading = useAppSelector(state => state.request.loading);
  const updating = useAppSelector(state => state.request.updating);
  const updateSuccess = useAppSelector(state => state.request.updateSuccess);
  const requestTypeValues = Object.keys(RequestType);
  const requestStatusValues = Object.keys(RequestStatus);
  const handleClose = () => {
    props.history.push('/request' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
    dispatch(getTrips({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...requestEntity,
      ...values,
      requester: users.find(it => it.id.toString() === values.requester.toString()),
      trip: trips.find(it => it.id.toString() === values.trip.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          requestType: 'RIDE',
          status: 'ACCEPTED',
          ...requestEntity,
          requester: requestEntity?.requester?.id,
          trip: requestEntity?.trip?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="goodpeopleApp.request.home.createOrEditLabel" data-cy="RequestCreateUpdateHeading">
            <Translate contentKey="goodpeopleApp.request.home.createOrEditLabel">Create or edit a Request</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="request-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('goodpeopleApp.request.requestType')}
                id="request-requestType"
                name="requestType"
                data-cy="requestType"
                type="select"
              >
                {requestTypeValues.map(requestType => (
                  <option value={requestType} key={requestType}>
                    {translate('goodpeopleApp.RequestType.' + requestType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('goodpeopleApp.request.startLocation')}
                id="request-startLocation"
                name="startLocation"
                data-cy="startLocation"
                type="text"
              />
              <ValidatedField
                label={translate('goodpeopleApp.request.destination')}
                id="request-destination"
                name="destination"
                data-cy="destination"
                type="text"
              />
              <ValidatedField
                label={translate('goodpeopleApp.request.numberOfSeatsRequested')}
                id="request-numberOfSeatsRequested"
                name="numberOfSeatsRequested"
                data-cy="numberOfSeatsRequested"
                type="text"
              />
              <ValidatedField
                label={translate('goodpeopleApp.request.product')}
                id="request-product"
                name="product"
                data-cy="product"
                type="text"
              />
              <ValidatedField
                label={translate('goodpeopleApp.request.deliveryLocation')}
                id="request-deliveryLocation"
                name="deliveryLocation"
                data-cy="deliveryLocation"
                type="text"
              />
              <ValidatedField
                label={translate('goodpeopleApp.request.status')}
                id="request-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {requestStatusValues.map(requestStatus => (
                  <option value={requestStatus} key={requestStatus}>
                    {translate('goodpeopleApp.RequestStatus.' + requestStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="request-requester"
                name="requester"
                data-cy="requester"
                label={translate('goodpeopleApp.request.requester')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="request-trip" name="trip" data-cy="trip" label={translate('goodpeopleApp.request.trip')} type="select">
                <option value="" key="0" />
                {trips
                  ? trips.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/request" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default RequestUpdate;
