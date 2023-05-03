import { ISpecialty, NewSpecialty } from './specialty.model';

export const sampleWithRequiredData: ISpecialty = {
  id: 10430,
  name: 'Pants invoice',
};

export const sampleWithPartialData: ISpecialty = {
  id: 96910,
  name: 'brand Shirt',
};

export const sampleWithFullData: ISpecialty = {
  id: 9741,
  name: 'Refined harness',
};

export const sampleWithNewData: NewSpecialty = {
  name: 'Kuwait',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
