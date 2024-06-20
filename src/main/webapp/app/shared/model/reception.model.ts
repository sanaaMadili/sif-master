import { IAppelCommande } from 'app/shared/model/appel-commande.model';

export interface IReception {
  id?: number;
  pays?: string | null;
  address?: string | null;
  appelCommandes?: IAppelCommande[] | null;
}

export const defaultValue: Readonly<IReception> = {};
