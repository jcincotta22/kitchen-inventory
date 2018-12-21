import { all, fork } from 'redux-saga/effects';

import * as productSagas from './productSagas';

export default function* rootSaga() {
  yield all([
    ...Object.values(productSagas),
  ].map(fork));
}
