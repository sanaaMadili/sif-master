import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Magasinier from './magasinier';
import MagasinierDetail from './magasinier-detail';
import MagasinierUpdate from './magasinier-update';
import MagasinierDeleteDialog from './magasinier-delete-dialog';

const MagasinierRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Magasinier />} />
    <Route path="new" element={<MagasinierUpdate />} />
    <Route path=":id">
      <Route index element={<MagasinierDetail />} />
      <Route path="edit" element={<MagasinierUpdate />} />
      <Route path="delete" element={<MagasinierDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MagasinierRoutes;
