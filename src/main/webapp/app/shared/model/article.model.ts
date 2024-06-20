import { IEtatStock } from 'app/shared/model/etat-stock.model';
import { ILigneCommande } from 'app/shared/model/ligne-commande.model';

export interface IArticle {
  id?: number;
  cai?: number | null;
  refPneu?: number | null;
  typePneu?: string | null;
  valeur?: number | null;
  imageContentType?: string | null;
  image?: string | null;
  devise?: string | null;
  etatStocks?: IEtatStock[] | null;
  ligneCommandes?: ILigneCommande[] | null;
}

export const defaultValue: Readonly<IArticle> = {};
