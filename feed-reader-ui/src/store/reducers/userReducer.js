import { LOG_IN, LOG_OUT, SET_SUBSCRIPTION } from '../actions/userActionsTypes';

const initialState = {
  username: '',
  loggedIn: false,
};

const currentUser = (state = initialState, action) => {
  switch (action.type) {
    case LOG_IN:
      return {
        ...state,
        username: action.payload.username,
        subscribed: action.payload.subscribed,
        loggedIn: true,
      };
    case LOG_OUT:
      return {
        ...state,
        username: '',
        subscribed: false,
        loggedIn: false,
      };
    case SET_SUBSCRIPTION:
      return {
        ...state,
        subscribed: action.payload.subscribed,
      };
    default:
      return state;
  }
};

export default currentUser;
