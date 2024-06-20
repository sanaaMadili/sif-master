import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AppelCommande from './appel-commande';
import AppelCommandeDetail from './appel-commande-detail';
import AppelCommandeUpdate from './appel-commande-update';
import AppelCommandeDeleteDialog from './appel-commande-delete-dialog';

const AppelCommandeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AppelCommande />} />
    <Route path="new" element={<AppelCommandeUpdate />} />
    <Route path=":id">
      <Route index element={<AppelCommandeDetail />} />
      <Route path="edit" element={<AppelCommandeUpdate />} />
      <Route path="delete" element={<AppelCommandeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AppelCommandeRoutes;
