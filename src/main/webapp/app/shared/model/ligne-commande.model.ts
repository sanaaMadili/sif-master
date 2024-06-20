import { IArticle } from 'app/shared/model/article.model';
import { IAppelCommande } from 'app/shared/model/appel-commande.model';

export interface ILigneCommande {
  id?: number;
  qte?: number | null;
  article?: IArticle | null;
  appelCommande?: IAppelCommande | null;
}

export const defaultValue: Readonly<ILigneCommande> = {};
