import dayjs from 'dayjs/esm';

import { IVisit, NewVisit } from './visit.model';

export const sampleWithRequiredData: IVisit = {
  id: 31906,
  description: 'multimedia',
};

export const sampleWithPartialData: IVisit = {
  id: 26360,
  visitDate: dayjs('2020-06-27'),
  description: 'viral',
};

export const sampleWithFullData: IVisit = {
  id: 90847,
  visitDate: dayjs('2020-06-26'),
  description: 'Checking Rupiah Administrator',
};

export const sampleWithNewData: NewVisit = {
  description: 'optical Heights',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
