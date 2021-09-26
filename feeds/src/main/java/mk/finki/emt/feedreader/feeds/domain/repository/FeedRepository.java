package mk.finki.emt.feedreader.feeds.domain.repository;

import java.util.Optional;
import mk.finki.emt.feedreader.feeds.domain.models.FeedSource;
import mk.finki.emt.feedreader.feeds.domain.models.FeedSourceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository
  extends JpaRepository<FeedSource, FeedSourceId> {
  Optional<FeedSource> findFirstByLinkUrl(String string);
}
