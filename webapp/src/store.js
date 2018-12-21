import { createStore, compose, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';
import throttle from 'lodash/throttle';
import createSagaMiddleware from 'redux-saga';
import { createLogger } from 'redux-logger';

import rootReducer from './reducers';
// import { loginWithToken } from './actions';
import { saveState, loadState } from './helpers';
import rootSaga from './sagas/index';

const sagaMiddleware = createSagaMiddleware();


const middleware = [
  thunk,
  sagaMiddleware,
];

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;


if (process.env.NODE_ENV !== 'production') {
  middleware.push(createLogger({
    collapsed: true,
  }));
}


export const store = createStore(
  rootReducer,
  loadState(),
  composeEnhancers(applyMiddleware(...middleware)),
);

sagaMiddleware.run(rootSaga);

store.subscribe(throttle(() => saveState(store.getState()), 1000));
// store.dispatch(loginWithToken());
