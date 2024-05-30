import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './subscription-details.reducer';

export const SubscriptionDetails = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const subscriptionDetailsList = useAppSelector(state => state.subscriptionDetails.entities);
  const loading = useAppSelector(state => state.subscriptionDetails.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="subscription-details-heading" data-cy="SubscriptionDetailsHeading">
        Subscription Details
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link
            to="/subscription-details/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Subscription Details
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {subscriptionDetailsList && subscriptionDetailsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('subscriptionName')}>
                  Subscription Name <FontAwesomeIcon icon={getSortIconByFieldName('subscriptionName')} />
                </th>
                <th className="hand" onClick={sort('subscriptionAmount')}>
                  Subscription Amount <FontAwesomeIcon icon={getSortIconByFieldName('subscriptionAmount')} />
                </th>
                <th className="hand" onClick={sort('taxAmount')}>
                  Tax Amount <FontAwesomeIcon icon={getSortIconByFieldName('taxAmount')} />
                </th>
                <th className="hand" onClick={sort('totalAmount')}>
                  Total Amount <FontAwesomeIcon icon={getSortIconByFieldName('totalAmount')} />
                </th>
                <th className="hand" onClick={sort('subscriptionStartDate')}>
                  Subscription Start Date <FontAwesomeIcon icon={getSortIconByFieldName('subscriptionStartDate')} />
                </th>
                <th className="hand" onClick={sort('subscriptionExpiryDate')}>
                  Subscription Expiry Date <FontAwesomeIcon icon={getSortIconByFieldName('subscriptionExpiryDate')} />
                </th>
                <th className="hand" onClick={sort('additionalComments')}>
                  Additional Comments <FontAwesomeIcon icon={getSortIconByFieldName('additionalComments')} />
                </th>
                <th className="hand" onClick={sort('notificationBeforeExpiry')}>
                  Notification Before Expiry <FontAwesomeIcon icon={getSortIconByFieldName('notificationBeforeExpiry')} />
                </th>
                <th className="hand" onClick={sort('notificationMuteFlag')}>
                  Notification Mute Flag <FontAwesomeIcon icon={getSortIconByFieldName('notificationMuteFlag')} />
                </th>
                <th className="hand" onClick={sort('notificationTo')}>
                  Notification To <FontAwesomeIcon icon={getSortIconByFieldName('notificationTo')} />
                </th>
                <th className="hand" onClick={sort('notificationCc')}>
                  Notification Cc <FontAwesomeIcon icon={getSortIconByFieldName('notificationCc')} />
                </th>
                <th className="hand" onClick={sort('notificationBcc')}>
                  Notification Bcc <FontAwesomeIcon icon={getSortIconByFieldName('notificationBcc')} />
                </th>
                <th>
                  Category <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {subscriptionDetailsList.map((subscriptionDetails, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/subscription-details/${subscriptionDetails.id}`} color="link" size="sm">
                      {subscriptionDetails.id}
                    </Button>
                  </td>
                  <td>{subscriptionDetails.subscriptionName}</td>
                  <td>{subscriptionDetails.subscriptionAmount}</td>
                  <td>{subscriptionDetails.taxAmount}</td>
                  <td>{subscriptionDetails.totalAmount}</td>
                  <td>
                    {subscriptionDetails.subscriptionStartDate ? (
                      <TextFormat type="date" value={subscriptionDetails.subscriptionStartDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {subscriptionDetails.subscriptionExpiryDate ? (
                      <TextFormat type="date" value={subscriptionDetails.subscriptionExpiryDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{subscriptionDetails.additionalComments}</td>
                  <td>{subscriptionDetails.notificationBeforeExpiry}</td>
                  <td>{subscriptionDetails.notificationMuteFlag ? 'true' : 'false'}</td>
                  <td>{subscriptionDetails.notificationTo}</td>
                  <td>{subscriptionDetails.notificationCc}</td>
                  <td>{subscriptionDetails.notificationBcc}</td>
                  <td>
                    {subscriptionDetails.category ? (
                      <Link to={`/category/${subscriptionDetails.category.id}`}>{subscriptionDetails.category.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/subscription-details/${subscriptionDetails.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/subscription-details/${subscriptionDetails.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/subscription-details/${subscriptionDetails.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Subscription Details found</div>
        )}
      </div>
    </div>
  );
};

export default SubscriptionDetails;
