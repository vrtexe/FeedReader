package mk.finki.emt.feedreader.feeds.api;

import java.util.Collection;
import lombok.AllArgsConstructor;
import mk.finki.emt.feedreader.feeds.api.client.UserFeedsClient;
import mk.finki.emt.feedreader.feeds.domain.models.Article;
import mk.finki.emt.feedreader.feeds.domain.models.FeedSource;
import mk.finki.emt.feedreader.feeds.services.FeedService;
import mk.finki.emt.feedreader.feeds.services.forms.FeedSourceForm;
import mk.finki.emt.feedreader.feeds.services.types.HtmlDocument;
import mk.finki.emt.feedreader.sharedkernel.domain.types.ActionCompleted;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * *The API exposed by the feeds module, it contains all the needed API methods required by the front end.
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/feeds")
@AllArgsConstructor
public class FeedResource {

  private final FeedService service;
  private final UserFeedsClient userClient;

  /**
   * This method just requests all the feed sources in the database.
   * @return  The response containing the collection of feed sources in json format and the httpStatus(OK, PARTIAL_CONTENT, INTERNAL_SERVER_ERROR etc.)
   */
  @GetMapping
  public ResponseEntity<Collection<FeedSource>> getAllFeedSources() {
    try {
      return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * This method returns all the articles for a feed source specified by the id property
   * @param id the id of a feed source
   * @return The response containing a collection of articles for a feed source in json format and the httpStatus(OK, PARTIAL_CONTENT, INTERNAL_SERVER_ERROR etc.)
   */
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

  /**
   * This method returns the html for a specified page that is later used to preview it in the frontend
   * @param articleId the id of the article to be shown
   * @return The response containing the html as string mapped to an object wrapper in json format and the httpStatus(OK, PARTIAL_CONTENT, INTERNAL_SERVER_ERROR etc.)
   */
  @GetMapping("/article/{articleId}")
  public ResponseEntity<HtmlDocument> getArticlePage(
    @PathVariable String articleId
  ) {
    try {
      return new ResponseEntity<>(
        new HtmlDocument(service.getArticlePage(articleId).html()),
        HttpStatus.OK
      );
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * This method gets all the feeds the user has subscribed to, by calling the exposed API from the user module
   * @param username the username of the authenticated in user
   * @return The response containing the collection of articles filtered by user in json format and the httpStatus(OK, PARTIAL_CONTENT, INTERNAL_SERVER_ERROR etc.)
   */
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

  /**
   * The method used to get all articles for every single feed source entered in the database.
   * @return The response containing the collection of all articles present in the database in json format and the httpStatus(OK, PARTIAL_CONTENT, INTERNAL_SERVER_ERROR etc.)
   */
  @GetMapping("/articles")
  public ResponseEntity<Collection<Article>> getAllArticles() {
    try {
      return new ResponseEntity<>(service.getAllArticles(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * The method used to get all articles for every single feed source entered in the database, but partially page by page.
   * @param page the page number to send to the front end
   * @param size the size of the page
   * @return The response containing the collection of articles
   *  for a given page with the corresponding size and information about the page
   *  in json format and the httpStatus(OK, PARTIAL_CONTENT, INTERNAL_SERVER_ERROR etc.)
   *
   */
  @GetMapping(path = "/pageable", params = { "page", "size" })
  public ResponseEntity<Slice<Article>> getAllArticlesPageable(
    @RequestParam Integer page,
    @RequestParam Integer size
  ) {
    try {
      Pageable pageable = PageRequest.of(page, size);
      return new ResponseEntity<>(
        service.getAllArticlesPageable(pageable),
        HttpStatus.PARTIAL_CONTENT
      );
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * The method used to get all articles for the feed sources the user is subscribed to, but partially page by page.
   * @param username the username of the authenticated user
   * @param page the page number to send to the front end
   * @param size the size of the page
   * @return The response containing the collection of articles
   *  for a given page with the corresponding size and information about the page
   *  in json format and the httpStatus(OK, PARTIAL_CONTENT, INTERNAL_SERVER_ERROR etc.)
   *
   */
  @GetMapping(path = "/user/{username}/pageable", params = { "page", "size" })
  public ResponseEntity<Slice<Article>> getAllArticlesByUserPageable(
    @PathVariable String username,
    @RequestParam Integer page,
    @RequestParam Integer size
  ) {
    try {
      Pageable pageable = PageRequest.of(page, size);
      return new ResponseEntity<>(
        service.getAllArticlesByListOfIdsPageable(
          userClient.findSubscriptionsByUsername(username),
          pageable
        ),
        HttpStatus.PARTIAL_CONTENT
      );
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * The feed source url to add to the database, the feed source data is extracted from the url
   * @param form The form only contains the url for the feed source
   * @return The response containing the newly created feed source
   * and the httpStatus(OK, PARTIAL_CONTENT, INTERNAL_SERVER_ERROR etc.)
   */
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

  /**
   * This method removes the feed source corresponding to the given id
   * @param id the id of the feed source
   * @return The response containing a boolean wrapper if the action is completed
   * and the httpStatus(OK, PARTIAL_CONTENT, INTERNAL_SERVER_ERROR etc.)
   */
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

  /**
   * This method updates the articles of all feed sources in the database by parsing link to the XML.
   * ! This method can take a while if there are a lot of feed sources
   * @return The response containing all the updated articles
   * and the httpStatus(OK, PARTIAL_CONTENT, INTERNAL_SERVER_ERROR etc.)
   */
  @GetMapping("/articles/update")
  public ResponseEntity<Collection<Article>> updateArticlesForAllSources() {
    try {
      return new ResponseEntity<>(service.updateAllArticles(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * This method updates only one source by parsing the XML from the url
   * @param id the id of the feed source
   * @return The response containing only the updated articles
   * and the httpStatus(OK, PARTIAL_CONTENT, INTERNAL_SERVER_ERROR etc.)
   */
  @GetMapping("/article/update/{id}")
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

  //So ovoj metod se dobiva soodvetniot feedSource za ID
  /**
   * This method is used to get the information for a single feed source including all the articles
   * @param id the id of the feed source
   * @return The response containing only feed source requested with the id
   * and the httpStatus(OK, PARTIAL_CONTENT, INTERNAL_SERVER_ERROR etc.)
   */
  @GetMapping("/{id}")
  public ResponseEntity<FeedSource> getFeedSourceById(@PathVariable String id) {
    try {
      return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
