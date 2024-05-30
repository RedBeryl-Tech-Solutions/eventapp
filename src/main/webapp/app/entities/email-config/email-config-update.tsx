import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEmailConfig } from 'app/shared/model/email-config.model';
import { getEntity, updateEntity, createEntity, reset } from './email-config.reducer';

export const EmailConfigUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const emailConfigEntity = useAppSelector(state => state.emailConfig.entity);
  const loading = useAppSelector(state => state.emailConfig.loading);
  const updating = useAppSelector(state => state.emailConfig.updating);
  const updateSuccess = useAppSelector(state => state.emailConfig.updateSuccess);

  const handleClose = () => {
    navigate('/email-config');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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

    const entity = {
      ...emailConfigEntity,
      ...values,
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
          ...emailConfigEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="eventappApp.emailConfig.home.createOrEditLabel" data-cy="EmailConfigCreateUpdateHeading">
            Create or edit a Email Config
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="email-config-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Email Id" id="email-config-emailId" name="emailId" data-cy="emailId" type="text" />
              <ValidatedField label="Token String" id="email-config-tokenString" name="tokenString" data-cy="tokenString" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/email-config" replace color="info">
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

export default EmailConfigUpdate;
