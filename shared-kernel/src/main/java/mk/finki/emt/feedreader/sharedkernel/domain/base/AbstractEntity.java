package mk.finki.emt.feedreader.sharedkernel.domain.base;

import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.util.ProxyUtils;

/**
 * The base class that all entities represented in the database extend from
 * It consists of an id with the corresponding type of id to the entity type
 */
@Getter
@MappedSuperclass
public class AbstractEntity<ID extends DomainObjectId> {

  @EmbeddedId
  private ID id;

  protected AbstractEntity(@NonNull ID id) {
    this.id = Objects.requireNonNull(id, "id must not be null");
  }

  protected AbstractEntity(@NonNull AbstractEntity<ID> source) {
    Objects.requireNonNull(source, "source must not be null");
    this.id = source.id;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || !getClass().equals(ProxyUtils.getUserClass(obj))) {
      return false;
    }

    var other = (AbstractEntity<?>) obj;
    return id != null && id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return id == null ? super.hashCode() : id.hashCode();
  }

  @Override
  public String toString() {
    return String.format("%s[%s]", getClass().getSimpleName(), id);
  }
}
