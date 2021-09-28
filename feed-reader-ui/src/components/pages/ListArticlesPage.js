import { useEffect, useState } from 'react';
import { Container, Button, Row, Col } from 'react-bootstrap';
import Loader from 'react-loader-spinner';
import { shallowEqual, useDispatch, useSelector } from 'react-redux';
import allActions from '../../store/actions';
import ArticleCard from '../ArticleCard';
import EmbeddedHtmlModal from '../modal/EmbeddedHtmlModal';

const ListArticlesPage = () => {
  const [articles, setArticles] = useState([]);
  const [id, setId] = useState(null);
  const [url, setUrl] = useState(null);
  const [title, setTitle] = useState(null);
  const [page, setPage] = useState(0);
  const [last, setLast] = useState(false);
  const [loading, setLoading] = useState(false);
  const [personalized, setPersonalized] = useState(false);
  const user = useSelector((state) => state.currentUser, shallowEqual);
  const dispatch = useDispatch();

  useEffect(() => {
    if (page === 0 && !loading) {
      setLoading(true);
      reloadArticlesPageable();
    }
  });

  useEffect(() => {
    document.addEventListener('updateArticles', async (e) => {
      let response = await updateAllArticles(e.detail.stopLoading);
      console.log(response);
      setLoading(true);
      reloadArticlesPageable();
    });
    dispatch(allActions.articleActions.updateArticles(true));
    return () => {
      dispatch(allActions.articleActions.updateArticles(false));
    };
  }, []);

  const handleClose = () => {
    setId(null);
    setUrl(null);
    setTitle(null);
  };

  const handleArticleOpen = (id, url, title) => {
    setId(id);
    setUrl(url);
    setTitle(title);
  };

  const handlePersonalizedSwitch = () => {
    setPersonalized(!personalized);
    setPage(0);
    setLast(false);
    setArticles([]);
  };

  const handleWindowScroll = (e) => {
    if (e.target.scrollHeight - e.target.scrollTop === e.target.clientHeight) {
      setLoading(true);
      reloadArticlesPageable();
    }
  };

  const reloadArticles = async () => {
    if (personalized) {
      await loadAllPersonalizedArticles();
    } else {
      await loadAllArticles();
    }
  };

  const reloadArticlesPageable = async () => {
    if (personalized) {
      return loadAllArticlesPersonalizedPageable();
    } else {
      return loadAllArticlesPageable();
    }
  };

  const listAllArticles = () => {
    return articles.length === 0 ? (
      <p>There are no articles do display</p>
    ) : (
      articles.map((article) => (
        <ArticleCard
          handleArticleOpen={handleArticleOpen}
          key={article.id.id}
          article={article}
        />
      ))
    );
  };

  const loadAllArticles = async () => {
    let response = await fetch('http://localhost:9090/api/feeds/articles', {
      headers: {
        'Access-Control-Allow-Origin': '*',
      },
    });
    let data = await response.json();
    setArticles(data);
  };

  const loadAllPersonalizedArticles = async () => {
    let response = await fetch(
      `http://localhost:9090/api/feeds/user/${user.username}`,
      {
        headers: {
          'Access-Control-Allow-Origin': '*',
        },
      },
    );
    let data = await response.json();
    setArticles(data);
  };

  const updateAllArticles = (stopLoading) => {
    return fetch('http://localhost:9090/api/feeds//articles/update', {
      headers: {
        'Access-Control-Allow-Origin': '*',
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Request failed');
        }
        return response.json();
      })
      .then((data) => {
        if (data) {
          console.log('updated successfully');
        }
      })
      .catch((error) => {
        console.error(error);
      })
      .finally(() => {
        stopLoading();
      });
  };

  const loadAllArticlesPageable = async () => {
    if (!last) {
      let response = await fetch(
        `http://localhost:9090/api/feeds/pageable?page=${page}&size=${6}`,
        {
          headers: {
            'Access-Control-Allow-Origin': '*',
          },
        },
      ).catch((error) => {
        console.error(error);
      });

      let data = await response.json();
      if (!data.empty) {
        setArticles([...articles, ...data.content]);
        setPage(page + 1);
        setLast(data.empty);
      } else {
        setLast(true);
      }
    }
    setLoading(false);
  };

  const loadAllArticlesPersonalizedPageable = async () => {
    if (!last) {
      let response = await fetch(
        `http://localhost:9090/api/feeds/user/${
          user.username
        }/pageable?page=${page}&size=${6}`,
        {
          headers: {
            'Access-Control-Allow-Origin': '*',
          },
        },
      ).catch((error) => {
        console.error(error);
      });

      let data = await response.json();
      if (!data.empty) {
        setArticles([...articles, ...data.content]);
        setPage(page + 1);
        setLast(data.empty);
      } else {
        setLast(true);
      }
    }
    setLoading(false);
  };

  return (
    <>
      <div
        onScroll={handleWindowScroll}
        style={{
          height: '90vh',
          overflowY: 'auto',
          overflowX: 'hidden',
          width: '100%',
        }}
      >
        <Container>
          <Container className="p-2">
            {user.loggedIn && user.subscribed ? (
              <>
                <Button
                  disabled={!personalized}
                  className="mr-3"
                  onClick={handlePersonalizedSwitch}
                >
                  All
                </Button>
                <Button
                  disabled={personalized}
                  className="mx-3"
                  onClick={handlePersonalizedSwitch}
                >
                  Personalized
                </Button>
              </>
            ) : (
              ''
            )}
          </Container>

          <Row sm={1} md={2} lg={3} className="g-5">
            {listAllArticles()}
          </Row>
          {loading ? (
            <Row>
              <Col className="d-flex justify-content-center ">
                <Loader
                  className="mt-4"
                  type="Puff"
                  color="lightgray"
                  height="50"
                  width="50"
                  visible="true"
                />
              </Col>
            </Row>
          ) : (
            ''
          )}
          {url ? (
            <EmbeddedHtmlModal
              id={id}
              url={url}
              title={title}
              handleClose={handleClose}
            />
          ) : (
            ''
          )}
        </Container>
      </div>
    </>
  );
};

export default ListArticlesPage;
