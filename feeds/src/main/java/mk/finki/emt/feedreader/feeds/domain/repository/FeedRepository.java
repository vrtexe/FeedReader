package mk.finki.emt.feedreader.feeds.domain.repository;

import java.util.Collection;
import java.util.Optional;
import mk.finki.emt.feedreader.feeds.domain.models.Article;
import mk.finki.emt.feedreader.feeds.domain.models.FeedSource;
import mk.finki.emt.feedreader.feeds.domain.models.FeedSourceId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * The only jpa repository for the feeds module that communicates with the database directly,
 * and is used to update all aggregates, initialized with the aggregate root entity (FeedSource class)
 */
@Repository
public interface FeedRepository
  extends JpaRepository<FeedSource, FeedSourceId> {
  /**
   * Tries to find a feed source with the provided link.
   * @param string the url of the feed source
   * @return an optional object that can contain the FeedSource or a null value
   */
  Optional<FeedSource> findFirstByLinkUrl(String string);

  /**
   * Finds all the articles for all the sources, and gets only the page that was requested
   * @param pageable the page number and page size requested
   * @return the Slice object containing the information on the paging and the articles for the page requested.
   */
  @Query("select fs.articles from FeedSource fs")
  Slice<Article> findAllArticlesPageable(Pageable pageable);

  /**
   * Finds all the articles for only one feed source, and gets only the page that was requested
   * @param pageable the page number and page size requested
   * @return the Slice object containing the information on the paging and the articles for the page requested.
   */
  @Query("select fs.articles from FeedSource fs where fs.id in ?1")
  Slice<Article> findAllArticlesByListPageable(
    Collection<FeedSourceId> feedSourceIds,
    Pageable pageable
  );
}
