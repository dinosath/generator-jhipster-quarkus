import dayjs from 'dayjs/esm';

import { IPost, NewPost } from './post.model';

export const sampleWithRequiredData: IPost = {
  id: 22188,
  title: 'hmph often injunction',
  content: '../fake-data/blob/hipster.txt',
  date: dayjs('2024-07-31T06:32'),
};

export const sampleWithPartialData: IPost = {
  id: 27209,
  title: 'save indeed',
  content: '../fake-data/blob/hipster.txt',
  date: dayjs('2024-07-30T19:34'),
};

export const sampleWithFullData: IPost = {
  id: 17852,
  title: 'sour visit',
  content: '../fake-data/blob/hipster.txt',
  date: dayjs('2024-07-31T03:52'),
};

export const sampleWithNewData: NewPost = {
  title: 'full ouch likewise',
  content: '../fake-data/blob/hipster.txt',
  date: dayjs('2024-07-30T23:55'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
