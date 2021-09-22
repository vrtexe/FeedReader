package mk.finki.emt.feedreader.feeds.api.client;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.FeedSubscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


// UserFeedClient klasata se koristi za da se dobija soodvetni podatoci od drugiot modul
@Service
public class UserFeedsClient {

  private final RestTemplate restTemplate;
  private final String serverUrl;

  public UserFeedsClient(@Value("${app.user-service.url}") String serverUrl) {
    this.serverUrl = serverUrl;
    this.restTemplate = new RestTemplate();
    var requestFactory = new SimpleClientHttpRequestFactory();
    this.restTemplate.setRequestFactory(requestFactory);
  }

  private UriComponentsBuilder uri() {
    return UriComponentsBuilder.fromUriString(this.serverUrl);
  }

  public Collection<FeedSubscription> findSubscriptionsByUsername(String username) {
    try {
      return restTemplate
        .exchange(
          uri().path("/api/subscriptions/" + username).build().toUri(),
          HttpMethod.GET,
          null,
          new ParameterizedTypeReference<List<FeedSubscription>>() {}
        )
        .getBody();
    } catch (Exception e) {
      return Collections.emptySet();
    }
  }
}
