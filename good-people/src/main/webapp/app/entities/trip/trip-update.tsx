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
import { getEntity, updateEntity, createEntity, reset } from './trip.reducer';

export const TripUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const tripEntity = useAppSelector(state => state.trip.entity);
  const loading = useAppSelector(state => state.trip.loading);
  const updating = useAppSelector(state => state.trip.updating);
  const updateSuccess = useAppSelector(state => state.trip.updateSuccess);
  const handleClose = () => {
    props.history.push('/trip' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.startTime = convertDateTimeToServer(values.startTime);

    const entity = {
      ...tripEntity,
      ...values,
      owner: users.find(it => it.id.toString() === values.owner.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          startTime: displayDefaultDateTime(),
        }
      : {
          ...tripEntity,
          startTime: convertDateTimeFromServer(tripEntity.startTime),
          owner: tripEntity?.owner?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="goodpeopleApp.trip.home.createOrEditLabel" data-cy="TripCreateUpdateHeading">
            <Translate contentKey="goodpeopleApp.trip.home.createOrEditLabel">Create or edit a Trip</Translate>
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
                  id="trip-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('goodpeopleApp.trip.startLocation')}
                id="trip-startLocation"
                name="startLocation"
                data-cy="startLocation"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                }}
              />
              <ValidatedField
                label={translate('goodpeopleApp.trip.destination')}
                id="trip-destination"
                name="destination"
                data-cy="destination"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                }}
              />
              <ValidatedField
                label={translate('goodpeopleApp.trip.startTime')}
                id="trip-startTime"
                name="startTime"
                data-cy="startTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('goodpeopleApp.trip.canOfferRide')}
                id="trip-canOfferRide"
                name="canOfferRide"
                data-cy="canOfferRide"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('goodpeopleApp.trip.canBringProduct')}
                id="trip-canBringProduct"
                name="canBringProduct"
                data-cy="canBringProduct"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('goodpeopleApp.trip.numberOfSeatsOffered')}
                id="trip-numberOfSeatsOffered"
                name="numberOfSeatsOffered"
                data-cy="numberOfSeatsOffered"
                type="text"
              />
              <ValidatedField
                label={translate('goodpeopleApp.trip.numberOfSeatsRemaining')}
                id="trip-numberOfSeatsRemaining"
                name="numberOfSeatsRemaining"
                data-cy="numberOfSeatsRemaining"
                type="text"
              />
              <ValidatedField id="trip-owner" name="owner" data-cy="owner" label={translate('goodpeopleApp.trip.owner')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/trip" replace color="info">
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

export default TripUpdate;
