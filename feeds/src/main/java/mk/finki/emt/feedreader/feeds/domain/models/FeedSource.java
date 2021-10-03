package mk.finki.emt.feedreader.feeds.domain.models;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
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


/**
 * This class is the root aggregate root for the current module, this is where most of the logic is.
 */
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

  /**
   * The constructor accepts only one parameter, the url of the feed source, the other properties can be extracted from the url,
   * the type of feed can be inferred from the contents of the XML, and the corresponding function is called to populate the other fields,
   * after populating the feed source information, the articles are also added initially.
   * @param link the url of the source
   * @throws Exception an exception can be thrown when opening the XML stream if the link is invalid, as well as when reading form the stream.
   */
  public FeedSource(Link link) throws Exception {
    super(FeedSourceId.randomId(FeedSourceId.class));
    this.link = link;
    this.articles = new HashSet<>();

    XMLStreamReader reader = link.openXMLStream();
    try {
      FeedType feedType = null;
      System.out.println(reader);
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
      this.populateFields(reader, feedType);
      this.updateArticlesStream(reader, feedType);
    } catch (Exception e) {
      throw e;
    } finally {
      reader.close();
    }
  }

  /**
   * This is where the rss feed articles are added to the collection of articles that is mapped to the article entity,
   * the article properties are extracted using the StAX parser, based on the RSS 2.0 specification, 
   * if the data can not be extracted the field is left empty 
   * @param reader the opened stream to continue reading from
   * @return the full article collection that was just added.
   * @throws Exception the exception thrown when reading from the stream of when parsing a value.
   */
  private Set<Article> readRSSArticles(XMLStreamReader reader) throws Exception {
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
                  if (title.isBlank()) {
                    title = reader.getElementText();
                  }
                }
                case "link" -> {
                  if (link == null) {
                    link = new Link(reader.getElementText(), LinkContentType.HTML);
                  }
                }
                case "description" -> {
                  String html = reader.getElementText();
                  Document doc = Jsoup.parse(html);
                  Elements elements = doc.getElementsByTag("img");
                  if (!elements.isEmpty()) {
                    image = new Image(elements.first().attr("src"), elements.first().attr("alt"));
                  }
                  elements = doc.getElementsByTag("p");
                  if (!elements.isEmpty() && summary.isEmpty()) {
                    summary = elements.first().text();
                    summary = summary.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();
                    if (summary.length() > 500) {
                      summary = Arrays.asList(summary.split("\\.")).stream().limit(2)
                          .map(sentence -> sentence.concat(".")).reduce("", String::concat);
                    }
                  }
                  if (summary.isEmpty()) {
                    summary = html;
                    summary = summary.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();
                    if (summary.length() > 500) {
                      summary = Arrays.asList(summary.split("\\.")).stream().limit(2)
                          .map(sentence -> sentence.concat(".")).reduce("", String::concat);
                    }
                  }
                }
                case "encoded" -> {
                  String html = reader.getElementText();
                  Document doc = Jsoup.parse(html);
                  Elements elements = doc.getElementsByTag("img");
                  if (!elements.isEmpty()) {
                    image = new Image(elements.first().attr("src"), elements.first().attr("alt"));
                  }
                  elements = doc.getElementsByTag("p");
                  if (!elements.isEmpty() && summary.isEmpty()) {
                    summary = elements.first().text();
                    summary = summary.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();
                  }
                  if (summary.isEmpty()) {
                    summary = html;
                    summary = summary.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();
                  }
                  if (summary.length() > 500) {
                    summary = Arrays.asList(summary.split("\\.")).stream().limit(2)
                        .map(sentence -> sentence.concat(".")).reduce("", String::concat);
                  }
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
                case "thumbnails" -> {
                  if (image == null) {
                    image = new Image(reader.getAttributeValue(null, "url"), "Displayed on Article card");
                  }
                }
                case "image" -> {
                  if (image == null) {
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
                    image = new Image(imageSrc, imageAlt);
                  }
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

   /**
   * This is where the atom feed articles are added to the collection of articles that is mapped to the article entity,
   * the article properties are extracted using the StAX parser, based on the Atom 1.0 standardization, 
   * if the data can not be extracted the field is left empty 
   * @param reader the opened stream to continue reading from
   * @return the full article collection that was just added.
   * @throws Exception the exception thrown when reading from the stream of when parsing a value.
   */
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

  /**
   * A function that decides which method to call based on the type of feed standard provided. 
   * @param reader the stream to pass to the method used for adding the articles to the database
   * @param type the type of feed that is being parsed
   * @return the articles that were parsed and returned from the corresponding method
   * @throws Exception the exceptions thrown by the corresponding methods
   */
  public Set<Article> updateArticlesStream(XMLStreamReader reader, FeedType type) throws Exception {
    if (type == FeedType.RSS) {
      return readRSSArticles(reader);
    } else if (type == FeedType.ATOM) {
      return readAtomArticles(reader);
    } else {
      return null;
    }
  }

  /**
   * This method updates the articles bu also opens the stream to the XML and infers the type  
   * @return the articles that were parsed and returned from the corresponding method
   * @throws Exception the exceptions thrown by the corresponding methods
   */
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

  /**
   * The method used to add to the value containing the current number of subscribers.
   * @return the object that called the method, for better method chaining
   */
  public FeedSource addSubscriber() {
    this.subscribers++;
    return this;
  }

  /**
   * The method used to subtract from the value containing the current number of subscribers.
   * @return the object that called the method, for better method chaining
   */
  public FeedSource removeSubscriber() {
    this.subscribers--;
    return this;
  }

  /**
   * The method is used to call the populate method corresponding to the type provided and return its result 
   * @param reader the opened stream to pass to the corresponding method 
   * @param type the type of feed
   * @throws Exception that can be thrown by the methods.
   */
  private void populateFields(XMLStreamReader reader, FeedType type) throws Exception {
    if (type == FeedType.RSS) {
      populateForRss(reader);
    } else if (type == FeedType.ATOM) {
      populateForAtom(reader);
    }
  }

  /**
   * The method populates the fields of the feed source when its written in the Atom 1.0 standard by parsing the XML, until it reaches the first article
   * @param reader the opened stream to read from and parse
   * @throws Exception the exceptions thrown when reading the XML stream or when parsing an invalid type 
   */
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

  /**
   * The method populates the fields of the feed source when its written in the RSS 2.0 standard by parsing the XML, until it reaches the first article
   * @param reader the opened stream to read from and parse
   * @throws XMLStreamException the exceptions thrown when reading the XML stream or when parsing an invalid type
   */
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
