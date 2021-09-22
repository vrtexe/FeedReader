package mk.finki.emt.feedreader.sharedkernel.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.util.ProxyUtils;

@MappedSuperclass
@Embeddable
@Getter
public class DomainObjectId implements Serializable {

  private String id;

  @JsonCreator
  protected DomainObjectId(@NonNull String uuid) {
    this.id = Objects.requireNonNull(uuid, "uuid must not be null");
  }

  @NonNull
  public static <ID extends DomainObjectId> ID randomId(
    @NonNull Class<ID> idClass
  ) {
    Objects.requireNonNull(idClass, "idClass must not be null");
    try {
      return idClass
        .getConstructor(String.class)
        .newInstance(UUID.randomUUID().toString());
    } catch (Exception ex) {
      throw new RuntimeException(
        "Could not create new instance of " + idClass,
        ex
      );
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null || !getClass().equals(ProxyUtils.getUserClass(o))) {
      return false;
    }
    DomainObjectId obj2 = (DomainObjectId) o;
    return this.getId() != null && this.getId().equals(obj2.getId());
  }

  @Override
  public int hashCode() {
    return id == null ? super.hashCode() : id.hashCode();
  }
}
