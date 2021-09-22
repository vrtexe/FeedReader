package mk.finki.emt.feedreader.users.domain.models;

import lombok.NonNull;
import mk.finki.emt.feedreader.sharedkernel.domain.base.DomainObjectId;

public class UserId extends DomainObjectId {

  private UserId() {
    super(UserId.randomId(UserId.class).getId());
  }

  public UserId(@NonNull String uuid) {
    super(uuid);
  }
}
