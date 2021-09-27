import { LOG_IN, LOG_OUT, SET_SUBSCRIPTION } from './userActionsTypes';

const setUsername = (username, subscribed) => ({
  type: LOG_IN,
  payload: { username, subscribed },
});

const logOut = () => ({
  type: LOG_OUT,
});

const setSubscribed = (subscribed) => ({
  type: SET_SUBSCRIPTION,
  payload: { subscribed },
});

export default {
  setUsername,
  logOut,
  setSubscribed,
};
