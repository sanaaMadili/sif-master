import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IExtraUser {
  id?: number;
  cin?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  numeroTelephone?: number | null;
  dateNaissance?: string | null;
  adresse?: string | null;
  pays?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IExtraUser> = {};
