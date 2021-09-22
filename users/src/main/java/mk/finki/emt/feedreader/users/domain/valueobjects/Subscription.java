package mk.finki.emt.feedreader.users.domain.valueobjects;

import java.time.Instant;
import javax.persistence.Embeddable;
import lombok.Getter;

//Pri pravenjen subskripcija ili vilo kakvi izmeni se dobiva nov objekt soodvetno
@Getter
@Embeddable
public class Subscription {

  private final Boolean isSubscibed;

  private final Instant since;

  private final Integer feeds;

  protected Subscription() {
    this.isSubscibed = null;
    this.since = null;
    this.feeds = null;
  }

  public Subscription(Boolean is, Instant since, Integer feeds) {
    this.isSubscibed = is;
    this.since = since;
    this.feeds = feeds;
  }

  public Subscription renew() {
    return new Subscription(true, Instant.now(), feeds);
  }

  public Subscription unsubscribe() {
    return new Subscription(false, Instant.now(), 0);
  }

  public Subscription AddFeed() {
    return new Subscription(isSubscibed, since, feeds + 1);
  }

  public Subscription removeFeed() {
    return new Subscription(isSubscibed, since, feeds - 1);
  }
}
