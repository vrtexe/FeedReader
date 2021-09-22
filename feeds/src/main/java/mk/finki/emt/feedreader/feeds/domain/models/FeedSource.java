package mk.finki.emt.feedreader.feeds.domain.models;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.Author;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.FeedType;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.Image;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.Link;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.LinkContentType;
import mk.finki.emt.feedreader.sharedkernel.domain.base.AbstractEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Getter
@Entity
@Table(name = "feed_sources")
public class FeedSource extends AbstractEntity<FeedSourceId> {

  private String title;

  private Link link;

  private Image logo;

  private String description;

  private String copyright;

  private Integer subscribers;

  @OneToMany(
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.EAGER
  )
  private Set<Article> articles;

  public FeedSource() {
    super(FeedSourceId.randomId(FeedSourceId.class));
    this.link = null;
    this.articles = new HashSet<>();
    this.logo = null;
    this.description = "";
    this.title = "";
    this.copyright = "";
    this.subscribers = 0;
  }

  public FeedSource(Link link) throws Exception {
    super(FeedSourceId.randomId(FeedSourceId.class));
    this.link = link;

    Document document = link.ReadXML();
    NodeList feeds = document.getElementsByTagName("feed");

    FeedType type = FeedType.ATOM;
    if (feeds.getLength() == 0) {
      feeds = document.getElementsByTagName("rss");
      type = FeedType.RSS;
      if (feeds.getLength() == 0) {
        throw new Exception("elements not found");
      }
    }

    articles = new HashSet<>();

    if (type == FeedType.ATOM) {
      Element element = (Element) feeds.item(0);

      this.logo = null;
      NodeList elementChildren = element.getElementsByTagName("logo");
      if (elementChildren.getLength() != 0) {
        logo =
          new Image(
            elementChildren.item(0).getTextContent(),
            "Logo of the feed provider"
          );
      }
      elementChildren = element.getElementsByTagName("icon");
      if (elementChildren.getLength() != 0) {
        logo =
          new Image(
            elementChildren.item(0).getTextContent(),
            "Logo of the feed provider"
          );
      }
    } else if (type == FeedType.RSS) {
      Element element = (Element) document
        .getElementsByTagName("channel")
        .item(0);

      this.title =
        element.getElementsByTagName("title").item(0).getTextContent();

      this.logo = null;
      NodeList elementChildren = element.getElementsByTagName("image");
      if (elementChildren.getLength() != 0) {
        Element imageElement = (Element) elementChildren.item(0);

        logo =
          new Image(
            imageElement.getElementsByTagName("url").item(0).getTextContent(),
            imageElement.getElementsByTagName("title").item(0).getTextContent()
          );
      }

      this.description =
        element.getElementsByTagName("description").item(0).getTextContent();

      this.copyright = null;
      elementChildren = element.getElementsByTagName("copyright");
      if (elementChildren.getLength() != 0) {
        this.copyright = elementChildren.item(0).getTextContent();
      }
    }

    this.subscribers = 0;

    updateArticles();
  }

