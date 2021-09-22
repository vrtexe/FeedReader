package mk.finki.emt.feedreader.feeds.api;

import java.util.Collection;
import lombok.AllArgsConstructor;
import mk.finki.emt.feedreader.feeds.api.client.UserFeedsClient;
import mk.finki.emt.feedreader.feeds.domain.models.Article;
import mk.finki.emt.feedreader.feeds.domain.models.FeedSource;
import mk.finki.emt.feedreader.feeds.services.FeedService;
import mk.finki.emt.feedreader.feeds.services.forms.FeedSourceForm;
import mk.finki.emt.feedreader.sharedkernel.domain.types.ActionCompleted;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/feeds")
@AllArgsConstructor
public class FeedResource {

  private final FeedService service;
  private final UserFeedsClient userClient;

  //So ovoj metod se dobivaat site FeedSources
  @GetMapping
  public ResponseEntity<Collection<FeedSource>> getAllFeedSources() {
    try {
      return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  //So ovoj metod se dobivaat site artikli za odreden source
  @GetMapping("/articles/{id}")
  public ResponseEntity<Collection<Article>> getAllArticlesForFeedSource(
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

  //So ovoj metod se dobivaat site artikli za soodveten korisnik
  @GetMapping("/user/{username}")
  public ResponseEntity<Collection<Article>> getAllArticlesForUser(
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

  //So ovoj metod se dobivaat site artikli
  @GetMapping("/articles")
  public ResponseEntity<Collection<Article>> getAllArticles() {
    try {
      return new ResponseEntity<>(service.getAllArticles(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  //So ovoj metod se dodava nov source vo bazata
  @PostMapping
  public ResponseEntity<FeedSource> addNewSource(
    @RequestBody(required = false) FeedSourceForm form
  ) {
    try {
      return new ResponseEntity<>(service.addSource(form), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  //So ovoj metod se dobivaat site FeedSources
  @DeleteMapping("/{id}")
  public ResponseEntity<ActionCompleted> removeExistingSource(
    @PathVariable String id
  ) {
    try {
      service.removeSource(id);
      return new ResponseEntity<>(new ActionCompleted(true), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
        new ActionCompleted(false),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }

  //So ovoj metod se pravi updejt na site artiklli vo odredeniot source
  @GetMapping("/articles/update/{id}")
  public ResponseEntity<Collection<Article>> updateArticlesForSource(
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

  //So ovoj metod se pravi updejt na site artiklli vo vo programata
  //Ova mozhi da potraj malku podolgo poradi chitanje na XML fajlot
  @GetMapping("/articles/update")
  public ResponseEntity<Collection<Article>> updateArticlesForAllSource(
    @PathVariable String id
  ) {
    try {
      return new ResponseEntity<>(service.updateAllArticles(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  //So ovoj metod se dobiva soodvetniot feedSource za ID
  @GetMapping("/{id}")
  public ResponseEntity<FeedSource> getFeedSourceById(@PathVariable String id) {
    try {
      return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
