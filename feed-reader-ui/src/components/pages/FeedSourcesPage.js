import { useEffect, useState } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import { shallowEqual, useSelector } from 'react-redux';
import FeedSourceAlt from '../FeedSourceAlt';
import { createUseStyles } from 'react-jss';

/**
 * The feed source page shows a list of all the feed sources available,
 * you can visit the page for a single feed source by clicking on it,
 * you can also subscribe from here, but you need to first subscribe to the service
 */
const useStyles = createUseStyles({
  pageTitle: {
    fontSize: 'x-large',
    fontWeight: 'bold',
    fontFamily: 'Helvetica, Arial, sans-serif',
  },
});

const FeedSourcePage = (props) => {
  const [feedSources, setFeedSources] = useState([]);
  const [loading, setLoading] = useState(false);
  const user = useSelector((state) => state.currentUser, shallowEqual);
  const classes = useStyles();

  useEffect(() => {
    setLoading(true);
    loadFeedSources(props.filter);
  }, []);

  const handleSubscribe = async (id) => {
    setLoading(true);
    let subscriptionForm = {
      username: user.username,
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
        setTimeout(loadFeedSources, 300);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const handleUnsubscribe = async (id) => {
    setLoading(true);
    let subscriptionForm = {
      username: user.username,
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
        setTimeout(() => loadFeedSources(props.filter), 300);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const loadFeedSources = async (filter) => {
    let response = await fetch('http://localhost:9090/api/feeds', {
      headers: {
        'Access-Control-Allow-Origin': '*',
      },
    });
    let data = await response.json();
    let userSources = await loadUSerFeedSources();
    data = data
      .map((feedSource) => {
        feedSource.subscribed =
          userSources.find(
            (userSource) => userSource.feedSourceId.id === feedSource.id.id,
          ) !== undefined;
        return feedSource;
      })
      .sort((a, b) => b.subscribers - a.subscribers);
    console.log('here');
    if (filter === 'user') {
      console.log('here1');

      data = data.filter((feedSource) => {
        console.log(feedSource.subscribed);
        return feedSource.subscribed;
      });
    }
    console.log(data);
    setFeedSources(data);
    setLoading(false);
  };

  const loadUSerFeedSources = async () => {
    if (!user.username) {
      return [];
    }
    let response = await fetch(
      `http://localhost:9091/api/subscriptions/${user.username}`,
      {
        headers: {
          'Access-Control-Allow-Origin': '*',
        },
      },
    );
    let data = await response.json();
    return data;
  };

  const listSources = () => {
    return feedSources.length === 0 ? (
      <p>There are no sources, please add some with the form bellow</p>
    ) : (
      feedSources.map((fs) => (
        <FeedSourceAlt
          key={fs.id.id}
          feedSource={fs}
          handleSubscribe={handleSubscribe}
          handleUnsubscribe={handleUnsubscribe}
          loading={loading}
        />
      ))
    );
  };

  return (
    <>
      <Container className="py-5">
        <Container>
          <h2 className={classes.pageTitle}>Feed Sources</h2>
        </Container>
        <Container style={{ height: '100%' }} className="overflow-auto">
          <Row style={{ height: '2.25em' }} className="my-3">
            <Col className="p-0 h-100" sm={1}>
              Logo
            </Col>
            <Col className="d-flex flex-row  justify-content-center">
              <div>Title</div>
            </Col>
            <Col className="d-flex flex-row  justify-content-center" sm={1}>
              <div>Subscribers</div>
            </Col>
            <Col className="d-flex flex-row  justify-content-center">
              <div>Controls</div>
            </Col>
          </Row>
          {listSources()}
        </Container>
      </Container>
    </>
  );
};

export default FeedSourcePage;
