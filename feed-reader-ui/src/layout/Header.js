import { Component } from 'react';
import { Container, Navbar, Nav, NavDropdown } from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import { Link } from 'react-router-dom';
import SettingsModal from '../components/modal/SettingsModal';
import UserLoginRegisterModal from '../components/modal/UserLoginRegisterModal';
class Header extends Component {
  constructor(props) {
    super(props);
    this.state = {
      showSettings: false,
      showLogin: false,
      username: null,
    };
  }

  componentDidMount() {
    this.handleUpdateUsername();
  }

  handleUpdateUsername = () => {
    this.setState({
      username: localStorage.getItem('username'),
    });
  };

  handleLogOut = () => {
    localStorage.removeItem('username');
    this.setState({
      username: null,
    });
  };

  handleLogin = () => {
    this.setState({
      username: localStorage.getItem('username'),
    });
  };

  handleShowSettings = () => {
    this.setState({
      showSettings: true,
    });
  };

  handleShowLogin = () => {
    this.setState({
      showLogin: true,
    });
  };

  handleClose = () => {
    this.setState({
      showSettings: false,
      showLogin: false,
    });
  };

  render() {
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
                <Nav.Link onClick={this.handleShowSettings}>Settings</Nav.Link>
                {!this.state.username ? (
                  <Nav.Link onClick={this.handleShowLogin}>Login</Nav.Link>
                ) : (
                  <NavDropdown title="Account" id="basic-nav-dropdown">
                    <LinkContainer to="/profile">
                      <NavDropdown.Item>Profile</NavDropdown.Item>
                    </LinkContainer>
                    <NavDropdown.Item onClick={this.handleLogOut}>
                      Log out
                    </NavDropdown.Item>
                  </NavDropdown>
                )}
              </Nav>
            </Navbar.Collapse>
          </Container>
        </Navbar>
        {this.state.showSettings ? (
          <SettingsModal
            show={this.state.showSettings}
            handleClose={this.handleClose}
          />
        ) : (
          ''
        )}

        {this.state.showLogin ? (
          <UserLoginRegisterModal
            show={this.state.showLogin}
            handleUpdateUsername={this.handleUpdateUsername}
            handleClose={this.handleClose}
          />
        ) : (
          ''
        )}
      </>
    );
  }
}

export default Header;
