package mk.finki.emt.feedreader.feeds.domain.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import lombok.Getter;
import mk.finki.emt.feedreader.feeds.domain.exceptions.TypeOfFeedNotSupportedException;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.Author;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.FeedType;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.Image;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.Link;
import mk.finki.emt.feedreader.feeds.domain.valueObjects.LinkContentType;
import mk.finki.emt.feedreader.sharedkernel.domain.base.AbstractEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

//Tuka se naogja pogolemata logika od ovoj modul
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

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private Set<Article> articles;

  protected FeedSource() {
    super(FeedSourceId.randomId(FeedSourceId.class));
    this.link = null;
    this.articles = new HashSet<>();
    this.logo = null;
    this.description = "";
    this.title = "";
    this.copyright = "";
    this.subscribers = 0;
  }

  // Vo konstruktorot se proveruva dali e validen linkot, dokolku ne e nema da se
  // kretira objekt
  // tuku bi se frlilo exception
  public FeedSource(Link link) throws Exception {
    super(FeedSourceId.randomId(FeedSourceId.class));
    this.link = link;
    this.articles = new HashSet<>();

    XMLStreamReader reader = link.openXMLStream();
    try {
      FeedType feedType = null;
      if (reader.hasNext()) {
        reader.next();
        if (reader.isStartElement()) {
          if (reader.getLocalName().equals("rss")) {
            feedType = FeedType.RSS;
          } else if (reader.getLocalName().equals("feed")) {
            feedType = FeedType.ATOM;
          } else {
            throw new TypeOfFeedNotSupportedException();
          }
        }
      }
      this.populateFieldsJsoup(reader, feedType);
      this.updateArticlesStream(reader, feedType);
    } catch (Exception e) {
      throw e;
    } finally {
      reader.close();
    }
  }

  // Tuka se pravi chitanje na XLM i mapiranje
  private Set<Article> readRSSArticles(XMLStreamReader reader) throws XMLStreamException, ParseException, Exception {
    this.articles.removeAll(this.articles);
    while (reader.hasNext()) {
      if (reader.isStartElement()) {
        String title = "";
        Link link = null;
        String summary = "";
        String category = null;
        Instant updated = null;
        Author author = null;
        Image image = null;
        if (reader.getLocalName().equals("item")) {
          while (reader.hasNext()) {
            reader.next();
            if (reader.isEndElement() && reader.getLocalName().equals("item")) {
              break;
            }
            if (reader.isStartElement()) {
              switch (reader.getLocalName()) {
                case "title" -> {
                  title = reader.getElementText();
                }
                case "link" -> {
                  link = new Link(reader.getElementText(), LinkContentType.HTML);
                }
                case "description" -> {
                  summary = reader.getElementText();
                  summary = summary.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();

                }
                case "category" -> {
                  category = reader.getElementText();
                }
                case "pubDate" -> {
                  updated = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").parse(reader.getElementText())
                      .toInstant();
                }
                case "author" -> {
                  author = new Author(reader.getElementText());
                }
                case "creator" -> {
                  if (author == null) {
                    author = new Author(reader.getElementText());
                  }
                }
                case "thumbnail" -> {
                  image = new Image(reader.getAttributeValue(null, "url"), "Displayed on Article card");
                }
              }
            }
          }
          articles.add(new Article(title, link, summary, category, updated, author, image));
        }
      }
      reader.next();
    }
    return articles;
  }

  private Set<Article> readAtomArticles(XMLStreamReader reader) throws Exception {
    this.articles.removeAll(this.articles);
    while (reader.hasNext()) {
      if (reader.isStartElement()) {
        String title = "";
        Link link = null;
        String summary = "";
        String category = null;
        Instant updated = null;
        Author author = null;
        Image image = null;
        if (reader.getLocalName().equals("entry")) {
          while (reader.hasNext()) {
            if (reader.isEndElement() && reader.getLocalName().equals("entry")) {
              break;
            }
            reader.next();
            if (reader.isStartElement()) {
              switch (reader.getLocalName()) {
                case "title" -> {
                  title = reader.getElementText();
                }
                case "id" -> {
                  link = new Link(reader.getElementText(), LinkContentType.HTML);
                }
                case "summary" -> {
                  summary = reader.getElementText();
                  summary = summary.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();
                }
                case "content" -> {
                  if (summary.isEmpty()) {
                    String typeAttr = reader.getAttributeValue(null, "type");
                    if (typeAttr != null && typeAttr.equals("html")) {
                      String html = reader.getElementText();
                      Document doc = Jsoup.parse(html);
                      Elements elements = doc.getElementsByTag("img");
                      if (!elements.isEmpty()) {
                        image = new Image(elements.first().attr("src"), elements.first().attr("alt"));
                      }
                      elements = doc.getElementsByTag("p");
                      if (!elements.isEmpty()) {
                        summary = elements.first().text();
                      }
                    } else {
                      summary = reader.getElementText();
                      summary = summary.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();
                    }
                  }
                }
                case "category" -> {
                  category = reader.getElementText();
                }
                case "updated" -> {
                  String date = reader.getElementText();
                  updated = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                      .parse(date.substring(0, date.lastIndexOf(":"))
                          .concat(date.substring(date.lastIndexOf(":") + 1, date.length())))
                      .toInstant();
                }
                case "author" -> {
                  author = Author.createFromXmlStream(reader);
                }
              }
            }
          }
          articles.add(new Article(title, link, summary, category, updated, author, image));
        }
      }
      reader.next();
    }
    return articles;
  }

  public Set<Article> updateArticlesStream(XMLStreamReader reader, FeedType type) throws Exception {
    if (type == FeedType.RSS) {
      return readRSSArticles(reader);
    } else if (type == FeedType.ATOM) {
      return readAtomArticles(reader);
    } else {
      return null;
    }
  }

  public Set<Article> updateArticlesStream() {
    XMLStreamReader reader = link.openXMLStream();
    FeedType feedType = null;
    try {
      if (reader.hasNext()) {
        reader.next();
        if (reader.isStartElement()) {
          if (reader.getLocalName().equals("rss")) {
            feedType = FeedType.RSS;
          } else if (reader.getLocalName().equals("feed")) {
            feedType = FeedType.ATOM;
          } else {
            throw new TypeOfFeedNotSupportedException();
          }
        }
      }
      if (feedType == FeedType.RSS) {
        return readRSSArticles(reader);
      } else if (feedType == FeedType.ATOM) {
        return readAtomArticles(reader);
      } else {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        reader.close();
      } catch (XMLStreamException e) {
        e.printStackTrace();
      }
    }

    return null;
  }

  // vo ovoj metod se zgolemuva brojot na subs za odreden source
  public FeedSource addSubscriber() {
    this.subscribers++;
    return this;
  }

  // vo ovoj metod se zgolemuva brojot na subs za odreden source
  public FeedSource removeSubscriber() {
    this.subscribers--;
    return this;
  }

  private void populateFieldsJsoup(XMLStreamReader reader, FeedType type) throws Exception {
    if (type == FeedType.RSS) {
      populateForRss(reader);
    } else if (type == FeedType.ATOM) {
      populateForAtom(reader);
    }
  }

  private void populateForAtom(XMLStreamReader reader) throws Exception {
    while (reader.hasNext()) {
      reader.next();
      if (reader.isStartElement()) {
        if (reader.getLocalName().equals("entry")) {
          break;
        }
        switch (reader.getLocalName()) {
          case "title" -> {
            this.title = reader.getElementText();
          }
          case "icon" -> {
            this.logo = new Image(reader.getElementText(), "Logo of the feed provider");
          }
          case "logo" -> {
            if (this.logo == null) {
              this.logo = new Image(reader.getElementText(), "Logo of the feed provider");
            }
          }
          case "rights" -> {
            this.copyright = reader.getElementText();
          }
          case "subtitle" -> {
            this.description = reader.getElementText();
          }
        }

      }
    }
    this.subscribers = 0;
  }

  private void populateForRss(XMLStreamReader reader) throws XMLStreamException {
    while (reader.hasNext()) {
      reader.next();
      if (reader.isStartElement()) {
        if (reader.getLocalName().equals("item")) {
          break;
        }
        switch (reader.getLocalName()) {
          case "title" -> {
            this.title = reader.getElementText();
          }
          case "image" -> {
            String imageAlt = null;
            String imageSrc = null;
            while (reader.hasNext()) {
              reader.next();
              if (reader.isEndElement() && reader.getLocalName().equals("image")) {
                break;
              }
              if (reader.isStartElement()) {
                switch (reader.getLocalName()) {
                  case "title" -> {
                    imageAlt = reader.getElementText();
                  }
                  case "url" -> {
                    imageSrc = reader.getElementText();
                  }
                }
              }
            }
            this.logo = new Image(imageSrc, imageAlt);
          }
          case "copyright" -> {
            this.copyright = reader.getElementText();
          }
          case "description" -> {
            this.description = reader.getElementText();
          }
        }
      }
    }
    this.subscribers = 0;
  }
}
