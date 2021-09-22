package mk.finki.emt.feedreader.feeds.api;

import java.util.Collection;
import lombok.AllArgsConstructor;
import mk.finki.emt.feedreader.feeds.api.client.UserFeedsClient;
import mk.finki.emt.feedreader.feeds.domain.models.ArticleCard;
import mk.finki.emt.feedreader.feeds.domain.models.FeedSource;
import mk.finki.emt.feedreader.feeds.services.FeedService;
import mk.finki.emt.feedreader.feeds.services.forms.FeedSourceForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feeds")
@AllArgsConstructor
public class FeedResource {

  private final FeedService service;
  private final UserFeedsClient userClient;

  @GetMapping
  public ResponseEntity<Collection<FeedSource>> getAllFeedSources() {
    try {
      return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/articles/{id}")
  public ResponseEntity<Collection<ArticleCard>> getAllArticlesForFeedSource(
    @PathVariable String id
  ) {
    try {
      return new ResponseEntity<>(
        service.getAllArticlesForSource(id),
        HttpStatus.OK
      );
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/user/{username}")
  public ResponseEntity<Collection<ArticleCard>> getAllArticlesForUser(
    @PathVariable String username
  ) {
    try {
      return new ResponseEntity<>(
        service.getAllArticlesForUser(
          userClient.findSubscriptionsByUsername(username)
        ),
        HttpStatus.OK
      );
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/articles")
  public ResponseEntity<Collection<ArticleCard>> getAllArticles() {
    try {
      return new ResponseEntity<>(service.getAllArticles(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping
  public ResponseEntity<FeedSource> addNewSource(
    @RequestBody FeedSourceForm form
  ) {
    try {
      return new ResponseEntity<>(service.addSource(form), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Boolean> removeExistingSource(@PathVariable String id) {
    try {
      service.removeSource(id);
      return new ResponseEntity<>(true, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/articles/update/{id}")
  public ResponseEntity<Collection<ArticleCard>> updateArticlesForSource(
    @PathVariable String id
  ) {
    try {
      return new ResponseEntity<>(
        service.updateArticlesForSource(id),
        HttpStatus.OK
      );
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/articles/update")
  public ResponseEntity<Collection<ArticleCard>> updateArticlesForAllSource(
    @PathVariable String id
  ) {
    try {
      return new ResponseEntity<>(service.updateAllArticles(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<FeedSource> getFeedSourceById(@PathVariable String id) {
    try {
      return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
