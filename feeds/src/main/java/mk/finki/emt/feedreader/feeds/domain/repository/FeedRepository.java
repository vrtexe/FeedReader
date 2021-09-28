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

@Repository
public interface FeedRepository
  extends JpaRepository<FeedSource, FeedSourceId> {
  Optional<FeedSource> findFirstByLinkUrl(String string);

  @Query("select fs.articles from FeedSource fs")
  Slice<Article> findAllArticlesPageable(Pageable pageable);

  @Query("select fs.articles from FeedSource fs where fs.id in ?1")
  Slice<Article> findAllArticlesByListPageable(
    Collection<FeedSourceId> feedSourceIds,
    Pageable pageable
  );
}
