import { combineReducers } from 'redux';

import user from './user';
import product from './product';

const rootReducer = combineReducers({
  user,
  product,
});

export default rootReducer;
