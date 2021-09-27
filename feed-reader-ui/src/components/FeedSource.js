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
        <Row style={{ height: '2em' }} className="mb-2">
          <Col className="p-0 h-100" sm={1}>
            <Image
              className="w-100 h-100"
              src={
                this.props.feedSource.logo
                  ? this.props.feedSource.logo.imageUrl
                  : ''
              }
              alt={
                this.props.feedSource.logo
                  ? this.props.feedSource.logo.imageAlt
                  : ''
              }
              roundedCircle
            ></Image>
          </Col>
          <Col className="align-self-center justify-self-center">
            <div>{this.props.feedSource.link.url}</div>
          </Col>
          <Col className="align-self-center justify-self-end" sm={1}>
            <div>{this.props.feedSource.subscribers}</div>
          </Col>
          <Col className="d-flex justify-content-end align-items-center">
            {this.props.subbed ? (
              this.state.username ? (
                this.props.feedSource.subscribed ? (
                  <Button
                    disabled={this.props.loading}
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
                    disabled={this.props.loading}
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
