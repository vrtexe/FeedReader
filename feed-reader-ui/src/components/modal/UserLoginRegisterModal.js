import { Component } from 'react';

import { LoginModal } from '../interface/authentication/LoginModal';
import { RegistrationModal } from '../interface/authentication/registrationModal';

/**
 * This component is a modal dialog and focuses on authentication,
 * it handles communication with the server for everything related to authentication
 */
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
          <LoginModal
            show={this.props.show}
            closeHandler={this.props.handleClose}
            formHeader={'Login form'}
            usernameFieldName={'Username'}
            usernameFieldType={'text'}
            usernameInputHandler={this.handleUsernameInput}
            usernameBinding={this.state.username}
            passwordInputHandler={this.handlePasswordInput}
            passwordBinding={this.state.password}
            buttonAction={this.handleFormSwitch}
            formSubmitHandler={this.handleLoginSubmit}
            submitButtonText={'Log in'}
          />
        ) : (
          <RegistrationModal
            show={this.props.show}
            closeHandler={this.props.handleClose}
            formHeader={'Registration form'}
            nameInputHandler={this.handleFirstNameInput}
            nameBinding={this.state.name}
            lastNameInputHandler={this.handleLastNameInput}
            lastNameBinding={this.state.lastName}
            emailInputHandler={this.handleEmailInput}
            emailBinding={this.state.email}
            usernameInputHandler={this.handleUsernameInput}
            usernameBinding={this.state.username}
            passwordInputHandler={this.handlePasswordInput}
            passwordBinding={this.state.password}
            buttonAction={this.handleFormSwitch}
            formSubmitHandler={this.handleRegisterSubmit}
            submitButtonText={'Register'}
          />
        )}
      </>
    );
  }
}

export default UserLoginModal;
