package mk.finki.emt.feedreader.sharedkernel.domain.events.subscriptions;

import lombok.Getter;
import mk.finki.emt.feedreader.sharedkernel.domain.config.TopicHolder;
import mk.finki.emt.feedreader.sharedkernel.domain.events.DomainEvent;

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
