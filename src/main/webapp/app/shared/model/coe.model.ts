import { IAppelCommande } from 'app/shared/model/appel-commande.model';

export interface ICoe {
  id?: number;
  typeVoiture?: string | null;
  poidsVoiture?: number | null;
  vitesseVoiture?: number | null;
  appelCommande?: IAppelCommande | null;
}

export const defaultValue: Readonly<ICoe> = {};
