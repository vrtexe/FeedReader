import { Col, Row, Button, Image } from 'react-bootstrap';
import { shallowEqual, useSelector } from 'react-redux';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { AlternatingButton } from './interface/alternatingButton';

const FeedSource = (props) => {
  const user = useSelector((state) => state.currentUser, shallowEqual);

  return (
    <>
      <Row style={{ height: '2.25em' }} className="my-3">
        <Col className="p-0 h-100" sm={1}>
          <Image
            className="w-50 h-100 border-1"
            src={
              props.feedSource.logo
                ? props.feedSource.logo.imageUrl
                : 'https://socialistmodernism.com/wp-content/uploads/2017/07/placeholder-image.png?w=640'
            }
            alt={
              props.feedSource.logo
                ? props.feedSource.logo.imageAlt
                : 'Placeholder image instead of logo'
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
          {user.loggedIn && user.subscribed ? (
            <AlternatingButton
                    alt={!props.feedSource.subscribed}
                    handleNormalAction={() => props.handleSubscribe(props.feedSource.id.id)
                      }
                    handleAltAction={() => props.handleUnsubscribe(props.feedSource.id.id)}
                    normalButtonText={'Subscribe'}
                    altButtonText={'Unsubscribe'}
                    altVariant={'danger'}
                    disabled={props.loading}
                    classNormal={'mx-2'}
                    classAlt={'mx-2'}
                  />
          ) : (
            ''
          )}
          <Button
            disabled={props.loading}
            onClick={() => props.removeFeedSource(props.feedSource.id.id)}
            variant="danger"
          >
            <FontAwesomeIcon icon={faTrash} />
          </Button>
        </Col>
      </Row>
    </>
  );
};
//TODO: remove the delete button when not logged in

export default FeedSource;
