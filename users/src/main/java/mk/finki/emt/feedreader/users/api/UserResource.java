package mk.finki.emt.feedreader.users.api;

import java.util.Collection;
import lombok.AllArgsConstructor;
import mk.finki.emt.feedreader.sharedkernel.domain.types.ActionCompleted;
import mk.finki.emt.feedreader.users.domain.models.FeedSubscription;
import mk.finki.emt.feedreader.users.domain.models.User;
import mk.finki.emt.feedreader.users.services.UserService;
import mk.finki.emt.feedreader.users.services.forms.SubscriptionForm;
import mk.finki.emt.feedreader.users.services.forms.UserLoginForm;
import mk.finki.emt.feedreader.users.services.forms.UserRegistrationForm;
import mk.finki.emt.feedreader.users.services.types.Username;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * * The API exposed by the users module, t contains all the needed API methods required by the front end or the other module.
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/subscriptions")
@AllArgsConstructor
public class UserResource {

  private final UserService service;

  /**
   * This method is called to get the information on all subscriptions for the corresponding user
   * @param username the username for the user
   * @return The response containing the collection of subscriptions in json format and the httpStatus(OK, PARTIAL_CONTENT, INTERNAL_SERVER_ERROR etc.)
   */
  @GetMapping("/{username}")
  public ResponseEntity<Collection<FeedSubscription>> getAllSubscriptionsForUser(
    @PathVariable String username
  ) {
    try {
      return new ResponseEntity<>(
        service.getSubscriptions(username),
        HttpStatus.OK
      );
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * This method gets all the information for the authenticated user.
   * @param username the username of the user
   * @return the user information
   */
  @GetMapping("/authenticated/{username}")
  public ResponseEntity<User> getLoggedInUser(@PathVariable String username) {
    try {
      return new ResponseEntity<>(
        service.getUserByUsername(username),
        HttpStatus.OK
      );
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * This method is used to register the user with the provided information.
   * @param form the form contains the corresponding information needed to register entered by the user
   * @return the username
   */
  @PostMapping("/register")
  public ResponseEntity<Username> registerUser(
    @RequestBody UserRegistrationForm form
  ) {
    try {
      return new ResponseEntity<>(
        new Username(service.registerUser(form)),
        HttpStatus.OK
      );
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * This method authenticates the user with the provided password and username
   * @param form contains the password and username entered by the user
   * @return the username if successful
   */
  @PostMapping("/login")
  public ResponseEntity<Username> authenticateUser(
    @RequestBody UserLoginForm form
  ) {
    try {
      return new ResponseEntity<>(
        new Username(service.LoginUser(form)),
        HttpStatus.OK
      );
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * This method subscribes the user to the service, giving him access to the features.
   * @param username the username of the user
   * @return An object containing whether the action was completed successfully
   */
  @PatchMapping("/subscribe/{username}")
  public ResponseEntity<ActionCompleted> subscribe(
    @PathVariable String username
  ) {
    try {
      service.subscribe(username);
      return new ResponseEntity<>(new ActionCompleted(true), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
        new ActionCompleted(false),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }

  /**
   * This method unsubscribes the user from the service, removing access to the provided features,
   * and emits an event to update values on the feeds module side.
   * @param username the username of the user
   * @return An object containing whether the action was completed successfully
   */
  @PatchMapping("/unsubscribe/{username}")
  public ResponseEntity<ActionCompleted> unsubscribe(
    @PathVariable String username
  ) {
    try {
      service.unsubscribe(username);
      return new ResponseEntity<>(new ActionCompleted(true), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
        new ActionCompleted(false),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }

  /**
   * This method subscribes the user to the specified feed source.
   * @param form containing the username and the feed source id
   * @return An object containing whether the action was completed successfully
   */
  @PostMapping("/feed/subscribe")
  public ResponseEntity<ActionCompleted> subscribeToFeed(
    @RequestBody SubscriptionForm form
  ) {
    try {
      service.SubscribeToFeed(form);
      return new ResponseEntity<>(new ActionCompleted(true), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
        new ActionCompleted(false),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }

  /**
   * This method unsubscribes the user from the specified feed source.
   * @param form containing the username and the feed source id
   * @return An object containing whether the action was completed successfully
   */
  @PostMapping("/feed/unsubscribe")
  public ResponseEntity<ActionCompleted> unsubscribeFromFeed(
    @RequestBody SubscriptionForm form
  ) {
    try {
      service.unsubscribeFromFeed(form);
      return new ResponseEntity<>(new ActionCompleted(true), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
        new ActionCompleted(false),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }
}
