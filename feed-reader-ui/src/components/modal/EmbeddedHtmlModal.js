import { Component } from 'react';
import { Modal } from 'react-bootstrap';

class EmbeddedHtmlModal extends Component {
  constructor(props) {
    super(props);
    this.state = {};
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
            <iframe style={{height:'80vh'}} className="w-100" src={this.props.url} title={this.props.title}></iframe>
          </Modal.Body>
        </Modal>
      </>
    );
  }
}

export default EmbeddedHtmlModal;