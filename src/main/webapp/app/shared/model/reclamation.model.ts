import { IClient } from 'app/shared/model/client.model';

export interface IReclamation {
  id?: number;
  date?: string | null;
  pieceJointeContentType?: string | null;
  pieceJointe?: string | null;
  raison?: string | null;
  client?: IClient | null;
}

export const defaultValue: Readonly<IReclamation> = {};
