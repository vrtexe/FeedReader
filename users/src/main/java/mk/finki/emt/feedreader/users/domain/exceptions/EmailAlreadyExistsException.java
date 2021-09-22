package mk.finki.emt.feedreader.users.domain.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

  public EmailAlreadyExistsException(String email) {
    super("User already exists on the email: " + email);
  }
}
