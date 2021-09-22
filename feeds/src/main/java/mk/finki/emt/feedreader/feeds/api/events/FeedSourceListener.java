package mk.finki.emt.feedreader.feeds.api.events;

import lombok.AllArgsConstructor;
import mk.finki.emt.feedreader.feeds.services.FeedService;
import mk.finki.emt.feedreader.sharedkernel.domain.config.TopicHolder;
import mk.finki.emt.feedreader.sharedkernel.domain.events.DomainEvent;
import mk.finki.emt.feedreader.sharedkernel.domain.events.subscriptions.UserSubscribed;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FeedSourceListener {

  private final FeedService service;

  @KafkaListener(
    topics = TopicHolder.TOPIC_USER_SUBSCRIBED_TO_FEED,
    groupId = "feedSubscriptionGroup"
  )
  public void userSubscribedListener(@Payload String jsonMessage) {
    try {
      UserSubscribed event = DomainEvent.fromJson(
        jsonMessage,
        UserSubscribed.class
      );
      service.addSubscriberToFeed(event.getFeedSourceId());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  @KafkaListener(
    topics = TopicHolder.TOPIC_USER_UNSUBSCRIBED_FROM_FEED,
    groupId = "feedSubscriptionGroup"
  )
  public void UserUnsubscribedListener(@Payload(required = false) String jsonMessage) {
    try {
      UserSubscribed event = DomainEvent.fromJson(
        jsonMessage,
        UserSubscribed.class
      );
      service.removeSubscriberFromFeed(event.getFeedSourceId());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
