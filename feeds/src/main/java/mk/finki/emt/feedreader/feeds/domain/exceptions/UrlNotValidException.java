package mk.finki.emt.feedreader.feeds.domain.exceptions;

public class UrlNotValidException extends Exception {

  public UrlNotValidException(String contentType) {
    super("The given url is not valid: The content type is unsupported: " + contentType );
  }

  public UrlNotValidException() {
    super("The given url is not valid");
  }
}
