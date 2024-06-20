import { IArticle } from 'app/shared/model/article.model';
import { IMagasin } from 'app/shared/model/magasin.model';

export interface IEtatStock {
  id?: number;
  qte?: number | null;
  location?: string | null;
  article?: IArticle | null;
  magasin?: IMagasin | null;
}

export const defaultValue: Readonly<IEtatStock> = {};
