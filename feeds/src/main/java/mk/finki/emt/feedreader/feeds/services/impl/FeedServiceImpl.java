package mk.finki.emt.feedreader.feeds.services.impl;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import lombok.AllArgsConstructor;
import mk.finki.emt.feedreader.feeds.domain.exceptions.FeedSourceNotFoundException;
import mk.finki.emt.feedreader.feeds.domain.models.Article;
import mk.finki.emt.feedreader.feeds.domain.models.FeedSource;
import mk.finki.emt.feedreader.feeds.domain.models.FeedSourceId;
import mk.finki.emt.feedreader.feeds.domain.repository.FeedRepository;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.FeedSubscription;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.Link;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.LinkContentType;
import mk.finki.emt.feedreader.feeds.services.FeedService;
import mk.finki.emt.feedreader.feeds.services.forms.FeedSourceForm;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class FeedServiceImpl implements FeedService {

  private final FeedRepository repository;

  private final Validator validator;

  @Override
  public FeedSource addSource(FeedSourceForm form) throws Exception {
    Objects.requireNonNull(form, "form must not be null");
    Set<ConstraintViolation<FeedSourceForm>> constraintViolations = validator.validate(
      form
    );
    if (constraintViolations.size() > 0) {
      throw new ConstraintViolationException(
        "The source form is not valid",
        constraintViolations
      );
    }
    return repository.saveAndFlush(
      new FeedSource(new Link(form.getUrl(), LinkContentType.XML))
    );
  }

  @Override
  public void removeSource(String id) {
    repository.deleteById(
      repository
        .saveAndFlush(
          repository
            .findById(new FeedSourceId(id))
            .orElseThrow(FeedSourceNotFoundException::new)
            .clearArticles()
        )
        .getId()
    );
  }

  @Override
  public Set<Article> updateArticlesForSource(String id) throws Exception {
    FeedSource feedSource = repository
      .findById(new FeedSourceId(id))
      .orElseThrow(FeedSourceNotFoundException::new);
    Set<Article> articles = feedSource.updateArticles();
    repository.saveAndFlush(feedSource);
    return articles;
  }

  @Override
  public Set<Article> updateAllArticles() throws Exception {
    return repository
      .findAll()
      .stream()
      .filter(
        source -> {
          try {
            source.updateArticles();
          } catch (Exception e) {
            e.printStackTrace();
          }
          repository.saveAndFlush(source);
          return true;
        }
      )
      .flatMap(source -> source.getArticles().stream())
      .collect(Collectors.toSet());
  }

  @Override
  public FeedSource findById(String id) {
    return repository
      .findById(new FeedSourceId(id))
      .orElseThrow(FeedSourceNotFoundException::new);
  }

  @Override
  public Set<Article> getAllArticlesForSource(String id) {
    return repository
      .findById(new FeedSourceId(id))
      .orElseThrow(FeedSourceNotFoundException::new)
      .getArticles();
  }

  @Override
  public Set<Article> getAllArticles() {
    return repository
      .findAll()
      .stream()
      .map(source -> source.getArticles())
      .flatMap(articles -> articles.stream())
      .collect(Collectors.toSet());
  }

  @Override
  public Collection<FeedSource> getAll() {
    return repository.findAll();
  }

  @Override
  public Set<Article> getAllArticlesForUser(
    Collection<FeedSubscription> feedSubscriptions
  ) {
    return repository
      .findAll()
      .stream()
      .filter(
        feed ->
          feedSubscriptions
            .stream()
            .anyMatch(
              sub -> {
                System.out.println(sub.getFeedSourceId().getId());
                System.out.println(feed.getId().getId());
                return sub.getFeedSourceId().equals(feed.getId());
              }
            )
      )
      .flatMap(feed -> feed.getArticles().stream())
      .collect(Collectors.toSet());
  }

  @Override
  public void addSubscriberToFeed(String id) {
    repository.saveAndFlush(
      repository
        .findById(new FeedSourceId(id))
        .orElseThrow(FeedSourceNotFoundException::new)
        .addSubscriber()
    );
  }

  @Override
  public void removeSubscriberFromFeed(String id) {
    repository.saveAndFlush(
      repository
        .findById(new FeedSourceId(id))
        .orElseThrow(FeedSourceNotFoundException::new)
        .removeSubscriber()
    );
  }
}
