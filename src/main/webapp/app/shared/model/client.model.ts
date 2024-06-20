import { IExtraUser } from 'app/shared/model/extra-user.model';
import { IReclamation } from 'app/shared/model/reclamation.model';
import { IAppelCommande } from 'app/shared/model/appel-commande.model';

export interface IClient {
  id?: number;
  profession?: string | null;
  extraUser?: IExtraUser | null;
  reclamations?: IReclamation[] | null;
  appelCommandes?: IAppelCommande[] | null;
}

export const defaultValue: Readonly<IClient> = {};
