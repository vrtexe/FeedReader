import { UPDATE_ARTICLES } from './articleActions';

const updateArticles = (toUpdate) => ({
  type: UPDATE_ARTICLES,
  payload: { toUpdate },
});

const articleActions = {
  updateArticles,
};

export default articleActions;
