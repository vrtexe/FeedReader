import { createStore } from 'redux';
import { persistStore, persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import rootReducer from './reducers';

// export const store = createStore(rootReducer);

const persistConfig = {
  key: 'root',
  storage,
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

const createPersistingStore = () => {
  let store = createStore(persistedReducer);
  let persistor = persistStore(store);
  return {
    store,
    persistor,
  };
};

export default createPersistingStore();
