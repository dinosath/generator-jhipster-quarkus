import { IBlog, NewBlog } from './blog.model';

export const sampleWithRequiredData: IBlog = {
  id: 29088,
  name: 'meanwhile',
  handle: 'elegantly quizzically',
};

export const sampleWithPartialData: IBlog = {
  id: 6037,
  name: 'uh-huh ack frigid',
  handle: 'haunt',
};

export const sampleWithFullData: IBlog = {
  id: 14747,
  name: 'these during',
  handle: 'mustache so sediment',
};

export const sampleWithNewData: NewBlog = {
  name: 'dioxide sadly',
  handle: 'underneath yippee bulky',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
