import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICategory } from 'app/shared/model/category.model';
import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { ISubscriptionDetails } from 'app/shared/model/subscription-details.model';
import { getEntity, updateEntity, createEntity, reset } from './subscription-details.reducer';

export const SubscriptionDetailsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const categories = useAppSelector(state => state.category.entities);
  const subscriptionDetailsEntity = useAppSelector(state => state.subscriptionDetails.entity);
  const loading = useAppSelector(state => state.subscriptionDetails.loading);
  const updating = useAppSelector(state => state.subscriptionDetails.updating);
  const updateSuccess = useAppSelector(state => state.subscriptionDetails.updateSuccess);

  const handleClose = () => {
    navigate('/subscription-details');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCategories({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.subscriptionAmount !== undefined && typeof values.subscriptionAmount !== 'number') {
      values.subscriptionAmount = Number(values.subscriptionAmount);
    }
    if (values.taxAmount !== undefined && typeof values.taxAmount !== 'number') {
      values.taxAmount = Number(values.taxAmount);
    }
    if (values.totalAmount !== undefined && typeof values.totalAmount !== 'number') {
      values.totalAmount = Number(values.totalAmount);
    }
    if (values.notificationBeforeExpiry !== undefined && typeof values.notificationBeforeExpiry !== 'number') {
      values.notificationBeforeExpiry = Number(values.notificationBeforeExpiry);
    }

    const entity = {
      ...subscriptionDetailsEntity,
      ...values,
      category: categories.find(it => it.id.toString() === values.category?.toString()),
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
          ...subscriptionDetailsEntity,
          category: subscriptionDetailsEntity?.category?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="eventappApp.subscriptionDetails.home.createOrEditLabel" data-cy="SubscriptionDetailsCreateUpdateHeading">
            Create or edit a Subscription Details
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
                <ValidatedField name="id" required readOnly id="subscription-details-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Subscription Name"
                id="subscription-details-subscriptionName"
                name="subscriptionName"
                data-cy="subscriptionName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Subscription Amount"
                id="subscription-details-subscriptionAmount"
                name="subscriptionAmount"
                data-cy="subscriptionAmount"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Tax Amount"
                id="subscription-details-taxAmount"
                name="taxAmount"
                data-cy="taxAmount"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Total Amount"
                id="subscription-details-totalAmount"
                name="totalAmount"
                data-cy="totalAmount"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Subscription Start Date"
                id="subscription-details-subscriptionStartDate"
                name="subscriptionStartDate"
                data-cy="subscriptionStartDate"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Subscription Expiry Date"
                id="subscription-details-subscriptionExpiryDate"
                name="subscriptionExpiryDate"
                data-cy="subscriptionExpiryDate"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Additional Comments"
                id="subscription-details-additionalComments"
                name="additionalComments"
                data-cy="additionalComments"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Notification Before Expiry"
                id="subscription-details-notificationBeforeExpiry"
                name="notificationBeforeExpiry"
                data-cy="notificationBeforeExpiry"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Notification Mute Flag"
                id="subscription-details-notificationMuteFlag"
                name="notificationMuteFlag"
                data-cy="notificationMuteFlag"
                check
                type="checkbox"
              />
              <ValidatedField
                label="Notification To"
                id="subscription-details-notificationTo"
                name="notificationTo"
                data-cy="notificationTo"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Notification Cc"
                id="subscription-details-notificationCc"
                name="notificationCc"
                data-cy="notificationCc"
                type="text"
              />
              <ValidatedField
                label="Notification Bcc"
                id="subscription-details-notificationBcc"
                name="notificationBcc"
                data-cy="notificationBcc"
                type="text"
              />
              <ValidatedField id="subscription-details-category" name="category" data-cy="category" label="Category" type="select">
                <option value="" key="0" />
                {categories
                  ? categories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/subscription-details" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default SubscriptionDetailsUpdate;
