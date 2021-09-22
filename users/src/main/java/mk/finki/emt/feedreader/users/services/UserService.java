package mk.finki.emt.feedreader.users.services;

import java.util.Collection;
import java.util.Set;
import mk.finki.emt.feedreader.users.domain.exceptions.LoginInfoNotValidException;
import mk.finki.emt.feedreader.users.domain.models.FeedSubscription;
import mk.finki.emt.feedreader.users.domain.models.User;
import mk.finki.emt.feedreader.users.services.forms.SubscriptionForm;
import mk.finki.emt.feedreader.users.services.forms.UserLoginForm;
import mk.finki.emt.feedreader.users.services.forms.UserRegistrationForm;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
  public String registerUser(UserRegistrationForm form);

  public String LoginUser(UserLoginForm form)
    throws UsernameNotFoundException, LoginInfoNotValidException;

  public Set<FeedSubscription> getSubscriptions(String username);

  public void subscribe(String username);

  public void unsubscribe(String username);

  public void SubscribeToFeed(SubscriptionForm form);

  public void unsubscribeFromFeed(SubscriptionForm form);

  public Collection<User> getUsers();
}
