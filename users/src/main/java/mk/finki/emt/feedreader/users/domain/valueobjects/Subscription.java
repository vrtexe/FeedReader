package mk.finki.emt.feedreader.users.domain.valueobjects;

import java.time.Instant;
import javax.persistence.Embeddable;
import lombok.Getter;

/**
 *
 * This class represents the subscription to the service,
 * the subscription information is kept in the same table as the user.
 * ! This class does not have any relations to the feed subscription
 */
@Getter
@Embeddable
public class Subscription {

  private final Boolean isSubscribed;

  private final Instant since;

  private final Integer feeds;

  protected Subscription() {
    this.isSubscribed = null;
    this.since = null;
    this.feeds = null;
  }

  public Subscription(Boolean is, Instant since, Integer feeds) {
    this.isSubscribed = is;
    this.since = since;
    this.feeds = feeds;
  }

  /**
   * This method is used to renew a subscription
   * @return a new instance of the current class with updated values
   */
  public Subscription renew() {
    return new Subscription(true, Instant.now(), feeds);
  }

  /**
   * This method is used to unsubscribe from the service
   * @return a new instance of the current class with updated values
   */
  public Subscription unsubscribe() {
    return new Subscription(false, Instant.now(), 0);
  }

  /**
   * This method is used to count the number of feeds the user is subscribed to
   * @return a new instance of the current class with updated values
   */
  public Subscription AddFeed() {
    return new Subscription(isSubscribed, since, feeds + 1);
  }

  /**
   * This method is used to count the number of feeds the user is subscribed to
   * @return a new instance of the current class with updated values
   */
  public Subscription removeFeed() {
    return new Subscription(isSubscribed, since, feeds - 1);
  }
}
