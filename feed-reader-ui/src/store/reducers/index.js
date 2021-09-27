import { combineReducers } from 'redux';
import currentUser from './userReducer';

const rootReducer = combineReducers({
  currentUser: currentUser,
});

export default rootReducer;
