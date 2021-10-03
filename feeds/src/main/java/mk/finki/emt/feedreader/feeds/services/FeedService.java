package mk.finki.emt.feedreader.feeds.services;

import java.util.Collection;
import java.util.Set;
import mk.finki.emt.feedreader.feeds.domain.models.Article;
import mk.finki.emt.feedreader.feeds.domain.models.FeedSource;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.FeedSubscription;
import mk.finki.emt.feedreader.feeds.services.forms.FeedSourceForm;
import org.jsoup.nodes.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

/**
 * The feed service that interacts withe re repository and the domain aggregate root to add, edit or remove content from the database.
 */
public interface FeedService {
  /**
   * This method adds a source to the database, together with the articles
   * @param form the object containing the data required to create a feed source
   * @return the created feed source
   * @throws Exception that occur when creating the feed source
   */
  FeedSource addSource(FeedSourceForm form) throws Exception;

  /**
   * This method removes a feed source for the given id
   * @param id the id of the feed source
   */
  void removeSource(String id);

  /**
   * This method calls for an update of all the articles in a given source and saves them in the database
   * @param id the id of teh feed source
   * @return the updated articles
   * @throws Exception
   */
  Set<Article> updateArticlesForSource(String id) throws Exception;

  /**
   * This method calls for an update of all the articles present in the database and saves them in the database
   * @return the updated articles
   * @throws Exception
   */
  Set<Article> updateAllArticles() throws Exception;

  /**
   * Finds the feed source requested by the id
   * @param id the id of the feed source
   * @return the feed source found in the database
   */
  FeedSource findById(String id);

  /**
   * Finds all articles in the database for every source present
   * @return the collection of articles
   */
  Set<Article> getAllArticles();

  /**
   * Finds all articles present in the database filtered by the subscriptions of the user
   * @param feedSubscriptions the subscriptions that the user is subscribed to
   * @return the collection of teh filtered articles
   */
  Set<Article> getAllArticlesForUser(
    Collection<FeedSubscription> feedSubscriptions
  );

  /**
   * Finds all the articles for the requested source
   * @param id the id of teh feed source
   * @return the collection of articles corresponding to the source
   */
  Set<Article> getAllArticlesForSource(String id);

  /**
   * This method controls adding subscribers to the counter of the feed source corresponding to the id
   * @param id the id of the feed source
   */
  void addSubscriberToFeed(String id);

  /**
   * This method controls subtracting subscribers from the counter of the feed source corresponding to the id
   * @param id the id of the feed source
   */
  void removeSubscriberFromFeed(String id);

  /**
   * The generic method used to find all teh feed sources present in the database
   * @return a collection of feed sources present in the database
   */
  Collection<FeedSource> getAll();

  /**
   * This method finds the article with the given id and extracts the HTML contents from its url.
   * @param articleId the id of teh article
   * @return Document object containing the HTML of the article page
   */
  Document getArticlePage(String articleId);

  /**
   * This method returns all the article with pagination
   * @param pageable the paging information (page number and size of the page to look for)
   * @return The slice object containing information on the pagination and the collection of articles
   */
  Slice<Article> getAllArticlesPageable(Pageable pageable);

  /**
   * This method returns all the article filtered by the sources the user is subscribed to with pagination
   * @param subscriptions the subscriptions of the user.
   * @param pageable the paging information (page number and size of the page to look for)
   * @return The slice object containing information on the pagination and the collection of articles
   */

  Slice<Article> getAllArticlesByListOfIdsPageable(
    Collection<FeedSubscription> subscriptions,
    Pageable pageable
  );

  /**
   * This method controls the subtraction of multiple sources for when the user unsubscribes from the service
   * @param ids
   */
  void removeSubscriberFromAllFeed(Collection<String> ids);
}
