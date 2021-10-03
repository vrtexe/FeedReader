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
  /**
   * The method that registers a user and adds the user to the database
   * @param form the registration form
   * @return the username of the registered user
   */
  public String registerUser(UserRegistrationForm form);

  /**
   * The method that is used to authenticate the user with the provided inputs.
   * @param form the authentication form
   * @return the username for the authenticated user
   * @throws UsernameNotFoundException if a user can not be found with that username in the database
   * @throws LoginInfoNotValidException if the provided information was invalid.
   */
  public String LoginUser(UserLoginForm form)
    throws UsernameNotFoundException, LoginInfoNotValidException;

  /**
   * Finds the subscriptions from the database for the specified username.
   * @param username the username of the user
   * @return the collection of subscriptions
   */
  public Set<FeedSubscription> getSubscriptions(String username);

  /**
   * The method that subscribes the user to the service
   * @param username the username of the user
   */
  public void subscribe(String username);

  /**
   * The method that unsubscribes the user from the service
   * @param username the username of the user
   */
  public void unsubscribe(String username);

  /**
   * The method that subscribes the user to the feed source
   * @param form the subscription form consisting of the username and the feed source id
   */
  public void SubscribeToFeed(SubscriptionForm form);

  /**
   * The method that unsubscribes the user from the feed source
   * @param form the subscription form consisting of the username and the feed source id
   */
  public void unsubscribeFromFeed(SubscriptionForm form);

  /**
   * Finds the suer by the given username.
   * @param username the username of the user
   * @return the object containing the information for the user
   */
  public User getUserByUsername(String username);

  /**
   * TFinds all available users in the database
   * @return the collection of users retrieved from the database
   */
  public Collection<User> getUsers();
}
