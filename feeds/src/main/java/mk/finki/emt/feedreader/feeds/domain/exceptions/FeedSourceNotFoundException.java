package mk.finki.emt.feedreader.feeds.domain.exceptions;

public class FeedSourceNotFoundException extends RuntimeException {

  public FeedSourceNotFoundException() {
    super("The Feed source with the given ID does not exist");
  }
}
