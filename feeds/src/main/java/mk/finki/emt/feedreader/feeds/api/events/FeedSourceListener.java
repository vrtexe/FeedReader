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

/**
 * * The FeedSourceListener class contains all the kafka event listeners 
 */
@Service
@AllArgsConstructor
public class FeedSourceListener {

  private final FeedService service;

  /**
   * * The event listener for the 'user subscribed to feed' topic, the event fires when user subscribes to a feed source.
   * after the event is fired a function is called to add to the counter keeping track of the number of subscribers for a feed.
   * @param jsonMessage is the data sent by the request, that is then mapped to the UserSubscribed object located in shared-kernel
   */
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

  /**
   * * The event listener for the 'user unsubscribed from feed' topic, the event fires when user unsubscribes from a feed source.
   * after the event is fired a function is called to subtract from the counter keeping track of the number of subscribers for a feed.
   * @param jsonMessage is the data sent by the request, that is then mapped to the UserUnSubscribed object located in shared-kernel
   */
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

  /**
   * * The event listener for the 'user unsubscribed from service' topic, the event fires when user unsubscribes from the global service.
   * after the event is fired a function is called to subtract from all of the feeds the user was subscribed to.
   * @param jsonMessage is the data sent by the request, that is then mapped to the UserUnsubscribeFromAll object located in shared-kernel
   */
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
