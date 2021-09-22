package mk.finki.emt.feedreader.feeds.domain.models;

import lombok.NonNull;
import mk.finki.emt.feedreader.sharedkernel.domain.base.DomainObjectId;

public class ArticleId extends DomainObjectId {

  private ArticleId() {
    super(ArticleId.randomId(ArticleId.class).getId());
  }

  public ArticleId(@NonNull String uuid) {
    super(uuid);
  }
}
