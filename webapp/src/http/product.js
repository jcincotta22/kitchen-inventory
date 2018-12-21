import { put } from 'redux-saga/effects';
import { urls, actionTypes as types } from '../constants';

export function* fetchProduct({ payload }) {
  const response = yield fetch(`${urls.GET_PRODUCT}/${payload.productId}`).then(response => response.json());
  yield put({ type: types.PRODUCT_RECEIVED, payload: response });
}

export function* productSearch({ payload }) {
  const postBody = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      field: 'long_name',
      value: payload.searchString,
    }),
  };
  const response = yield fetch(urls.PRODUCT_SEARCH, postBody)
    .then(response => response.json());
  yield put({ type: types.PRODUCT_SEARCH_RESULTS, payload: response });
}
