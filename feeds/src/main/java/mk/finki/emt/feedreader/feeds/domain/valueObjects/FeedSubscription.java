package mk.finki.emt.feedreader.feeds.domain.valueObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.Getter;
import mk.finki.emt.feedreader.feeds.domain.models.FeedSourceId;
import mk.finki.emt.feedreader.sharedkernel.domain.base.ValueObject;

/**
 * This class is used to represent objects that correspond to the class from the users module,
 * the objects are received in a JSON format, and are later mapped to this class using jackson.
 */
@Getter
public class FeedSubscription implements ValueObject {

  private final FeedSubscriptionsId id;

  private final FeedSourceId feedSourceId;

  private final Instant since;

  @JsonCreator
  public FeedSubscription(
    @JsonProperty("id") FeedSubscriptionsId id,
    @JsonProperty("feedSourceId") FeedSourceId feedSourceId,
    @JsonProperty("since") Instant since
  ) {
    this.id = id;
    this.feedSourceId = feedSourceId;
    this.since = since;
  }
}
