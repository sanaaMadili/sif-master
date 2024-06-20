import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import LigneCommande from './ligne-commande';
import LigneCommandeDetail from './ligne-commande-detail';
import LigneCommandeUpdate from './ligne-commande-update';
import LigneCommandeDeleteDialog from './ligne-commande-delete-dialog';

const LigneCommandeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<LigneCommande />} />
    <Route path="new" element={<LigneCommandeUpdate />} />
    <Route path=":id">
      <Route index element={<LigneCommandeDetail />} />
      <Route path="edit" element={<LigneCommandeUpdate />} />
      <Route path="delete" element={<LigneCommandeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LigneCommandeRoutes;
