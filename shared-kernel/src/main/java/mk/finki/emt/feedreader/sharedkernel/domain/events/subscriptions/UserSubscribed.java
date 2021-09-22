package mk.finki.emt.feedreader.sharedkernel.domain.events.subscriptions;

import lombok.Getter;
import mk.finki.emt.feedreader.sharedkernel.domain.config.TopicHolder;
import mk.finki.emt.feedreader.sharedkernel.domain.events.DomainEvent;

@Getter
public class UserSubscribed extends DomainEvent {

  private String feedSourceId;

  public UserSubscribed(String feedSourceId) {
    super(TopicHolder.TOPIC_USER_SUBSCRIBED_TO_FEED);
    this.feedSourceId = feedSourceId;
  }

  public UserSubscribed() {
    super(TopicHolder.TOPIC_USER_SUBSCRIBED_TO_FEED);
  }
}
