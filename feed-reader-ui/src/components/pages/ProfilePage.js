import { useEffect, useState } from 'react';
import { Container, Row, Col, Button } from 'react-bootstrap';
import { shallowEqual, useDispatch, useSelector } from 'react-redux';
import allActions from '../../store/actions';

const UserProfilePage = () => {
  const [userInfo, setUserInfo] = useState(null);
  const user = useSelector((state) => state.currentUser, shallowEqual);
  const dispatch = useDispatch();

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
      }).then(data => {
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
        <Container>
          <Row>
            <Col>{userInfo.name}</Col>
            <Col>{userInfo.lastName}</Col>
            <Col></Col>
            <Col>{userInfo.email}</Col>
          </Row>
          <Row>
            <Col>
              {!userInfo.subscription.isSubscibed ? (
                <Button onClick={handleSubscribe}>Subscribe</Button>
              ) : (
                <Button onClick={handleUnsubscribe}>Unsubscribe</Button>
              )}
            </Col>
            <Col>{transformDate(userInfo.subscription.since)}</Col>
          </Row>
        </Container>
      ) : (
        ''
      )}
    </>
  );
};

export default UserProfilePage;
