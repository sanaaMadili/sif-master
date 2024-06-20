import dayjs from 'dayjs';
import { IReception } from 'app/shared/model/reception.model';
import { IClient } from 'app/shared/model/client.model';
import { ILigneCommande } from 'app/shared/model/ligne-commande.model';

export interface IAppelCommande {
  id?: number;
  numCommande?: number | null;
  dateCommande?: string | null;
  dateLivraison?: string | null;
  dateExpedition?: string | null;
  status?: number | null;
  annomalie?: number | null;
  reception?: IReception | null;
  client?: IClient | null;
  ligneCommandes?: ILigneCommande[] | null;
}

export const defaultValue: Readonly<IAppelCommande> = {};
