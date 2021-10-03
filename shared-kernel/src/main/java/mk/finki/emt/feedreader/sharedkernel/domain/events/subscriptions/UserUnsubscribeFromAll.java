package mk.finki.emt.feedreader.sharedkernel.domain.events.subscriptions;

import java.util.Collection;
import lombok.Getter;
import mk.finki.emt.feedreader.sharedkernel.domain.config.TopicHolder;
import mk.finki.emt.feedreader.sharedkernel.domain.events.DomainEvent;

/**
 * The event container published when a user has unsubscribed from the service.
 * it contains all the data needed when the listener intercepts the event
 */
@Getter
public class UserUnsubscribeFromAll extends DomainEvent {

  private Collection<String> feedSourceIds;

  public UserUnsubscribeFromAll(Collection<String> feedSourceIds) {
    super(TopicHolder.TOPIC_USER_UNSUBSCRIBED_FROM_SERVICE);
    this.feedSourceIds = feedSourceIds;
  }

  public UserUnsubscribeFromAll() {
    super(TopicHolder.TOPIC_USER_UNSUBSCRIBED_FROM_SERVICE);
  }
}
