import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Reclamation from './reclamation';
import ReclamationDetail from './reclamation-detail';
import ReclamationUpdate from './reclamation-update';
import ReclamationDeleteDialog from './reclamation-delete-dialog';

const ReclamationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Reclamation />} />
    <Route path="new" element={<ReclamationUpdate />} />
    <Route path=":id">
      <Route index element={<ReclamationDetail />} />
      <Route path="edit" element={<ReclamationUpdate />} />
      <Route path="delete" element={<ReclamationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReclamationRoutes;
