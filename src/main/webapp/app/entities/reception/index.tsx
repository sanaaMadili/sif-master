import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Reception from './reception';
import ReceptionDetail from './reception-detail';
import ReceptionUpdate from './reception-update';
import ReceptionDeleteDialog from './reception-delete-dialog';

const ReceptionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Reception />} />
    <Route path="new" element={<ReceptionUpdate />} />
    <Route path=":id">
      <Route index element={<ReceptionDetail />} />
      <Route path="edit" element={<ReceptionUpdate />} />
      <Route path="delete" element={<ReceptionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReceptionRoutes;
