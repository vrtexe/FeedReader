package mk.finki.emt.feedreader.sharedkernel.domain.config;

/**
 * All the event topics, that can be published or have listeners attached to
 */
public class TopicHolder {

  public static final String TOPIC_USER_SUBSCRIBED_TO_FEED =
    "user-subscribed-to-feed";
  public static final String TOPIC_USER_UNSUBSCRIBED_FROM_FEED =
    "user-unsubscribed-from-feed";
  public static final String TOPIC_USER_UNSUBSCRIBED_FROM_SERVICE =
    "user-unsubscribed-from-service";
}
