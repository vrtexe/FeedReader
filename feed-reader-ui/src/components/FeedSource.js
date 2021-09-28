import { Col, Row, Button, Image } from 'react-bootstrap';
import { shallowEqual, useSelector } from 'react-redux';

const FeedSource = (props) =>  {
  const user = useSelector((state) => state.currentUser, shallowEqual);

    return (
      <>
        <Row style={{ height: '2em' }} className="mb-2">
          <Col className="p-0 h-100" sm={1}>
            <Image
              className="w-100 h-100"
              src={
                props.feedSource.logo
                  ? props.feedSource.logo.imageUrl
                  : ''
              }
              alt={
                props.feedSource.logo
                  ? props.feedSource.logo.imageAlt
                  : ''
              }
              roundedCircle
            ></Image>
          </Col>
          <Col className="align-self-center justify-self-center">
            <div>{props.feedSource.link.url}</div>
          </Col>
          <Col className="align-self-center justify-self-end" sm={1}>
            <div>{props.feedSource.subscribers}</div>
          </Col>
          <Col className="d-flex justify-content-end align-items-center">
            {user.subscribed ? (
              user.username ? (
                props.feedSource.subscribed ? (
                  <Button
                    disabled={props.loading}
                    onClick={() =>
                      props.handleUnsubscribe(props.feedSource.id.id)
                    }
                    className="mx-2"
                    variant="primary"
                  >
                    Unsubscribe
                  </Button>
                ) : (
                  <Button
                    disabled={props.loading}
                    onClick={() =>
                      props.handleSubscribe(props.feedSource.id.id)
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
              disabled={props.loading}
              onClick={() =>
                props.removeFeedSource(props.feedSource.id.id)
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

export default FeedSource;
