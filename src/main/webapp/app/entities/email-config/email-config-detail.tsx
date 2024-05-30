import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './email-config.reducer';

export const EmailConfigDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const emailConfigEntity = useAppSelector(state => state.emailConfig.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="emailConfigDetailsHeading">Email Config</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{emailConfigEntity.id}</dd>
          <dt>
            <span id="emailId">Email Id</span>
          </dt>
          <dd>{emailConfigEntity.emailId}</dd>
          <dt>
            <span id="tokenString">Token String</span>
          </dt>
          <dd>{emailConfigEntity.tokenString}</dd>
        </dl>
        <Button tag={Link} to="/email-config" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/email-config/${emailConfigEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmailConfigDetail;
