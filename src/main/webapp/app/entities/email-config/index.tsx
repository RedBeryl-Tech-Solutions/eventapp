import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EmailConfig from './email-config';
import EmailConfigDetail from './email-config-detail';
import EmailConfigUpdate from './email-config-update';
import EmailConfigDeleteDialog from './email-config-delete-dialog';

const EmailConfigRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EmailConfig />} />
    <Route path="new" element={<EmailConfigUpdate />} />
    <Route path=":id">
      <Route index element={<EmailConfigDetail />} />
      <Route path="edit" element={<EmailConfigUpdate />} />
      <Route path="delete" element={<EmailConfigDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EmailConfigRoutes;
