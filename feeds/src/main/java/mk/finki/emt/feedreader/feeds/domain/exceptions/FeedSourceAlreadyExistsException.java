package mk.finki.emt.feedreader.feeds.domain.exceptions;

public class FeedSourceAlreadyExistsException extends RuntimeException {

  public FeedSourceAlreadyExistsException() {
    super("The feed source already exists");
  }
}
