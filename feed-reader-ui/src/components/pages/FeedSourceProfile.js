import { useEffect, useState } from 'react';
import { createUseStyles } from 'react-jss';
import { shallowEqual, useSelector } from 'react-redux';
import { useParams } from 'react-router';
import { Button } from 'react-bootstrap';
import { AlternatingButton } from '../interface/alternatingButton';
import { AlternatingText } from '../interface/alternatingText';
import ListArticlesPage from './ListArticlesPage';

/**
 * The feed source page shows data related to a single feed source,
 * it contains the logic of subscribing an unsubscribing from a feed source,
 */
const useStyles = createUseStyles({
  pageContainer: {
    width: '100%',
    padding: '3% 10%',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#eeeeee',
    fontFamily: 'Helvetica, Arial, sans-serif',
  },

  feedSourceCard: {
    width: '100%',
    border: '0px lightgray solid',
    borderRadius: '1.6em',
    aspectRatio: '3/1',
    backgroundColor: '#2a2a2a',
    boxSizing: 'border-box',
    display: 'flex',
    flexDirection: 'row',
    boxShadow: 'rgba(99, 99, 99, 0.2) 0px 2px 8px 0px;',
  },

  mainInfoPanel: {
    width: '65%',
    height: '100%',
    padding: '2%',
    boxSizing: 'border-box',
    borderRadius: '1.5em',
    backgroundColor: '#f5f5f5',
  },

  headingPanel: {
    width: '100%',
    height: '33%',
    display: 'flex',
    justifyContent: 'start',
    alignItems: 'center',
    boxSizing: 'border-box',
    padding: '1%',
    paddingBottom: '2%',
    borderBottom: '0.1em lightGray solid',
  },

  logo: {
    backgroundImage: (props) => `url(${props.url})`,
    backgroundSize: '100% 100%',
    height: '100%',
    aspectRatio: '1/1',
    borderRadius: '1.4em',
    marginRight: '1%',
  },

  title: {
    minWidth: '70%',
    marginLeft: '1%',
    fontSize: '1.6em',
    fontWeight: 'bold',
  },

  descriptionPanel: {
    width: '100%',
    height: '35%',
    backgroundColor: 'lightGray',
    borderRadius: '0.5em',
    boxSizing: 'border-box',
    padding: '2%',
    margin: '1% 0',
  },

  subscribeButtonContainer: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
    height: '25%',
    boxSizing: 'border-box',
    padding: '1% 0',
    borderTop: '0.1em solid LightGray',
  },

  SubscribeButton: {
    height: '80%',
    width: '30%',
    padding: '2%',
    margin: '0 2%',
    fontSize: '1.5vw',
    borderRadius: '0.25em',
  },

  rightPanel: {
    height: '100%',
    width: '35%',
    boxSizing: 'border-box',
    padding: '2%',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    color: 'white',
  },

  informationSection: {
    height: '60%',
    width: '100%',
    boxSizing: 'border-box',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    color: 'white',
  },

  valuePreviewBoxed: {
    width: '100%',
    display: 'flex',
    justifyContent: 'space-evenly',
    boxSizing: 'border-box',
    padding: '3%',
    marginBottom: '5%',
    border: '1px white solid',
    backgroundColor: 'darkGray',
    fontWeight: 'bold',
    fontSize: '1.8vw',
    borderRadius: '0.5em',
  },

  subscriptionStatus: {
    height: '30%',
    width: '100%',
  },

  subscribed: {
    color: 'PaleGreen',
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    fontSize: '2.3vw',
    height: '100%',
  },

  notSubscribed: {
    color: 'crimson',
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    fontSize: '2.3vw',
    height: '100%',
  },

  copyrightSection: {
    fontSize: '0.6em',
    color: 'dimGray',
    height: '5%',
    boxSizing: 'border-box',
    padding: '0.1em',
  },

  subscriptionDate: {
    height: '10%',
    width: '100%',
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'end',
    alignItems: 'end',
    fontSize: '0.8em',
    color: 'lightGray',
  },

  articlesPreview: {
    width: '100%',
    height: '100%',
    padding: '2%',
    boxSizing: 'border-box',
    marginTop: '1%',
    backgroundColor: '#f5f5f5',
    borderRadius: '1.5em',
    boxShadow: 'rgba(99, 99, 99, 0.2) 0px 2px 8px 0px;',
  },
});

