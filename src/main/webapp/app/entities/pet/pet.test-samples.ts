import dayjs from 'dayjs/esm';

import { IPet, NewPet } from './pet.model';

export const sampleWithRequiredData: IPet = {
  id: 62498,
  name: 'Legacy',
};

export const sampleWithPartialData: IPet = {
  id: 56955,
  name: 'Pennsylvania online quantifyin',
  birthDate: dayjs('2020-06-26'),
};

export const sampleWithFullData: IPet = {
  id: 78891,
  name: 'Pizza Shoes',
  birthDate: dayjs('2020-06-27'),
};

export const sampleWithNewData: NewPet = {
  name: 'EXE Wooden Technician',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
