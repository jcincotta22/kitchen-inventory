import { combineReducers } from 'redux';
import * as types from '../constants/actionTypes';

const currentProduct = (state = {}, { type, payload }) => {
  switch (type) {
    case types.PRODUCT_RECEIVED:
      return payload;
    default:
      return state;
  }
};

const searchResults = (state = {}, { type, payload }) => {
  switch (type) {
    case types.PRODUCT_SEARCH_RESULTS:
      return payload;
    default:
      return state;
  }
};

const loading = (state = {}, { type }) => {
  switch (type) {
    case types.FETCH_PRODUCT:
    case types.PRODUCT_SEARCH:
      return true;
    case types.PRODUCT_RECEIVED:
    case types.PRODUCT_SEARCH_RESULTS:
      return false;
    default:
      return state;
  }
};

const product = combineReducers({
  loading,
  currentProduct,
  searchResults,
});

export default product;
