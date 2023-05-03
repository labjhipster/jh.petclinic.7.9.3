import { IOwner, NewOwner } from './owner.model';

export const sampleWithRequiredData: IOwner = {
  id: 70414,
  firstName: 'Manley',
  lastName: 'Gerhold',
  address: 'Buckinghamshire Markets',
  city: 'Fayetteville',
  telephone: '(248) 448-1000 x6025',
};

export const sampleWithPartialData: IOwner = {
  id: 31169,
  firstName: 'Kurtis',
  lastName: 'Tremblay',
  address: 'ADP Integrated Refined',
  city: 'Longview',
  telephone: '1-323-403-7486',
};

export const sampleWithFullData: IOwner = {
  id: 11291,
  firstName: 'Javonte',
  lastName: 'Kiehn',
  address: 'invoice Computers',
  city: 'Kubton',
  telephone: '(850) 966-9531',
};

export const sampleWithNewData: NewOwner = {
  firstName: 'Matilde',
  lastName: 'Lang',
  address: 'indexing Reactive back',
  city: 'South Cyril',
  telephone: '1-200-879-7479 x8571',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
