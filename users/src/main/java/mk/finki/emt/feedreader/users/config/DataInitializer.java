package mk.finki.emt.feedreader.users.config;

import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import mk.finki.emt.feedreader.users.services.UserService;
import mk.finki.emt.feedreader.users.services.forms.SubscriptionForm;
import mk.finki.emt.feedreader.users.services.forms.UserRegistrationForm;
import org.springframework.stereotype.Component;

@Component
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
        new SubscriptionForm(username, "3620d7eb-2995-4b5a-9cc9-9616568a91bc")
      );
      service.SubscribeToFeed(
        new SubscriptionForm(username, "c83e67d5-7cde-4af0-b57d-d0d84ac51fbb")
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
        new SubscriptionForm(username, "3620d7eb-2995-4b5a-9cc9-9616568a91bc")
      );
      service.SubscribeToFeed(
        new SubscriptionForm(username, "c83e67d5-7cde-4af0-b57d-d0d84ac51fbb")
      );
      service.unsubscribeFromFeed(
        new SubscriptionForm(username, "c83e67d5-7cde-4af0-b57d-d0d84ac51fbb")
      );
    }
  }
}
