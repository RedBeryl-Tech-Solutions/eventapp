import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EmailConfig from './email-config';
import SubscriptionDetails from './subscription-details';
import Category from './category';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="email-config/*" element={<EmailConfig />} />
        <Route path="subscription-details/*" element={<SubscriptionDetails />} />
        <Route path="category/*" element={<Category />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
