package mk.finki.emt.feedreader.feeds.api.events;

import lombok.AllArgsConstructor;
import mk.finki.emt.feedreader.feeds.services.FeedService;
import mk.finki.emt.feedreader.sharedkernel.domain.config.TopicHolder;
import mk.finki.emt.feedreader.sharedkernel.domain.events.DomainEvent;
import mk.finki.emt.feedreader.sharedkernel.domain.events.subscriptions.UserSubscribed;
import mk.finki.emt.feedreader.sharedkernel.domain.events.subscriptions.UserUnsubscribeFromAll;
import mk.finki.emt.feedreader.sharedkernel.domain.events.subscriptions.UserUnsubscribed;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//Tuka se naigjaat site eventListenteners za soodvetnata klasa
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
      UserUnsubscribed event = DomainEvent.fromJson(
        jsonMessage,
        UserUnsubscribed.class
      );
      service.removeSubscriberFromFeed(event.getFeedSourceId());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  @KafkaListener(
    topics = TopicHolder.TOPIC_USER_UNSUBSCRIBED_FROM_SERVICE,
    groupId = "feedSubscriptionGroup"
  )
  public void UserUnsubscribedFromServiceListener(@Payload(required = false) String jsonMessage) {
    try {
      UserUnsubscribeFromAll event = DomainEvent.fromJson(
        jsonMessage,
        UserUnsubscribeFromAll.class
      );
      service.removeSubscriberFromAllFeed(event.getFeedSourceIds());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
