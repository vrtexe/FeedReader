package mk.finki.emt.feedreader.users.domain.valueobjects;

import lombok.NonNull;
import mk.finki.emt.feedreader.sharedkernel.domain.base.DomainObjectId;

public class FeedSourceId extends DomainObjectId {

  private FeedSourceId() {
    super(FeedSourceId.randomId(FeedSourceId.class).getId());
  }

  public FeedSourceId(@NonNull String uuid) {
    super(uuid);
  }
}