  public Set<Article> updateArticles() throws Exception {
    Document document = link.ReadXML();
    NodeList feeds = document.getElementsByTagName("feed");

    FeedType type = FeedType.ATOM;
    if (feeds.getLength() == 0) {
      feeds = document.getElementsByTagName("rss");
      type = FeedType.RSS;
      if (feeds.getLength() == 0) {
        throw new Exception("elements not found");
      }
    }

    this.articles.removeAll(this.articles);

    if (type == FeedType.ATOM) {
      feeds = document.getElementsByTagName("entry");
      for (
        int i = 0;
        i < (feeds.getLength() < 3 ? feeds.getLength() : 3);
        i++
      ) {
        Node node = feeds.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          Element element = (Element) node;

          String title = element
            .getElementsByTagName("title")
            .item(0)
            .getTextContent();

          Link link = new Link(
            element.getElementsByTagName("id").item(0).getTextContent(),
            LinkContentType.HTML
          );

          String summary = null;
          NodeList elementChildren = element.getElementsByTagName("summary");
          if (elementChildren.getLength() != 0) {
            summary = elementChildren.item(0).getTextContent();
          } else {
            elementChildren = element.getElementsByTagName("content");
            if (elementChildren.getLength() != 0) {
              Element contentElement = (Element) elementChildren.item(0);
              NodeList contentNodes = contentElement.getElementsByTagName("p");
              if (contentNodes.getLength() != 0) {
                summary = contentNodes.item(0).getTextContent();
              } else {
                summary = elementChildren.item(0).getTextContent();
              }
            }
          }

          summary = summary.trim();

          while (summary.contains("<")) {
            summary =
              summary.substring(0, summary.indexOf("<")) +
              summary.substring(summary.indexOf(">") + 1, summary.length() - 1);
          }

          summary = summary.trim();

          if (summary.length() > 100) {
            summary = summary.substring(0, 100);
          }

          String category = null;
          elementChildren = element.getElementsByTagName("category");
          if (elementChildren.getLength() != 0) {
            category = elementChildren.item(0).getTextContent();
          }

          String date = element
            .getElementsByTagName("updated")
            .item(0)
            .getTextContent();

          Instant updated = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .parse(
              date
                .substring(0, date.lastIndexOf(":"))
                .concat(
                  date.substring(date.lastIndexOf(":") + 1, date.length())
                )
            )
            .toInstant();

          Author author = null;
          elementChildren = element.getElementsByTagName("author");
          if (elementChildren.getLength() != 0) {
            if (elementChildren.item(0).getNodeType() == Node.ELEMENT_NODE) {
              Element authorElement = (Element) elementChildren.item(0);

              String name = authorElement
                .getElementsByTagName("name")
                .item(0)
                .getTextContent();

              String uri = null;
              NodeList authorChildren = authorElement.getElementsByTagName(
                "uri"
              );
              if (authorChildren.getLength() != 0) {
                uri = authorChildren.item(0).getTextContent();
              }

              String email = null;
              authorChildren = authorElement.getElementsByTagName("email");
              if (authorChildren.getLength() != 0) {
                email = authorChildren.item(0).getTextContent();
              }

              author = new Author(name, email, uri);
            }
          }

          String image = link.ReadHtmlAndReturnFirstFoundImageUrl();

          articles.add(
            new Article(
              title,
              link,
              summary,
              category,
              updated,
              author,
              image
            )
          );
        }
      }
    } else if (type == FeedType.RSS) {
      feeds = document.getElementsByTagName("item");
      for (
        int i = 0;
        i < (feeds.getLength() < 3 ? feeds.getLength() : 3);
        i++
      ) {
        Node node = feeds.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          Element element = (Element) node;

          String title = element
            .getElementsByTagName("title")
            .item(0)
            .getTextContent();

          Link link = new Link(
            element.getElementsByTagName("link").item(0).getTextContent(),
            LinkContentType.HTML
          );

          String summary = element
            .getElementsByTagName("description")
            .item(0)
            .getTextContent();

          while (summary.contains("<")) {
            summary =
              summary.substring(0, summary.indexOf("<")) +
              summary.substring(summary.indexOf(">") + 1, summary.length() - 1);
          }

          if (summary.length() > 100) {
            summary = summary.substring(0, 100);
          }

          String category = null;
          NodeList elementChildren = element.getElementsByTagName("category");
          if (elementChildren.getLength() != 0) {
            category = elementChildren.item(0).getTextContent();
          }

          Instant updated = null;
          elementChildren = element.getElementsByTagName("pubDate");
          if (elementChildren.getLength() != 0) {
            updated =
              new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
                .parse(elementChildren.item(0).getTextContent())
                .toInstant();
          }

          Author author = null;
          elementChildren = element.getElementsByTagName("author");
          if (elementChildren.getLength() != 0) {
            author =
              new Author(null, elementChildren.item(0).getTextContent(), null);
          }

          String image = link.ReadHtmlAndReturnFirstFoundImageUrl();

          articles.add(
            new Article(
              title,
              link,
              summary,
              category,
              updated,
              author,
              image
            )
          );
        }
      }
    }
    return articles;
  }

  public FeedSource clearArticles() {
    // this.articleDROP TABLE article;s.removeAll(this.articles);
    return this;
  }

  public FeedSource addSubscriber() {
    this.subscribers++;
    return this;
  }

  public FeedSource removeSubscriber() {
    this.subscribers--;
    return this;
  }
}
