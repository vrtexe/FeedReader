package mk.finki.emt.feedreader.users.domain.exceptions;

public class UserNameAlreadyExistsException extends RuntimeException {

  public UserNameAlreadyExistsException(String username) {
    super("User already exists with the username: " + username);
  }
}
