import { useEffect, useState } from 'react';
import { Container, Button, Row } from 'react-bootstrap';
import { shallowEqual, useSelector } from 'react-redux';
import ArticleCard from '../ArticleCard';
import EmbeddedHtmlModal from '../modal/EmbeddedHtmlModal';

const ListArticlesPage = () => {
  const [articles, setArticles] = useState([]);
  const [id, setId] = useState(null);
  const [url, setUrl] = useState(null);
  const [title, setTitle] = useState(null);
  const [personalized, setPersonalized] = useState(false);
  const user = useSelector((state) => state.currentUser, shallowEqual);

  useEffect(() => {
    loadAllArticles();
    console.log(user)
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

  const handlePersonalizedSwitch = async () => {
    setPersonalized(!personalized);
    await reloadArticles();
  };

  const reloadArticles = async () => {
    if (!personalized) {
      await loadAllPersonalizedArticles();
    } else {
      await loadAllArticles();
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
    let username = localStorage.getItem('username');
    let response = await fetch(
      `http://localhost:9090/api/feeds/user/${username}`,
      {
        headers: {
          'Access-Control-Allow-Origin': '*',
        },
      },
    );
    let data = await response.json();
    setArticles(data);
  };

  return (
    <>
      <Container>
        <Container className="p-2">
          {user.loggedIn && user.subscribed ? (
            <Button onClick={handlePersonalizedSwitch}>
              Switch to personalized
            </Button>
          ) : (
            ''
          )}
        </Container>
        <Row sm={1} md={2} lg={3} className="g-5">
          {listAllArticles()}
        </Row>
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
    </>
  );
};

export default ListArticlesPage;
