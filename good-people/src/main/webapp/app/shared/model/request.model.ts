import { IUser } from 'app/shared/model/user.model';
import { ITrip } from 'app/shared/model/trip.model';
import { RequestType } from 'app/shared/model/enumerations/request-type.model';
import { RequestStatus } from 'app/shared/model/enumerations/request-status.model';

export interface IRequest {
  id?: number;
  requestType?: RequestType;
  startLocation?: string | null;
  destination?: string | null;
  numberOfSeatsRequested?: number | null;
  product?: string | null;
  deliveryLocation?: string | null;
  status?: RequestStatus | null;
  requester?: IUser | null;
  trip?: ITrip | null;
}

export const defaultValue: Readonly<IRequest> = {};
