import { useState } from 'react';
import { Container, Navbar, Nav, NavDropdown } from 'react-bootstrap';
import { useDispatch, useSelector } from 'react-redux';
import { LinkContainer } from 'react-router-bootstrap';
import { SettingsModal } from '../components/modal/SettingsModal';
import UserLoginRegisterModal from '../components/modal/UserLoginRegisterModal';
import allActions from '../store/actions';

const Header = () => {
  const [showSettings, setShowSettings] = useState(false);
  const [showLogin, setShowLogin] = useState(false);
  const user = useSelector((state) => state.currentUser);

  const dispatch = useDispatch();

  const handleUpdateUsername = async (username) => {
    let subscribed = await loadUser(username);
    console.log(subscribed);
    dispatch(allActions.userActions.setUsername(username, subscribed));
  };

  const handleLogOut = () => {
    console.log(dispatch(allActions.userActions.logOut()));
    console.log(user);
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
    return data.subscription.isSubscibed;
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
            <Nav className="justify-content-end">
              <Nav.Link onClick={handleShowSettings}>Settings</Nav.Link>
              {!user.loggedIn ? (
                <Nav.Link onClick={handleShowLogin}>Login</Nav.Link>
              ) : (
                <NavDropdown title="Account" id="basic-nav-dropdown">
                  <LinkContainer to="/profile">
                    <NavDropdown.Item>Profile</NavDropdown.Item>
                  </LinkContainer>
                  <NavDropdown.Item onClick={handleLogOut}>
                    Log out
                  </NavDropdown.Item>
                </NavDropdown>
              )}
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

export default Header;
