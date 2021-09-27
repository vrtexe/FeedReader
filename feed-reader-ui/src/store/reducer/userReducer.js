import { UPDATE } from '../actions/userActionsTypes';

const initialState = {
  username: null,
};

const userStore = (state = initialState, action) => {
  switch (action.type) {
    case UPDATE: {
      return action.payload.username;
    }
    default:
      return state;
  }
};

export default userStore;
