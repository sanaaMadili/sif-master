import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EtatStock from './etat-stock';
import EtatStockDetail from './etat-stock-detail';
import EtatStockUpdate from './etat-stock-update';
import EtatStockDeleteDialog from './etat-stock-delete-dialog';

const EtatStockRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EtatStock />} />
    <Route path="new" element={<EtatStockUpdate />} />
    <Route path=":id">
      <Route index element={<EtatStockDetail />} />
      <Route path="edit" element={<EtatStockUpdate />} />
      <Route path="delete" element={<EtatStockDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EtatStockRoutes;
