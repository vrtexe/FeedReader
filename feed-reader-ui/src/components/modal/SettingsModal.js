import { Component } from 'react';
import {
  Col,
  Container,
  Form,
  Modal,
  Button,
  Row,
  FloatingLabel,
} from 'react-bootstrap';
import FeedSource from '../FeedSource';

class SettingsModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      feedSources: [],
      url: '',
      loading: false,
    };
  }

  async componentDidMount() {
    await this.loadFeedSources();
    await this.loadUser();
  }

  handleSubmit = (e) => {
    e.preventDefault();
    this.addFeedSource(this.state.url);
  };

  handleUrlWrite = (e) => {
    this.setState({
      url: e.target.value,
    });
  };

  render() {
    return (
      <>
        <Modal
          size="xl"
          fullscreen={true}
          show={this.props.show}
          onHide={this.props.handleClose}
        >
          <Container>
            <Modal.Header closeButton>
              <Modal.Title>Settings</Modal.Title>
            </Modal.Header>
            <Modal.Body className="h-100 overflow-auto">
              <Container style={{ height: '50vh' }} className="overflow-auto">
                {this.listSources()}
              </Container>
            </Modal.Body>
            <Modal.Footer>
              <Container>
                <Form onSubmit={this.handleSubmit} className="w-100">
                  <Form.Group className="mb-3 h-100" controlId="formBasicEmail">
                    <Row>
                      <Col className="align-items-center">
                        <FloatingLabel
                          controlId="floatingInput"
                          label="URL"
                          className="mb-3"
                        >
                          <Form.Control
                            type="text"
                            onChange={this.handleUrlWrite}
                            value={this.state.url}
                            placeholder="https://someUrl/rss.xml"
                          />
                        </FloatingLabel>
                      </Col>
                      <Col className="h-100 align-items-center p-1" md="1">
                        <Button
                          variant="primary"
                          size="lg"
                          className="h-100"
                          type="submit"
                          disabled={this.state.loading}
                        >
                          Add
                        </Button>
                      </Col>
                    </Row>
                  </Form.Group>
                </Form>
              </Container>
            </Modal.Footer>
          </Container>
        </Modal>
      </>
    );
  }

  listSources() {
    return this.state.feedSources.length === 0 ? (
      <p>There are no sources</p>
    ) : (
      this.state.feedSources.map((fs) => (
        <FeedSource
          key={fs.id.id}
          feedSource={fs}
          subbed={this.state.user}
          handleSubscribe={this.handleSubscribe}
          handleUnsubscribe={this.handleUnsubscribe}
          removeFeedSource={this.removeFeedSource}
          loading={this.state.loading}
        />
      ))
    );
  }

  loadFeedSources = async () => {
    let response = await fetch('http://localhost:9090/api/feeds', {
      headers: {
        'Access-Control-Allow-Origin': '*',
      },
    });
    let data = await response.json();
    let userSources = await this.loadUSerFeedSources();
    this.setState({
      feedSources: data
        .map((feedSource) => {
          feedSource.subscribed = userSources.find(
            (userSource) => userSource.feedSourceId.id === feedSource.id.id,
          )
            ? true
            : false;
          return feedSource;
        })
        .sort((a, b) => b.subscribers - a.subscribers),
    });
  };

  loadUSerFeedSources = async () => {
    let username = localStorage.getItem('username');
    if (!username) {
      return [];
    }
    let response = await fetch(
      `http://localhost:9091/api/subscriptions/${username}`,
      {
        headers: {
          'Access-Control-Allow-Origin': '*',
        },
      },
    );
    let data = await response.json();
    return data;
  };

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
    this.setState({
      user: data.subscription.isSubscibed,
    });
  };

  addFeedSource = async (url) => {
    this.setState({
      loading: true,
    });
    let newFeedSource = { url };
    await fetch('http://localhost:9090/api/feeds', {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(newFeedSource),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Request failed');
        }
        return response.json();
      })
      .then((data) => {
        this.setState({
          feedSources: [...this.state.feedSources, data],
          loading: false,
        });
      })
      .catch((error) => {
        console.log(error);
      });
  };

  removeFeedSource = async (id) => {
    await fetch(`http://localhost:9090/api/feeds/${id}`, {
      method: 'DELETE',
      headers: {
        'Access-Control-Allow-Origin': '*',
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Request failed');
        } else {
          this.setState({
            loading: false,
            feedSources: this.state.feedSources.filter((v) => v.id.id !== id),
          });
        }
      })
      .catch((error) => {
        console.log(error);
      });
  };

  handleSubscribe = async (id) => {
    let username = localStorage.getItem('username');
    let subscriptionForm = {
      username: username,
      feedSourceId: id,
    };
    await fetch('http://localhost:9091/api/subscriptions/feed/subscribe', {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(subscriptionForm),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Request failed');
        }
        this.loadFeedSources();
      })
      .catch((error) => {
        console.log(error);
      });
  };

  handleUnsubscribe = async (id) => {
    let username = localStorage.getItem('username');
    let subscriptionForm = {
      username: username,
      feedSourceId: id,
    };
    await fetch('http://localhost:9091/api/subscriptions/feed/unsubscribe', {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(subscriptionForm),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Request failed');
        }
        this.loadFeedSources();
      })
      .catch((error) => {
        console.log(error);
      });
  };
}

export default SettingsModal;
