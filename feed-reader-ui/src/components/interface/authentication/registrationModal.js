import {
  Modal,
  Button,
  Container,
  Form,
  NavLink,
  Col,
  Row,
} from 'react-bootstrap';

/**
 * This component is the registration form, it is used in the userLoginRegistration modal dialog,
 * where it can be switched with the login form
 */
export const RegistrationModal = (props) => {
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
              <Row>
                <Col>
                  <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>First Name</Form.Label>
                    <Form.Control
                      onChange={props.nameInputHandler}
                      value={props.nameBinding}
                      type="text"
                      placeholder="Enter first name"
                    />
                  </Form.Group>
                </Col>
                <Col>
                  <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>Last Name</Form.Label>
                    <Form.Control
                      onChange={props.lastNameInputHandler}
                      value={props.lastNameBinding}
                      type="text"
                      placeholder="Enter last name"
                    />
                  </Form.Group>
                </Col>
              </Row>
              <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>Username</Form.Label>
                <Form.Control
                  onChange={props.usernameInputHandler}
                  value={props.usernameBinding}
                  type="text"
                  placeholder="Enter username"
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
              <Form.Group className="mb-3" controlId="formBasicPassword">
                <Form.Label>Confirm Password</Form.Label>
                <Form.Control type="password" placeholder="Confirm password" />
              </Form.Group>
              <Form.Group className="mb-3" controlId="formBasicPassword">
                <Form.Label>Email</Form.Label>
                <Form.Control
                  onChange={props.emailInputHandler}
                  value={props.emailBinding}
                  type="email"
                  placeholder="user@example.com"
                />
              </Form.Group>
              <Form.Group className="mb-3" controlId="formBasicCheckbox">
                <Form.Check type="checkbox" label="Agree to terms" />
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
            Already have an account? login here
          </NavLink>
        </Modal.Footer>
      </Modal>
    </>
  );
};
