package mk.finki.emt.feedreader.feeds.domain.exceptions;

public class UnsupportedXMLStreamReaderState extends RuntimeException {

  public UnsupportedXMLStreamReaderState() {
    super("The current state of the stream reader is unsupported");
  }
}
