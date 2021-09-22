package mk.finki.emt.feedreader.users.domain.models;

import java.time.Instant;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NonNull;
import mk.finki.emt.feedreader.sharedkernel.domain.base.AbstractEntity;
import mk.finki.emt.feedreader.users.domain.valueobjects.FeedSourceId;

@Getter
@Entity
@Table(name = "subscriptions")
public class FeedSubscription extends AbstractEntity<FeedSubscriptionsId> {

  private Instant since;

  @AttributeOverride(
    name = "id",
    column = @Column(name = "feedsource_id", nullable = false)
  )
  private FeedSourceId feedSourceId;

  private FeedSubscription() {
    super(FeedSubscriptionsId.randomId(FeedSubscriptionsId.class));
    this.since = Instant.now();
  }

  protected FeedSubscription(@NonNull FeedSourceId feedSourceId) {
    super(FeedSubscriptionsId.randomId(FeedSubscriptionsId.class));
    this.since = Instant.now();
    this.feedSourceId = feedSourceId;
  }
}
