import { Component } from 'react';
import {
  Modal,
  Button,
  Container,
  Form,
  NavLink,
  Col,
  Row,
} from 'react-bootstrap';

class UserLoginModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loginMode: true,
      name: '',
      lastName: '',
      username: '',
      password: '',
      confirmPassword: '',
      email: '',
    };
  }

  handleFirstNameInput = (e) => {
    this.setState({
      name: e.target.value,
    });
  };

  handleLastNameInput = (e) => {
    this.setState({
      lastName: e.target.value,
    });
  };

  handleUsernameInput = (e) => {
    this.setState({
      username: e.target.value,
    });
  };

  handlePasswordInput = (e) => {
    this.setState({
      password: e.target.value,
    });
  };

  handleEmailInput = (e) => {
    this.setState({
      email: e.target.value,
    });
  };

  handleFormSwitch = () => {
    this.setState({
      loginMode: !this.state.loginMode,
    });
  };

  handleFormSwitch = () => {
    this.setState({
      loginMode: !this.state.loginMode,
    });
  };

  handleRegisterSubmit = async (e) => {
    e.preventDefault();
    let credentials = {
      name: this.state.name,
      lastName: this.state.lastName,
      username: this.state.username,
      password: this.state.password,
      email: this.state.email,
    };
    await fetch('http://localhost:9091/api/subscriptions/register', {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(credentials),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Request failed');
        }
        return response.json();
      })
      .then((data) => {
        this.props.handleUpdateUsername(data.username);
        this.props.handleClose();
      })
      .catch((error) => {
        console.log(error.message);
      });
  };

  handleLoginSubmit = async (e) => {
    e.preventDefault();
    let credentials = {
      username: this.state.username,
      password: this.state.password,
    };
    await fetch('http://localhost:9091/api/subscriptions/login', {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(credentials),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Request failed');
        }
        return response.json();
      })
      .then((data) => {
        this.props.handleUpdateUsername(data.username);
        this.props.handleClose();
      })
      .catch((error) => {
        console.log(error.message);
      });
  };

  render() {
    return (
      <>
        {this.state.loginMode ? (
          <Modal
            show={this.props.show}
            onHide={this.props.handleClose}
            size="md"
            aria-labelledby="contained-modal-title-vcenter"
            backdrop="static"
            centered
          >
            <Modal.Header closeButton>
              <Modal.Title id="contained-modal-title-vcenter">
                Login Form
              </Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <Container>
                <Form onSubmit={this.handleLoginSubmit}>
                  <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>Username</Form.Label>
                    <Form.Control
                      onChange={this.handleUsernameInput}
                      value={this.state.username}
                      type="text"
                      placeholder="Enter username"
                    />
                  </Form.Group>
                  <Form.Group className="mb-3" controlId="formBasicPassword">
                    <Form.Label>Password</Form.Label>
                    <Form.Control
                      onChange={this.handlePasswordInput}
                      value={this.state.password}
                      type="password"
                      placeholder="Password"
                    />
                  </Form.Group>
                  <Form.Group className="mb-3" controlId="formBasicCheckbox">
                    <Form.Check type="checkbox" label="Remember login" />
                  </Form.Group>
                  <Row>
                    <Col className="d-flex justify-content-center">
                      <Button variant="primary" className="w-25" type="submit">
                        Log in
                      </Button>
                    </Col>
                  </Row>
                </Form>
              </Container>
            </Modal.Body>
            <Modal.Footer className="d-flex justify-content-center">
              <NavLink onClick={this.handleFormSwitch}>
                Don't have an account yet? register now
              </NavLink>
            </Modal.Footer>
          </Modal>
        ) : (
          <Modal
            show={this.props.show}
            onHide={this.props.handleClose}
            size="md"
            aria-labelledby="contained-modal-title-vcenter"
            backdrop="static"
            centered
          >
            <Modal.Header closeButton>
              <Modal.Title id="contained-modal-title-vcenter">
                Login Form
              </Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <Container>
                <Form onSubmit={this.handleRegisterSubmit}>
                  <Row>
                    <Col>
                      <Form.Group className="mb-3" controlId="formBasicEmail">
                        <Form.Label>First Name</Form.Label>
                        <Form.Control
                          onChange={this.handleFirstNameInput}
                          value={this.state.name}
                          type="text"
                          placeholder="Enter first name"
                        />
                      </Form.Group>
                    </Col>
                    <Col>
                      <Form.Group className="mb-3" controlId="formBasicEmail">
                        <Form.Label>Last Name</Form.Label>
                        <Form.Control
                          onChange={this.handleLastNameInput}
                          value={this.state.lastName}
                          type="text"
                          placeholder="Enter last name"
                        />
                      </Form.Group>
                    </Col>
                  </Row>
                  <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>Username</Form.Label>
                    <Form.Control
                      onChange={this.handleUsernameInput}
                      value={this.state.username}
                      type="text"
                      placeholder="Enter username"
                    />
                  </Form.Group>
                  <Form.Group className="mb-3" controlId="formBasicPassword">
                    <Form.Label>Password</Form.Label>
                    <Form.Control
                      onChange={this.handlePasswordInput}
                      value={this.state.password}
                      type="password"
                      placeholder="Password"
                    />
                  </Form.Group>
                  <Form.Group className="mb-3" controlId="formBasicPassword">
                    <Form.Label>Confirm Password</Form.Label>
                    <Form.Control
                      type="password"
                      placeholder="Confirm password"
                    />
                  </Form.Group>
                  <Form.Group className="mb-3" controlId="formBasicPassword">
                    <Form.Label>Email</Form.Label>
                    <Form.Control
                      onChange={this.handleEmailInput}
                      value={this.state.email}
                      type="email"
                      placeholder="user@example.com"
                    />
                  </Form.Group>
                  <Form.Group className="mb-3" controlId="formBasicCheckbox">
                    <Form.Check type="checkbox" label="Agree to terms" />
                  </Form.Group>
                  <Row>
                    <Col className="d-flex justify-content-center">
                      <Button variant="primary" className="w-25" type="submit">
                        Register
                      </Button>
                    </Col>
                  </Row>
                </Form>
              </Container>
            </Modal.Body>
            <Modal.Footer className="d-flex justify-content-center">
              <NavLink onClick={this.handleFormSwitch}>
                Already have an account? login here
              </NavLink>
            </Modal.Footer>
          </Modal>
        )}
      </>
    );
  }
}

export default UserLoginModal;
