import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Coe from './coe';
import CoeDetail from './coe-detail';
import CoeUpdate from './coe-update';
import CoeDeleteDialog from './coe-delete-dialog';

const CoeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Coe />} />
    <Route path="new" element={<CoeUpdate />} />
    <Route path=":id">
      <Route index element={<CoeDetail />} />
      <Route path="edit" element={<CoeUpdate />} />
      <Route path="delete" element={<CoeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CoeRoutes;
