import { Component } from 'react';
import { Button, Card, Col, Container, Row } from 'react-bootstrap';

class ArticleCard extends Component {
  constructor(props) {  
    super(props);
    this.state = {};
  }

  render() {
    return (
      <>
        <Col>
          <Card className="h-100 w-100">
            <Card.Img
              variant="top"
              className="h-100 w-100"
              src={
                this.props.article.image
                  ? this.props.article.image.imageUrl
                  : 'https://socialistmodernism.com/wp-content/uploads/2017/07/placeholder-image.png?w=640'
              }
              alt={
                this.props.article.image
                  ? this.props.article.image.imageAlt
                  : 'https://socialistmodernism.com/wp-content/uploads/2017/07/placeholder-image.png?w=640'
              }
            />
            <Card.Body>
              <Card.Title>{this.props.article.title}</Card.Title>
              <Card.Text>{this.props.article.summary}</Card.Text>
              <Container>
                <Row className="justify-center align-items-center lg-center md-center">
                  <Col className="d-flex flex-row justify-content-center">
                    <Button
                      onClick={() =>
                        this.props.handleArticleOpen(
                          this.props.article.id.id,
                          this.props.article.link.url,
                          this.props.article.title,
                        )
                      }
                      variant="primary"
                    >
                      Open Article
                    </Button>
                  </Col>
                </Row>
              </Container>
            </Card.Body>
            <Card.Footer className="text-muted form-control-sm">
              Published on: {this.transformDate()}
            </Card.Footer>
          </Card>
        </Col>
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
