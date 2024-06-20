import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AppelCommande from './appel-commande';
import Clr from './clr';
import Coe from './coe';
import LigneCommande from './ligne-commande';
import Article from './article';
import Magasin from './magasin';
import Reception from './reception';
import ExtraUser from './extra-user';
import Client from './client';
import Magasinier from './magasinier';
import EtatStock from './etat-stock';
import Reclamation from './reclamation';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="appel-commande/*" element={<AppelCommande />} />
        <Route path="clr/*" element={<Clr />} />
        <Route path="coe/*" element={<Coe />} />
        <Route path="ligne-commande/*" element={<LigneCommande />} />
        <Route path="article/*" element={<Article />} />
        <Route path="magasin/*" element={<Magasin />} />
        <Route path="reception/*" element={<Reception />} />
        <Route path="extra-user/*" element={<ExtraUser />} />
        <Route path="client/*" element={<Client />} />
        <Route path="magasinier/*" element={<Magasinier />} />
        <Route path="etat-stock/*" element={<EtatStock />} />
        <Route path="reclamation/*" element={<Reclamation />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
