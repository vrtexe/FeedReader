import { Component } from 'react';
import { Button, Card } from 'react-bootstrap';

class ArticleCard extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <>
        <Card className="mx-2 mb-2" style={{ width: '22rem' }}>
          <Card.Img variant="top" cl src={this.props.article.image} />
          <Card.Body>
            <Card.Title>{this.props.article.title}</Card.Title>
            <Card.Text>{this.props.article.summary}</Card.Text>
            <Button
              onClick={() =>
                this.props.handleArticleOpen(
                  this.props.article.link.url,
                  this.props.article.title,
                )
              }
              variant="primary"
            >
              Go somewhere
            </Button>
          </Card.Body>
          <Card.Footer className="text-muted form-control-sm">
            Published on: {this.transformDate()}
          </Card.Footer>
        </Card>
      </>
    );
  }

  transformDate = () => {
    let date = new Date(Date.parse(this.props.article.published));
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
}

export default ArticleCard;
