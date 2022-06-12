import dayjs from 'dayjs';
import { IRequest } from 'app/shared/model/request.model';
import { IUser } from 'app/shared/model/user.model';

export interface ITrip {
  id?: number;
  startLocation?: string;
  destination?: string;
  startTime?: string;
  canOfferRide?: boolean | null;
  canBringProduct?: boolean | null;
  numberOfSeatsOffered?: number | null;
  numberOfSeatsRemaining?: number | null;
  requests?: IRequest[] | null;
  owner?: IUser | null;
}

export const defaultValue: Readonly<ITrip> = {
  canOfferRide: false,
  canBringProduct: false,
};
