package mk.finki.emt.feedreader.feeds.domain.exceptions;

public class TypeOfFeedNotSupportedException extends RuntimeException {

  public TypeOfFeedNotSupportedException() {
    super("The type of feed provided is not supported");
  }
}
