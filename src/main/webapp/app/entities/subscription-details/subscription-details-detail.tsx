import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './subscription-details.reducer';

export const SubscriptionDetailsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const subscriptionDetailsEntity = useAppSelector(state => state.subscriptionDetails.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subscriptionDetailsDetailsHeading">Subscription Details</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{subscriptionDetailsEntity.id}</dd>
          <dt>
            <span id="subscriptionName">Subscription Name</span>
          </dt>
          <dd>{subscriptionDetailsEntity.subscriptionName}</dd>
          <dt>
            <span id="subscriptionAmount">Subscription Amount</span>
          </dt>
          <dd>{subscriptionDetailsEntity.subscriptionAmount}</dd>
          <dt>
            <span id="taxAmount">Tax Amount</span>
          </dt>
          <dd>{subscriptionDetailsEntity.taxAmount}</dd>
          <dt>
            <span id="totalAmount">Total Amount</span>
          </dt>
          <dd>{subscriptionDetailsEntity.totalAmount}</dd>
          <dt>
            <span id="subscriptionStartDate">Subscription Start Date</span>
          </dt>
          <dd>
            {subscriptionDetailsEntity.subscriptionStartDate ? (
              <TextFormat value={subscriptionDetailsEntity.subscriptionStartDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="subscriptionExpiryDate">Subscription Expiry Date</span>
          </dt>
          <dd>
            {subscriptionDetailsEntity.subscriptionExpiryDate ? (
              <TextFormat value={subscriptionDetailsEntity.subscriptionExpiryDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="additionalComments">Additional Comments</span>
          </dt>
          <dd>{subscriptionDetailsEntity.additionalComments}</dd>
          <dt>
            <span id="notificationBeforeExpiry">Notification Before Expiry</span>
          </dt>
          <dd>{subscriptionDetailsEntity.notificationBeforeExpiry}</dd>
          <dt>
            <span id="notificationMuteFlag">Notification Mute Flag</span>
          </dt>
          <dd>{subscriptionDetailsEntity.notificationMuteFlag ? 'true' : 'false'}</dd>
          <dt>
            <span id="notificationTo">Notification To</span>
          </dt>
          <dd>{subscriptionDetailsEntity.notificationTo}</dd>
          <dt>
            <span id="notificationCc">Notification Cc</span>
          </dt>
          <dd>{subscriptionDetailsEntity.notificationCc}</dd>
          <dt>
            <span id="notificationBcc">Notification Bcc</span>
          </dt>
          <dd>{subscriptionDetailsEntity.notificationBcc}</dd>
          <dt>Category</dt>
          <dd>{subscriptionDetailsEntity.category ? subscriptionDetailsEntity.category.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/subscription-details" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscription-details/${subscriptionDetailsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubscriptionDetailsDetail;
