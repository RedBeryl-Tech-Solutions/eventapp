import dayjs from 'dayjs';
import { ICategory } from 'app/shared/model/category.model';

export interface ISubscriptionDetails {
  id?: number;
  subscriptionName?: string;
  subscriptionAmount?: number;
  taxAmount?: number;
  totalAmount?: number;
  subscriptionStartDate?: dayjs.Dayjs;
  subscriptionExpiryDate?: dayjs.Dayjs;
  additionalComments?: string;
  notificationBeforeExpiry?: number;
  notificationMuteFlag?: boolean;
  notificationTo?: string;
  notificationCc?: string | null;
  notificationBcc?: string | null;
  category?: ICategory | null;
}

export const defaultValue: Readonly<ISubscriptionDetails> = {
  notificationMuteFlag: false,
};
