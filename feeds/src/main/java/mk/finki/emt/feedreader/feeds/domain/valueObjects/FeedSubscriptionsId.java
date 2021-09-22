package mk.finki.emt.feedreader.feeds.domain.valueObjects;

import lombok.NonNull;
import mk.finki.emt.feedreader.sharedkernel.domain.base.DomainObjectId;

public class FeedSubscriptionsId extends DomainObjectId {

  private FeedSubscriptionsId() {
    super(FeedSubscriptionsId.randomId(FeedSubscriptionsId.class).getId());
  }

  public FeedSubscriptionsId(@NonNull String uuid) {
    super(uuid);
  }
}