export const FeedSourceProfile = () => {
  const [feedSource, setFeedSource] = useState(null);
  const [loading, setLoading] = useState(false);
  const [subscribed, setSubscribed] = useState(false);
  const [subscription, setSubscription] = useState(null);
  const user = useSelector((state) => state.currentUser, shallowEqual);
  const { id } = useParams();
  const classes = useStyles({
    url: feedSource && feedSource.logo ? feedSource.logo.imageUrl : '',
  });

  useEffect(() => {
    async function fetchData() {
      const userResponse = await loadUser();
      computeSubscribed(userResponse);
      findSubscription(userResponse);
    }
    fetchData();
    loadFeedSource();
  }, []);

  const computeSubscribed = (updatedUser) => {
    if (updatedUser) {
      setSubscribed(
        updatedUser.subscriptions.find((sub) => sub.feedSourceId.id === id) !==
          undefined,
      );
    }
    setLoading(false);
  };

  const findSubscription = (updatedUser) => {
    if (updatedUser) {
      setSubscription(
        updatedUser.subscriptions.find((sub) => sub.feedSourceId.id === id),
      );
    } else {
      setSubscription(null);
    }
  };

  const loadFeedSource = async () => {
    let response = await fetch(`http://localhost:9090/api/feeds/${id}`, {
      headers: {
        'Access-Control-Allow-Origin': '*',
      },
    });
    let data = await response.json();
    setFeedSource(data);
    return data;
  };

  const loadUser = async () => {
    if (!user.username) {
      return;
    }
    let response = await fetch(
      `http://localhost:9091/api/subscriptions/authenticated/${user.username}`,
      {
        headers: {
          'Access-Control-Allow-Origin': '*',
        },
      },
    );
    let data = await response.json();
    return data;
  };

  const handleSubscribe = async () => {
    setLoading(true);
    let subscriptionForm = {
      username: user.username,
      feedSourceId: id,
    };
    let response = await fetch(
      'http://localhost:9091/api/subscriptions/feed/subscribe',
      {
        method: 'POST',
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(subscriptionForm),
      },
    ).catch((error) => {
      console.log(error);
    });
    let data = await response.json();
    if (data) {
      if (data.completed) {
        let updatedUser = await loadUser();
        computeSubscribed(updatedUser);
        findSubscription(updatedUser);
        loadFeedSource();
      }
    }
  };

  const handleUnsubscribe = async () => {
    setLoading(true);
    let subscriptionForm = {
      username: user.username,
      feedSourceId: id,
    };
    let response = await fetch(
      'http://localhost:9091/api/subscriptions/feed/unsubscribe',
      {
        method: 'POST',
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(subscriptionForm),
      },
    ).catch((error) => {
      console.log(error);
    });
    let data = await response.json();
    if (data) {
      if (data.completed) {
        let updatedUser = await loadUser();
        computeSubscribed(updatedUser);
        findSubscription(updatedUser);
        loadFeedSource();
      }
    }
  };

  const handleRedirectToSource = () => {
    window.open(feedSource.link.url, '__blank');
  };

  const transformDate = (d) => {
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

  return (
    <>
      {feedSource ? (
        <>
          <div className={classes.pageContainer}>
            <div className={classes.feedSourceCard}>
              <div className={classes.mainInfoPanel}>
                <div className={classes.headingPanel}>
                  <div className={classes.logo}></div>
                  <div className={classes.title}>{feedSource.title}</div>
                </div>
                <div className={classes.descriptionPanel}>
                  {feedSource.description}
                </div>
                <div className={classes.subscribeButtonContainer}>
                  {user.loggedIn && user.subscribed ? (
                    <AlternatingButton
                      alt={!subscribed}
                      handleNormalAction={handleSubscribe}
                      handleAltAction={handleUnsubscribe}
                      normalButtonText={'Subscribe'}
                      altButtonText={'Unsubscribe'}
                      altVariant={'danger'}
                      disabled={loading}
                      classNormal={classes.SubscribeButton}
                      classAlt={classes.SubscribeButton}
                    />
                  ) : (
                    ''
                  )}
                  <Button
                    className={classes.SubscribeButton}
                    onClick={handleRedirectToSource}
                  >
                    XML
                  </Button>
                </div>
                <div className={classes.copyrightSection}>
                  {feedSource.copyright}
                </div>
              </div>
              <div className={classes.rightPanel}>
                <div className={classes.informationSection}>
                  <div className={classes.valuePreviewBoxed}>
                    <span>Articles: </span>
                    <span>{feedSource.articles.length}</span>
                  </div>
                  <div className={classes.valuePreviewBoxed}>
                    <span>Subscribers: </span>
                    <span>{feedSource.subscribers}</span>
                  </div>
                </div>
                <div className={classes.subscriptionStatus}>
                  <AlternatingText
                    normalText={'Subscribed'}
                    altText={'Not Subscribed'}
                    classNormal={classes.subscribed}
                    classAlt={classes.notSubscribed}
                    alt={subscribed}
                  />
                </div>
                <div className={classes.subscriptionDate}>
                  {subscription ? <>{transformDate(subscription.since)}</> : ''}
                </div>
              </div>
            </div>
            <div className={classes.articlesPreview}>
              <ListArticlesPage overflow={'none'} id={id} filter="feedSource" />
            </div>
          </div>
        </>
      ) : (
        ''
      )}
    </>
  );
};
