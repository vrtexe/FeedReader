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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/subscriptions")
@AllArgsConstructor
public class UserResource {

  private final UserService service;

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
