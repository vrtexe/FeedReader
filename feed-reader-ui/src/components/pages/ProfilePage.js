import { useEffect, useState } from 'react';
import { createUseStyles } from 'react-jss';
import { shallowEqual, useDispatch, useSelector } from 'react-redux';
import allActions from '../../store/actions';
import UserInfoTab from '../interface/userInfoTab';
import FeedSourcePage from './FeedSourcesPage';
import ProfileSideCard from '../interface/ProfileSideCard';
import { AlternatingText } from '../interface/alternatingText';
import { AlternatingButton } from '../interface/alternatingButton';

const useStyles = createUseStyles({
  profileContainer: {
    width: '100%',
    display: 'flex',
    flexDirection: 'column',
    backgroundColor: '#eeeeee',
    boxSizing: 'border-box',
    padding: '5% 0',
    justifyContent: 'center',
    alignItems: 'center',
    fontFamily: 'Helvetica, Arial, sans-serif',
  },
  profileCard: {
    width: '70%',
    aspectRatio: '2/1',
    backgroundColor: '#ffffff',
    border: '0 lightgray solid',
    boxSizing: 'border-box',
    borderRadius: '1em',
    display: 'flex',
    flexDirection: 'row',
    boxShadow: 'rgba(99, 99, 99, 0.2) 0px 2px 8px 0px;',
    fontFamily: 'inherit',
  },

  subscribed: {
    color: 'PaleGreen',
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    fontSize: '3vw',
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

  notSubscribedField: {
    color: 'crimson',
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    fontSize: '2vw',
    height: '100%',
    transform: 'rotateY(0deg) rotate(15deg)',
    fontWeight: 'bold',
  },

  additionalInfo: {
    width: '65%',
    boxSizing: 'border-box',
    padding: '2.5%',
    display: 'flex',
    flexDirection: 'column',
  },

  unsubscribedInfo: {
    backgroundColor: 'darkgray',
    borderRadius: '1em',
    boxSizing: 'border-box',
    padding: '0 3%',
    height: '70%',
  },

  subscriptionTab: {
    height: '20%',
    boxSizing: 'border-box',
    padding: '0 3%',
    display: 'flex',
    width: '100%',
    justifyContent: 'center',
    alignItems: 'center',
  },

  subscribeButtonContainer: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
    padding: '1% 0',
    boxSizing: 'border-box',
    borderTop: '0.1em solid LightGray',
  },

  SubscribeButton: {
    height: '100%',
    width: '50%',
    padding: '2%',
    fontSize: '2vw',
    borderRadius: '0.25em',
  },

  subscribedFeedSources: {
    border: '1px white solid',
    borderRadius: '1em',
    backgroundColor: 'white',
    marginTop: '1%',
    width: '70%',
  },
});

const UserProfilePage = () => {
  const [userInfo, setUserInfo] = useState(null);
  const user = useSelector((state) => state.currentUser, shallowEqual);
  const dispatch = useDispatch();
  const classes = useStyles();

  useEffect(() => {
    loadUser();
  }, []);

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
    setUserInfo(data);
    return data;
  };

  const handleSubscribe = async (id) => {
    await fetch(
      `http://localhost:9091/api/subscriptions/subscribe/${userInfo.authentication.username}`,
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
        return loadUser();
      })
      .then((data) => {
        console.log(data);
        dispatch(
          allActions.userActions.setSubscribed(data.subscription.isSubscibed),
        );
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const handleUnsubscribe = async () => {
    await fetch(
      `http://localhost:9091/api/subscriptions/unsubscribe/${userInfo.authentication.username}`,
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
        return loadUser();
      })
      .then((data) => {
        dispatch(
          allActions.userActions.setSubscribed(data.subscription.isSubscibed),
        );
      })
      .catch((error) => {
        console.log(error);
      });
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
      {userInfo ? (
        <main className={classes.profileContainer}>
          <div className={classes.profileCard}>
            <ProfileSideCard
              fieldOneLabel={'Name'}
              fieldTwoLabel={'Surname'}
              fieldOneValue={userInfo.name}
              fieldTwoValue={userInfo.lastName}
            >
              <AlternatingText
                normalText={'Subscribed'}
                altText={'Not Subscribed'}
                classNormal={classes.subscribed}
                classAlt={classes.notSubscribed}
                alt={userInfo.subscription.isSubscibed}
              />
            </ProfileSideCard>
            <div className={classes.additionalInfo}>
              <UserInfoTab
                tabTitle={'Information'}
                fieldOneLabel={'Email'}
                fieldTwoLabel={'Username'}
                fieldOneValue={userInfo.email}
                fieldTwoValue={userInfo.authentication.username}
              />
              <UserInfoTab
                tabTitle={'Subscription'}
                fieldOneLabel={'Feeds'}
                fieldTwoLabel={'Since'}
                fieldOneValue={userInfo.subscription.feeds}
                fieldTwoValue={transformDate(userInfo.subscription.since)}
                altContent={!userInfo.subscription.isSubscibed}
              >
                <div className={classes.unsubscribedInfo}>
                  <span className={classes.notSubscribedField}>
                    Not Subscribed
                  </span>
                </div>
              </UserInfoTab>
              <div className={classes.subscriptionTab}>
                <div className={classes.subscribeButtonContainer}>
                  <AlternatingButton
                    alt={!userInfo.subscription.isSubscibed}
                    handleNormalAction={handleSubscribe}
                    handleAltAction={handleUnsubscribe}
                    normalButtonText={'Subscribe'}
                    altButtonText={'Unsubscribe'}
                    altVariant={'danger'}
                    classNormal={classes.SubscribeButton}
                    classAlt={classes.SubscribeButton}
                  />
                </div>
              </div>
            </div>
          </div>
          <div className={classes.subscribedFeedSources}>
            <FeedSourcePage filter={'user'} />
          </div>
        </main>
      ) : (
        ''
      )}
    </>
  );
};

export default UserProfilePage;
