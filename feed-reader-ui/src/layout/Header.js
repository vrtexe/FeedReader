import { useState } from 'react';
import { Container, Navbar, Nav, NavDropdown } from 'react-bootstrap';
import Loader from 'react-loader-spinner';
import { useDispatch, useSelector } from 'react-redux';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSyncAlt, faCog, faUser } from '@fortawesome/free-solid-svg-icons';
import { LinkContainer } from 'react-router-bootstrap';
import { SettingsModal } from '../components/modal/SettingsModal';
import UserLoginRegisterModal from '../components/modal/UserLoginRegisterModal';
import allActions from '../store/actions';

/**
 * The header component consists of the navigation links,
 * and two modal boxes, one for setting up the feed sources,
 * and the other for the authentication.
 */
export const Header = () => {
  const [showSettings, setShowSettings] = useState(false);
  const [showLogin, setShowLogin] = useState(false);
  const [loading, setLoading] = useState(false);
  const user = useSelector((state) => state.currentUser);
  const updateButton = useSelector((state) => state.articleUpdate);

  const dispatch = useDispatch();

  const stopLoading = () => {
    setLoading(false);
  };

  const handleUpdateUsername = async (username) => {
    let subscribed = await loadUser(username);
    dispatch(allActions.userActions.setUsername(username, subscribed));
  };

  const handleDispatchUpdate = () => {
    const event = new CustomEvent('updateArticles', {
      detail: { stopLoading },
    });
    document.dispatchEvent(event);
    setLoading(true);
    console.log(loading);
  };

  const handleLogOut = () => {
    dispatch(allActions.userActions.logOut());
  };

  const handleShowSettings = () => {
    setShowSettings(true);
  };

  const handleShowLogin = () => {
    setShowLogin(true);
  };

  const handleClose = () => {
    setShowSettings(false);
    setShowLogin(false);
  };

  const loadUser = async (username) => {
    let response = await fetch(
      `http://localhost:9091/api/subscriptions/authenticated/${username}`,
      {
        headers: {
          'Access-Control-Allow-Origin': '*',
        },
      },
    );
    let data = await response.json();
    return data.subscription.isSubscribed;
  };

  return (
    <>
      <Navbar variant="dark" bg="dark" expand="lg">
        <Container>
          <LinkContainer to="/">
            <Navbar.Brand>Feed Reader</Navbar.Brand>
          </LinkContainer>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <LinkContainer to="/">
                <Nav.Link>Home</Nav.Link>
              </LinkContainer>
              <LinkContainer to="/sources">
                <Nav.Link>Feed Sources</Nav.Link>
              </LinkContainer>
            </Nav>
            <Nav className="h-100 justify-content-end">
              {loading ? (
                <Loader
                  type="Puff"
                  color="lightgray"
                  height="45"
                  width="45"
                  visible="true"
                />
              ) : (
                ''
              )}
              {!user.loggedIn ? (
                <Nav.Link onClick={handleShowLogin}>Login</Nav.Link>
              ) : (
                <NavDropdown title={<FontAwesomeIcon icon={faUser} />} id="basic-nav-dropdown">
                  <LinkContainer to="/profile">
                    <NavDropdown.Item>Profile</NavDropdown.Item>
                  </LinkContainer>
                  <NavDropdown.Item onClick={handleLogOut}>
                    Log out
                  </NavDropdown.Item>
                </NavDropdown>
              )}
              <Nav.Link hidden={!updateButton.toUpdate} onClick={handleDispatchUpdate}>
                <FontAwesomeIcon icon={faSyncAlt} />
              </Nav.Link>
              <Nav.Link onClick={handleShowSettings}>
                <FontAwesomeIcon icon={faCog} />
              </Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
      {showSettings ? (
        <SettingsModal show={showSettings} handleClose={handleClose} />
      ) : (
        ''
      )}

      {showLogin ? (
        <UserLoginRegisterModal
          show={showLogin}
          handleUpdateUsername={handleUpdateUsername}
          handleClose={handleClose}
        />
      ) : (
        ''
      )}
    </>
  );
};

