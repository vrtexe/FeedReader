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

public interface FeedService {
  FeedSource addSource(FeedSourceForm form) throws Exception;

  void removeSource(String id);

  Set<Article> updateArticlesForSource(String id) throws Exception;

  Set<Article> updateAllArticles() throws Exception;

  FeedSource findById(String id);

  Set<Article> getAllArticles();

  Set<Article> getAllArticlesForUser(
    Collection<FeedSubscription> feedSubscriptions
  );

  Set<Article> getAllArticlesForSource(String id);

  void addSubscriberToFeed(String id);

  void removeSubscriberFromFeed(String id);

  Collection<FeedSource> getAll();

  Document getArticlePage(String articleId);

  Slice<Article> getAllArticlesPageable(Pageable pageable);

  Slice<Article> getAllArticlesByListOfIdsPageable(
    Collection<FeedSubscription> subscriptions,
    Pageable pageable
  );
}
