package mk.finki.emt.feedreader.users.api;

import java.util.Collection;
import lombok.AllArgsConstructor;
import mk.finki.emt.feedreader.users.domain.models.FeedSubscription;
import mk.finki.emt.feedreader.users.services.UserService;
import mk.finki.emt.feedreader.users.services.forms.SubscriptionForm;
import mk.finki.emt.feedreader.users.services.forms.UserLoginForm;
import mk.finki.emt.feedreader.users.services.forms.UserRegistrationForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  @PostMapping("/register")
  public ResponseEntity<String> registerUser(
    @RequestBody UserRegistrationForm form
  ) {
    try {
      return new ResponseEntity<>(service.registerUser(form), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<String> authenticateUser(
    @RequestBody UserLoginForm form
  ) {
    try {
      return new ResponseEntity<>(service.LoginUser(form), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PatchMapping("/subscribe/{username}")
  public ResponseEntity<Boolean> subscribe(@PathVariable String username) {
    try {
      service.subscribe(username);
      return new ResponseEntity<>(true, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PatchMapping("/unsubscribe/{username}")
  public ResponseEntity<Boolean> unsubscribe(@PathVariable String username) {
    try {
      service.unsubscribe(username);
      return new ResponseEntity<>(true, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/feed/subscribe")
  public ResponseEntity<Boolean> subscribeToFeed(
    @RequestBody SubscriptionForm form
  ) {
    try {
      service.SubscribeToFeed(form);
      return new ResponseEntity<>(true, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/feed/unsubscribe")
  public ResponseEntity<Boolean> unsubscribeFromFeed(
    @RequestBody SubscriptionForm form
  ) {
    try {
      service.SubscribeToFeed(form);
      return new ResponseEntity<>(true, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
