import { UPDATE } from './userActionsTypes';

export const update = (username) => ({
  type: UPDATE,
  payload: { username },
});
