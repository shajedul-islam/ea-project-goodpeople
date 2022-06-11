import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IRequest } from 'app/shared/model/request.model';

export interface ITrip {
  id?: number;
  startLocation?: string;
  destination?: string;
  startTime?: string;
  canOfferRide?: boolean | null;
  canBringProduct?: boolean | null;
  numberOfSeatsOffered?: number;
  numberOfSeatsRemaining?: number | null;
  owner?: IUser | null;
  requests?: IRequest[] | null;
}

export const defaultValue: Readonly<ITrip> = {
  canOfferRide: false,
  canBringProduct: false,
};
