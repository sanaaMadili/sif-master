import { IExtraUser } from 'app/shared/model/extra-user.model';
import { IMagasin } from 'app/shared/model/magasin.model';

export interface IMagasinier {
  id?: number;
  profession?: string | null;
  extraUser?: IExtraUser | null;
  magasin?: IMagasin | null;
}

export const defaultValue: Readonly<IMagasinier> = {};
