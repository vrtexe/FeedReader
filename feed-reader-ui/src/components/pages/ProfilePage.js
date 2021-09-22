import { Component } from 'react';
import { Container, Row, Col, Button } from 'react-bootstrap';

class UserProfilePage extends Component {
  constructor(props) {
    super(props);
    this.state = {
      user: null,
    };
  }

  async componentDidMount() {
    await this.loadUser();
  }

  render() {
    return (
      <>
        {this.state.user ? (
          <Container>
            <Row>
              <Col>{this.state.user.name}</Col>
              <Col>{this.state.user.lastName}</Col>
              <Col></Col>
              <Col>{this.state.user.email}</Col>
            </Row>
            <Row>
              <Col>
                {!this.state.user.subscription.isSubscibed ? (
                  <Button onClick={this.handleSubscribe}>Subscribe</Button>
                ) : (
                  <Button onClick={this.handleUnsubscribe}>Unsubscribe</Button>
                )}
              </Col>
              <Col>
                {this.transformDate(this.state.user.subscription.since)}
              </Col>
            </Row>
          </Container>
        ) : (
          ''
        )}
      </>
    );
  }

  loadUser = async () => {
    let username = localStorage.getItem('username');
    if (!username) {
      return [];
    }
    let response = await fetch(
      `http://localhost:9091/api/subscriptions/authenticated/${username}`,
      {
        headers: {
          'Access-Control-Allow-Origin': '*',
        },
      },
    );
    let data = await response.json();
    console.log(data);
    this.setState({
      user: data,
    });
  };

  handleSubscribe = async (id) => {
    await fetch(
      `http://localhost:9091/api/subscriptions/subscribe/${this.state.user.authentication.username}`,
      {
        method: 'PATCH',
        headers: {
          'Access-Control-Allow-Origin': '*',
        },
      },
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error('Request failed');
        }
        this.loadUser();
      })
      .catch((error) => {
        console.log(error);
      });
  };

  handleUnsubscribe = async () => {
    await fetch(
      `http://localhost:9091/api/subscriptions/unsubscribe/${this.state.user.authentication.username}`,
      {
        method: 'PATCH',
        headers: {
          'Access-Control-Allow-Origin': '*',
        },
      },
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error('Request failed');
        }
        this.loadUser();
      })
      .catch((error) => {
        console.log(error);
      });
  };

  transformDate = (d) => {
    let date = new Date(Date.parse(d));
    return (
      (date.getDate().toString().length === 1
        ? '0' + date.getDate()
        : date.getDate()) +
      '/' +
      (date.getMonth().toString().length === 1
        ? '0' + date.getMonth()
        : date.getMonth()) +
      '/' +
      date.getFullYear() +
      ' ' +
      (date.getHours().toString().length === 1
        ? '0' + date.getHours()
        : date.getHours()) +
      ':' +
      (date.getMinutes().toString().length === 1
        ? '0' + date.getMinutes()
        : date.getMinutes())
    );
  };
}

export default UserProfilePage;
