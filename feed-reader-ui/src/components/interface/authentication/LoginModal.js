import {
  Modal,
  Button,
  Container,
  Form,
  NavLink,
  Col,
  Row,
} from 'react-bootstrap';

export const LoginModal = (props) => {
  return (
    <>
      <Modal
        show={props.show}
        onHide={props.closeHandler}
        size="md"
        aria-labelledby="contained-modal-title-vcenter"
        backdrop="static"
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title id="contained-modal-title-vcenter">
            {props.formHeader}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Container>
            <Form onSubmit={props.formSubmitHandler}>
              <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>{props.usernameFieldName}</Form.Label>
                <Form.Control
                  onChange={props.usernameInputHandler}
                  value={props.usernameBinding}
                  type={props.usernameFieldType}
                  placeholder={`Enter ${props.usernameFieldName.toLowerCase()}`}
                />
              </Form.Group>
              <Form.Group className="mb-3" controlId="formBasicPassword">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  onChange={props.passwordInputHandler}
                  value={props.passwordBinding}
                  type="password"
                  placeholder="Password"
                />
              </Form.Group>
              <Form.Group className="mb-3" controlId="formBasicCheckbox">
                <Form.Check type="checkbox" label="Remember login" />
              </Form.Group>
              <Row>
                <Col className="d-flex justify-content-center">
                  <Button variant="primary" className="w-25" type="submit">
                    {props.submitButtonText}
                  </Button>
                </Col>
              </Row>
            </Form>
          </Container>
        </Modal.Body>
        <Modal.Footer className="d-flex justify-content-center">
          <NavLink onClick={props.buttonAction}>
            Don't have an account yet? register now
          </NavLink>
        </Modal.Footer>
      </Modal>
    </>
  );
};
