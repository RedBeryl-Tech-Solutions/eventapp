import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SubscriptionDetails from './subscription-details';
import SubscriptionDetailsDetail from './subscription-details-detail';
import SubscriptionDetailsUpdate from './subscription-details-update';
import SubscriptionDetailsDeleteDialog from './subscription-details-delete-dialog';

const SubscriptionDetailsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SubscriptionDetails />} />
    <Route path="new" element={<SubscriptionDetailsUpdate />} />
    <Route path=":id">
      <Route index element={<SubscriptionDetailsDetail />} />
      <Route path="edit" element={<SubscriptionDetailsUpdate />} />
      <Route path="delete" element={<SubscriptionDetailsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SubscriptionDetailsRoutes;
