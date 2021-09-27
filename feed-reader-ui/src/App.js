import { BrowserRouter as Router, Switch, Route, Link } from 'react-router-dom';
import FeedSourcePage from './components/pages/FeedSourcesPage';
import ListArticlesPage from './components/pages/ListArticlesPage';
import UserProfilePage from './components/pages/ProfilePage';
import Header from './layout/Header';
import './style/css/App.css';

export default function App() {
  return (
    <Router>
      <Header />
      <Switch>
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
