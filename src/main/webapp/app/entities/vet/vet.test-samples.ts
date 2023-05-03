import { IVet, NewVet } from './vet.model';

export const sampleWithRequiredData: IVet = {
  id: 99986,
  firstName: 'Addison',
  lastName: 'Lowe',
};

export const sampleWithPartialData: IVet = {
  id: 91083,
  firstName: 'Curt',
  lastName: 'Goldner',
};

export const sampleWithFullData: IVet = {
  id: 64235,
  firstName: 'Shaylee',
  lastName: 'Wyman',
};

export const sampleWithNewData: NewVet = {
  firstName: 'Ceasar',
  lastName: 'Frami',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
