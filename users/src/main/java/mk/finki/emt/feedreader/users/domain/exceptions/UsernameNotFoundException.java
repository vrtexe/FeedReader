package mk.finki.emt.feedreader.users.domain.exceptions;

public class UsernameNotFoundException extends RuntimeException {

  public UsernameNotFoundException(String username) {
    super("Could not find a user with the username: " + username);
  }
}
