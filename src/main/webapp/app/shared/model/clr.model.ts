import dayjs from 'dayjs';
import { IAppelCommande } from 'app/shared/model/appel-commande.model';

export interface IClr {
  id?: number;
  constructeurAutomobile?: string | null;
  modelVoiture?: string | null;
  anneeVoiture?: number | null;
  etatPneu?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  dateProduction?: string | null;
  appelCommande?: IAppelCommande | null;
}

export const defaultValue: Readonly<IClr> = {};
