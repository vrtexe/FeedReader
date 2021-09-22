package mk.finki.emt.feedreader.feeds.domain.valueObjects;

import javax.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Author {

  private final String name;

  private final String email;

  private final String uri;

  public Author() {
    this.name = "";
    this.email = "";
    this.uri = "";
  }

  public Author(String name) {
    this.name = name;
    this.email = null;
    this.uri = null;
  }

  public Author(String name, String email, String uri) {
    this.name = name;
    this.email = email;
    this.uri = uri;
  }
}
