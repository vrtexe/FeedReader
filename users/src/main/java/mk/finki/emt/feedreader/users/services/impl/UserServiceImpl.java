package mk.finki.emt.feedreader.users.services.impl;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import lombok.AllArgsConstructor;
import mk.finki.emt.feedreader.sharedkernel.domain.events.subscriptions.UserSubscribed;
import mk.finki.emt.feedreader.sharedkernel.domain.events.subscriptions.UserUnsubscribed;
import mk.finki.emt.feedreader.sharedkernel.infr.DomainEventPublisher;
import mk.finki.emt.feedreader.users.domain.exceptions.EmailAlreadyExistsException;
import mk.finki.emt.feedreader.users.domain.exceptions.LoginInfoNotValidException;
import mk.finki.emt.feedreader.users.domain.exceptions.UserNameAlreadyExistsException;
import mk.finki.emt.feedreader.users.domain.models.FeedSubscription;
import mk.finki.emt.feedreader.users.domain.models.User;
import mk.finki.emt.feedreader.users.domain.repository.UserRepository;
import mk.finki.emt.feedreader.users.domain.valueobjects.AuthInfo;
import mk.finki.emt.feedreader.users.domain.valueobjects.FeedSourceId;
import mk.finki.emt.feedreader.users.services.UserService;
import mk.finki.emt.feedreader.users.services.forms.SubscriptionForm;
import mk.finki.emt.feedreader.users.services.forms.UserLoginForm;
import mk.finki.emt.feedreader.users.services.forms.UserRegistrationForm;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository repository;

  private final Validator validator;

  private final PasswordEncoder passwordEncoder;

  private final DomainEventPublisher eventPublisher;

  @Override
  public String registerUser(UserRegistrationForm form) {
    Objects.requireNonNull(form, "form must not be null");
    Set<ConstraintViolation<UserRegistrationForm>> constraintViolations = validator.validate(
      form
    );
    if (constraintViolations.size() > 0) {
      throw new ConstraintViolationException(
        "The user credentials are not valid",
        constraintViolations
      );
    }

    if (
      repository
        .findFirstByAuthenticationUsername(form.getUsername())
        .isPresent()
    ) throw new UserNameAlreadyExistsException(form.getUsername());
    if (
      repository.findFirstByEmail(form.getEmail()).isPresent()
    ) throw new EmailAlreadyExistsException(form.getEmail());

    return repository
      .saveAndFlush(
        new User(
          form.getName(),
          form.getLastName(),
          new AuthInfo(
            form.getUsername(),
            passwordEncoder.encode(form.getPassword())
          ),
          form.getEmail()
        )
      )
      .getAuthentication()
      .getUsername();
  }

  @Override
  public String LoginUser(UserLoginForm form)
    throws UsernameNotFoundException, LoginInfoNotValidException {
    return repository
      .findFirstByAuthenticationUsername(form.getUsername())
      .orElseThrow(() -> new UsernameNotFoundException(form.getUsername()))
      .getAuthentication()
      .login(form.getUsername(), form.getPassword());
  }

  @Override
  public Set<FeedSubscription> getSubscriptions(String username) {
    return repository
      .findFirstByAuthenticationUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException(username))
      .getSubscriptions();
  }

  @Override
  public void subscribe(String username) {
    repository.saveAndFlush(
      repository
        .findFirstByAuthenticationUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(username))
        .renewSubscription()
    );
  }

  @Override
  public void unsubscribe(String username) {
    repository.saveAndFlush(
      repository
        .findFirstByAuthenticationUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(username))
        .unsubscribe()
    );
  }

  @Override
  public void SubscribeToFeed(SubscriptionForm form) {
    repository.saveAndFlush(
      repository
        .findFirstByAuthenticationUsername(form.getUsername())
        .orElseThrow(() -> new UsernameNotFoundException(form.getUsername()))
        .subscribeToFeed(new FeedSourceId(form.getFeedSourceId()))
    );
    eventPublisher.publish(new UserSubscribed(form.getFeedSourceId()));
  }

  @Override
  public void unsubscribeFromFeed(SubscriptionForm form) {
    repository.saveAndFlush(
      repository
        .findFirstByAuthenticationUsername(form.getUsername())
        .orElseThrow(() -> new UsernameNotFoundException(form.getUsername()))
        .unsubscribeFromFeed(new FeedSourceId(form.getFeedSourceId()))
    );
    eventPublisher.publish(new UserUnsubscribed(form.getFeedSourceId()));
  }

  @Override
  public Collection<User> getUsers() {
    return repository.findAll();
  }

  @Override
  public User getUserByUsername(String username) {
    return repository
      .findFirstByAuthenticationUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
