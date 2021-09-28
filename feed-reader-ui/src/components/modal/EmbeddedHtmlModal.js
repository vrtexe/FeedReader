import { Component } from 'react';
import { Row, Col, Container, Modal } from 'react-bootstrap';
import Loader from 'react-loader-spinner';

class EmbeddedHtmlModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      htmlContent: '',
      loading: false,
    };
  }

  async componentDidMount() {
    this.setState({
      loading: true,
    });
    void this.loadArticlePage().catch((error) => {
      console.log(error);
    });
  }

  render() {
    return (
      <>
        <Modal
          size="lg"
          show={this.props.url}
          onHide={this.props.handleClose}
          aria-labelledby="iframeModal"
        >
          <Modal.Header closeButton>
            <Modal.Title id="iframeModal">{this.props.title}</Modal.Title>
          </Modal.Header>
          <Modal.Body className="w-100">
            {this.state.loading ? (
              <Container>
                <Row>
                  <Col className="d-flex justify-content-center">
                    <Loader
                      type="Puff"
                      color="lightgray"
                      height="100"
                      width="100"
                      visible="true"
                    />
                  </Col>
                </Row>
              </Container>
            ) : (
              <iframe
                style={{ height: '80vh' }}
                className="w-100"
                srcDoc={this.state.htmlContent}
                title={this.props.title}
              ></iframe>
            )}
          </Modal.Body>
        </Modal>
      </>
    );
  }

  loadArticlePage = async () => {
    let response = await fetch(
      `http://localhost:9090/api/feeds//article/${this.props.id}`,
      {
        headers: {
          'Access-Control-Allow-Origin': '*',
        },
      },
    );
    let data = await response.json();
    this.setState({
      htmlContent: data.content,
      loading: false,
    });
  };
}

export default EmbeddedHtmlModal;
