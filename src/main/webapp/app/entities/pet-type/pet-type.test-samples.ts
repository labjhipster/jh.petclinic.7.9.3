import { IPetType, NewPetType } from './pet-type.model';

export const sampleWithRequiredData: IPetType = {
  id: 27524,
  name: 'withdrawal',
};

export const sampleWithPartialData: IPetType = {
  id: 14351,
  name: 'Directives',
};

export const sampleWithFullData: IPetType = {
  id: 10708,
  name: 'Persistent array Zimbabwe',
};

export const sampleWithNewData: NewPetType = {
  name: 'synthesizing',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
