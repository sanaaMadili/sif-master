import { IMagasinier } from 'app/shared/model/magasinier.model';
import { IEtatStock } from 'app/shared/model/etat-stock.model';

export interface IMagasin {
  id?: number;
  codeMagasin?: number | null;
  pays?: string | null;
  address?: string | null;
  magasinier?: IMagasinier | null;
  etatStocks?: IEtatStock[] | null;
}

export const defaultValue: Readonly<IMagasin> = {};
