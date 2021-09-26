package mk.finki.emt.feedreader.feeds.config;

import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import mk.finki.emt.feedreader.feeds.domain.models.FeedSource;
import mk.finki.emt.feedreader.feeds.services.FeedService;
import mk.finki.emt.feedreader.feeds.services.forms.FeedSourceForm;
import org.springframework.stereotype.Component;

//Klatata beshe koristena za postavuvanje na podatoci vo bazata
@Component
@AllArgsConstructor
public class DataInitializer {

  private final FeedService feedService;

  @PostConstruct
  public void initData() throws Exception {
    if (feedService.getAll().isEmpty()) {
      FeedSource feedSource = feedService.addSource(
        new FeedSourceForm("https://www.buzzfeed.com/world.xml")
      );
      feedService.getAllArticlesForSource(feedSource.getId().getId());
      feedSource =
        feedService.addSource(
          new FeedSourceForm("https://www.polygon.com/rss/index.xml")
        );
      feedService.updateArticlesForSource(feedSource.getId().getId());
      feedService.updateAllArticles();
      feedSource =
        feedService.addSource(
          new FeedSourceForm("https://www.pcgamer.com/rss/")
        );
      feedService.removeSource(feedSource.getId().getId());
    }
  }
}
//https://www.polygon.com/rss/index.xml
