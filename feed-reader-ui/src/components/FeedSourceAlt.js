import { Image, Row, Col, Button } from 'react-bootstrap';
import { createUseStyles } from 'react-jss';
import { shallowEqual, useSelector } from 'react-redux';
import { LinkContainer } from 'react-router-bootstrap';
import { AlternatingButton } from './interface/alternatingButton';

/**
 * This component represents and alternate design for a single feed source row,
 * with less options
 */
const useStyles = createUseStyles({
  buttonLink: {
    color: 'DodgerBlue',
    textDecoration: 'none',
  },
});

const FeedSourceAlt = (props) => {
  const user = useSelector((state) => state.currentUser, shallowEqual);
  const classes = useStyles();

  const handleRedirectToSource = () => {
    window.open(props.feedSource.link.url, '__blank');
  };

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
          <LinkContainer to={`/sources/${props.feedSource.id.id}`}>
            <Button className={classes.buttonLink} variant="link">
              <div>{props.feedSource.title}</div>
            </Button>
          </LinkContainer>
        </Col>
        <Col className="align-self-center justify-self-end" sm={1}>
          <div>{props.feedSource.subscribers}</div>
        </Col>
        <Col className="d-flex justify-content-end align-items-center">
          {user.loggedIn && user.subscribed ? (
            <AlternatingButton
              alt={!props.feedSource.subscribed}
              handleNormalAction={() =>
                props.handleSubscribe(props.feedSource.id.id)
              }
              handleAltAction={() =>
                props.handleUnsubscribe(props.feedSource.id.id)
              }
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
          <Button onClick={handleRedirectToSource}>XML</Button>
        </Col>
      </Row>
    </>
  );
};

export default FeedSourceAlt;
