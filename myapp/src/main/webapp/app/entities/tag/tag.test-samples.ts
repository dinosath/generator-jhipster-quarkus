import { ITag, NewTag } from './tag.model';

export const sampleWithRequiredData: ITag = {
  id: 23452,
  name: 'yuck',
};

export const sampleWithPartialData: ITag = {
  id: 20454,
  name: 'traumatize',
};

export const sampleWithFullData: ITag = {
  id: 6329,
  name: 'incidentally incandescence',
};

export const sampleWithNewData: NewTag = {
  name: 'fortunately',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
