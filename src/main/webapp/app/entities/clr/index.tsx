import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Clr from './clr';
import ClrDetail from './clr-detail';
import ClrUpdate from './clr-update';
import ClrDeleteDialog from './clr-delete-dialog';

const ClrRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Clr />} />
    <Route path="new" element={<ClrUpdate />} />
    <Route path=":id">
      <Route index element={<ClrDetail />} />
      <Route path="edit" element={<ClrUpdate />} />
      <Route path="delete" element={<ClrDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ClrRoutes;
