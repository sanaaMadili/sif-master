import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ExtraUser from './extra-user';
import ExtraUserDetail from './extra-user-detail';
import ExtraUserUpdate from './extra-user-update';
import ExtraUserDeleteDialog from './extra-user-delete-dialog';

const ExtraUserRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ExtraUser />} />
    <Route path="new" element={<ExtraUserUpdate />} />
    <Route path=":id">
      <Route index element={<ExtraUserDetail />} />
      <Route path="edit" element={<ExtraUserUpdate />} />
      <Route path="delete" element={<ExtraUserDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ExtraUserRoutes;
