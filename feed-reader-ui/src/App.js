import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import { FeedSourceProfile } from './components/pages/FeedSourceProfile';
import FeedSourcePage from './components/pages/FeedSourcesPage';
import ListArticlesPage from './components/pages/ListArticlesPage';
import UserProfilePage from './components/pages/ProfilePage';
import { Header } from './layout/Header';
import './style/css/App.css';

/**
 * The app component is the main component of the application
 * consisting of the router, all the different pages and the header.
 */
export default function App() {
  return (
    <Router>
      <Header />
      <Switch>
        <Route exact path="/sources/:id">
          <FeedSourceProfile />
        </Route>
        <Route exact path="/sources">
          <FeedSourcePage />
        </Route>
        <Route exact path="/profile">
          <UserProfilePage />
        </Route>
        <Route exact path="/">
          <ListArticlesPage />
        </Route>
      </Switch>
    </Router>
  );
}
