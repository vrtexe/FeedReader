import { Component } from 'react';
import { Container, Button } from 'react-bootstrap';
import ArticleCard from '../ArticleCard';
import EmbeddedHtmlModal from '../modal/EmbeddedHtmlModal';

class ListArticlesPage extends Component {
  constructor(props) {
    super(props);
    this.state = {
      articles: [],
      url: null,
      title: null,
      personalized: false,
      user: null,
    };
  }

  componentDidMount() {
    this.loadAllArticles();
    this.loadUser();
  }

  handleClose = () => {
    this.setState({
      url: null,
      title: null,
    });
  };

  handleArticleOpen = (url, title) => {
    this.setState({
      url,
      title,
    });
  };

  handlePersonalizedSwitch = async () => {
    this.setState({
      personalized: !this.state.personalized,
    });
    await this.reloadArticles();
  };

  reloadArticles = async () => {
    if (!this.state.personalized) {
      await this.loadAllPersonalizedArticles();
    } else {
      await this.loadAllArticles();
    }
  };

  render() {
    return (
      <>
        <Container>
          <Container className="p-2">
            {this.state.user ? (
              <Button onClick={this.handlePersonalizedSwitch}>
                Switch to personalized
              </Button>
            ) : (
              ''
            )}
          </Container>
          <div className="d-flex flex-wrap h-100 justify-content-center align-items-baseline my-1">
            {this.listAllArticles()}
          </div>
          {this.state.url !== null ? (
            <EmbeddedHtmlModal
              url={this.state.url}
              title={this.state.title}
              handleClose={this.handleClose}
            />
          ) : (
            ''
          )}
        </Container>
      </>
    );
  }

  listAllArticles = () => {
    return this.state.articles.length === 0 ? (
      <p>There are no articles do display</p>
    ) : (
      this.state.articles.map((article) => (
        <ArticleCard
          handleArticleOpen={this.handleArticleOpen}
          key={article.id.id}
          article={article}
        />
      ))
    );
  };

  loadAllArticles = async () => {
    let response = await fetch('http://localhost:9090/api/feeds/articles', {
      headers: {
        'Access-Control-Allow-Origin': '*',
      },
    });
    let data = await response.json();
    this.setState({
      articles: data,
    });
  };

  loadUser = async () => {
    let username = localStorage.getItem('username');
    if (!username) {
      return [];
    }
    let response = await fetch(
      `http://localhost:9091/api/subscriptions/authenticated/${username}`,
      {
        headers: {
          'Access-Control-Allow-Origin': '*',
        },
      },
    );
    let data = await response.json();
    this.setState({
      user: data.subscription.isSubscibed,
    });
  };

  loadAllPersonalizedArticles = async () => {
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
    this.setState({
      articles: data,
    });
  };
}

export default ListArticlesPage;
