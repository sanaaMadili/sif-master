import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Magasin from './magasin';
import MagasinDetail from './magasin-detail';
import MagasinUpdate from './magasin-update';
import MagasinDeleteDialog from './magasin-delete-dialog';

const MagasinRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Magasin />} />
    <Route path="new" element={<MagasinUpdate />} />
    <Route path=":id">
      <Route index element={<MagasinDetail />} />
      <Route path="edit" element={<MagasinUpdate />} />
      <Route path="delete" element={<MagasinDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MagasinRoutes;
