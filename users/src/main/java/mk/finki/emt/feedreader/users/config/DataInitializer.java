package mk.finki.emt.feedreader.users.config;

import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import mk.finki.emt.feedreader.users.services.UserService;
import mk.finki.emt.feedreader.users.services.forms.SubscriptionForm;
import mk.finki.emt.feedreader.users.services.forms.UserRegistrationForm;
// import org.springframework.stereotype.Component;

// @Component
@AllArgsConstructor
public class DataInitializer {

  private final UserService service;

  @PostConstruct
  public void initData() throws Exception {
    if (service.getUsers().isEmpty()) {
      String username = service.registerUser(
        new UserRegistrationForm(
          "Vangel",
          "Trajkovski",
          "vangel21",
          "password21",
          "vangel@gmail.com"
        )
      );

      service.subscribe(username);
      service.SubscribeToFeed(
        new SubscriptionForm(username, "8868eef1-8044-4596-b565-121af40e03f8")
      );
      service.SubscribeToFeed(
        new SubscriptionForm(username, "03d2989d-3314-4690-ae54-7611abd03c3f")
      );

      username =
        service.registerUser(
          new UserRegistrationForm(
            "Vangel2",
            "Trajkovski2",
            "vangel12",
            "password12",
            "vangel12@gmail.com"
          )
        );
      service.subscribe(username);
      service.SubscribeToFeed(
        new SubscriptionForm(username, "8868eef1-8044-4596-b565-121af40e03f8")
      );
      service.SubscribeToFeed(
        new SubscriptionForm(username, "03d2989d-3314-4690-ae54-7611abd03c3f")
      );
      service.unsubscribeFromFeed(
        new SubscriptionForm(username, "8868eef1-8044-4596-b565-121af40e03f8")
      );
    }
  }
}
