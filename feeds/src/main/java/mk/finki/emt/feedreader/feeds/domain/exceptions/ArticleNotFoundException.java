package mk.finki.emt.feedreader.feeds.domain.exceptions;

public class ArticleNotFoundException extends RuntimeException {

  public ArticleNotFoundException() {
    super("An Article with the provided ID does not exist");
  }
}
