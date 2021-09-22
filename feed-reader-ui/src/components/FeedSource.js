import { Component } from 'react';
import { Col, Row, Button, Image } from 'react-bootstrap';

class FeedSource extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: null,
    };
  }

  componentDidMount() {
    this.setState({
      username: localStorage.getItem('username'),
    });
  }

  render() {
    return (
      <>
        <Row className="mb-2">
          <Col md={1}>
            <Image
              className="w-100 h-100"
              src={this.props.feedSource.logo.imageUrl}
              alt={this.props.feedSource.logo.imageAlt}
              roundedCircle
            ></Image>
          </Col>
          <Col className="d-flex justify-content-start align-items-center px-5">
            <div>{this.props.feedSource.link.url}</div>
          </Col>
          <Col md={1}>
            <div>{this.props.feedSource.subscribers}</div>
          </Col>
          <Col className="d-flex justify-content-end align-items-center">
            {this.state.username ? (
              this.props.feedSource.subscribed ? (
                <Button
                  onClick={() =>
                    this.props.handleUnsubscribe(this.props.feedSource.id.id)
                  }
                  className="mx-2"
                  variant="primary"
                >
                  Unsubscribe
                </Button>
              ) : (
                <Button
                  onClick={() =>
                    this.props.handleSubscribe(this.props.feedSource.id.id)
                  }
                  className="mx-2"
                  variant="primary"
                >
                  Subscribe
                </Button>
              )
            ) : (
              ''
            )}
            <Button
              disabled={this.props.loading}
              onClick={() =>
                this.props.removeFeedSource(this.props.feedSource.id.id)
              }
              variant="danger"
            >
              Remove
            </Button>
          </Col>
        </Row>
      </>
    );
  }
}

export default FeedSource;