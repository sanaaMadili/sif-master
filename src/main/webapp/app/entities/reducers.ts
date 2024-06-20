import appelCommande from 'app/entities/appel-commande/appel-commande.reducer';
import clr from 'app/entities/clr/clr.reducer';
import coe from 'app/entities/coe/coe.reducer';
import ligneCommande from 'app/entities/ligne-commande/ligne-commande.reducer';
import article from 'app/entities/article/article.reducer';
import magasin from 'app/entities/magasin/magasin.reducer';
import reception from 'app/entities/reception/reception.reducer';
import extraUser from 'app/entities/extra-user/extra-user.reducer';
import client from 'app/entities/client/client.reducer';
import magasinier from 'app/entities/magasinier/magasinier.reducer';
import etatStock from 'app/entities/etat-stock/etat-stock.reducer';
import reclamation from 'app/entities/reclamation/reclamation.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  appelCommande,
  clr,
  coe,
  ligneCommande,
  article,
  magasin,
  reception,
  extraUser,
  client,
  magasinier,
  etatStock,
  reclamation,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
