package mk.finki.emt.feedreader.sharedkernel.domain.events.subscriptions;

import lombok.Getter;
import mk.finki.emt.feedreader.sharedkernel.domain.config.TopicHolder;
import mk.finki.emt.feedreader.sharedkernel.domain.events.DomainEvent;

/**
 * The event container published when the user unsubscribed from a feed
 * it contains all the data needed when the listener intercepts the event
 */
@Getter
public class UserUnsubscribed extends DomainEvent {

  private String feedSourceId;

  public UserUnsubscribed(String feedSourceId) {
    super(TopicHolder.TOPIC_USER_UNSUBSCRIBED_FROM_FEED);
    this.feedSourceId = feedSourceId;
  }

  public UserUnsubscribed() {
    super(TopicHolder.TOPIC_USER_UNSUBSCRIBED_FROM_FEED);
  }
}
