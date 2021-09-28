import { combineReducers } from 'redux';
import currentUser from './userReducer';
import articleUpdate from './articleReducer';

const rootReducer = combineReducers({
  currentUser: currentUser,
  articleUpdate: articleUpdate,
});

export default rootReducer;
