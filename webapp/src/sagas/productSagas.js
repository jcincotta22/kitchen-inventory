import { takeLatest } from 'redux-saga/effects';
import { fetchProduct, productSearch } from '../http/product';
import { actionTypes as types } from '../constants';

export function* actionWatcher() {
  yield takeLatest(types.FETCH_PRODUCT, fetchProduct);
}

export function* productSearchWatcher() {
  yield takeLatest(types.PRODUCT_SEARCH, productSearch);
}
