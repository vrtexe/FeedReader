package mk.finki.emt.feedreader.feeds.services;

import java.util.Collection;
import java.util.Set;
import mk.finki.emt.feedreader.feeds.domain.models.ArticleCard;
import mk.finki.emt.feedreader.feeds.domain.models.FeedSource;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.FeedSubscription;
import mk.finki.emt.feedreader.feeds.services.forms.FeedSourceForm;

public interface FeedService {
  FeedSource addSource(FeedSourceForm form) throws Exception;

  void removeSource(String id);

  Set<ArticleCard> updateArticlesForSource(String id) throws Exception;

  Set<ArticleCard> updateAllArticles() throws Exception;

  FeedSource findById(String id);

  Set<ArticleCard> getAllArticles();

  Set<ArticleCard> getAllArticlesForUser(
    Collection<FeedSubscription> feedSubscriptions
  );

  Set<ArticleCard> getAllArticlesForSource(String id);

  void addSubscriberToFeed(String id);

  void removeSubscriberFromFeed(String id);

  Collection<FeedSource> getAll();
}
