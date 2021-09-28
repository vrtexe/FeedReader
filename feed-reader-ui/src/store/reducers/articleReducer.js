import { UPDATE_ARTICLES } from '../actions/articleActions';

const initialState = {
  toUpdate: false,
};

const articleUpdate = (state = initialState, action) => {
  switch (action.type) {
    case UPDATE_ARTICLES:
      return {
        ...state,
        toUpdate: action.payload.toUpdate,
      };
    default:
      return state;
  }
};

export default articleUpdate;
