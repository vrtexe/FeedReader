import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import './style/css/index.css';
import reportWebVitals from './report/reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Provider } from 'react-redux';
import { PersistGate } from 'redux-persist/integration/react';
import LoadingComponent from './components/interface/LoadingComponent';
import store from './store/store';

/**
 * The index file that initializes the react application,
 * the provider component provides the store to all the descendants,
 * while the persistGate component persists the data in the store after a reload is performed
 */
ReactDOM.render(
  <React.StrictMode>
    <Provider store={store.store}>
      <PersistGate loading={<LoadingComponent />} persistor={store.persistor}>
        <App />
      </PersistGate>
    </Provider>
  </React.StrictMode>,
  document.getElementById('root'),
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
