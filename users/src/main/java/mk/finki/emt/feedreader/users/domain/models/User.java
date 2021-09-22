package mk.finki.emt.feedreader.users.domain.models;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import mk.finki.emt.feedreader.sharedkernel.domain.base.AbstractEntity;
import mk.finki.emt.feedreader.users.domain.valueobjects.AuthInfo;
import mk.finki.emt.feedreader.users.domain.valueobjects.FeedSourceId;
import mk.finki.emt.feedreader.users.domain.valueobjects.Subscription;

@Getter
@Entity
@Table(name = "users")
public class User extends AbstractEntity<UserId> {

  private String name;

  private String lastName;

  private AuthInfo authentication;

  private String email;

  private Subscription subscription;

  @OneToMany(
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.EAGER
  )
  private Set<FeedSubscription> subscriptions;

  protected User() {
    super(UserId.randomId(UserId.class));
    this.name = null;
    this.lastName = null;
    this.authentication = null;
    this.email = null;
    this.subscription = null;
  }

  public User(
    String Name,
    String lastName,
    AuthInfo authentication,
    String email
  ) {
    super(UserId.randomId(UserId.class));
    this.name = Name;
    this.lastName = lastName;
    this.authentication = authentication;
    this.email = email;
    this.subscription = new Subscription(false, Instant.now(), 0);
    this.subscriptions = new HashSet<FeedSubscription>();
  }

  public User(
    UserId id,
    String Name,
    String lastName,
    AuthInfo authentication,
    String email,
    Subscription subscription
  ) {
    super(id);
    this.name = Name;
    this.lastName = lastName;
    this.authentication = authentication;
    this.email = email;
    this.subscription = subscription;
    this.subscriptions = new HashSet<FeedSubscription>();
  }

  public User renewSubscription() {
    this.subscription = this.subscription.renew();
    return this;
  }

  public User unsubscribe() {
    this.subscription = this.subscription.unsubscribe();
    return this;
  }

  public User subscribeToFeed(FeedSourceId id) {
    this.subscriptions.add(new FeedSubscription(id));
    this.subscription = this.subscription.AddFeed();
    return this;
  }

  public User unsubscribeFromFeed(FeedSourceId id) {
    this.subscriptions.removeIf(sub -> sub.getFeedSourceId().equals(id));
    this.subscription = this.subscription.removeFeed();
    return this;
  }
}
