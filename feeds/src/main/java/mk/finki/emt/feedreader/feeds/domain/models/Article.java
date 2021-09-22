package mk.finki.emt.feedreader.feeds.domain.models;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.Author;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.Link;
import mk.finki.emt.feedreader.sharedkernel.domain.base.AbstractEntity;

//Artiklot e chist entitet bez logika pozadi nego
@Getter
@Entity
@Table(name = "article")
public class Article extends AbstractEntity<ArticleId> {

  private String title;

  private Link link;

  private String summary;

  private String category;

  private Instant published;

  private Author author;

  private String image;

  protected Article() {
    super(ArticleId.randomId(ArticleId.class));
    this.title = null;
    this.link = null;
    this.summary = null;
    this.category = null;
    this.published = null;
    this.author = null;
    this.image = null;
  }

  public Article(
    String title,
    Link link,
    String summary,
    String category,
    Instant published,
    Author author,
    String image
  ) {
    super(ArticleId.randomId(ArticleId.class));
    this.title = title;
    this.link = link;
    this.summary = summary;
    this.category = category;
    this.published = published;
    this.author = author;
    this.image = image;
  }
}
