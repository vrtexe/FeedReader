package mk.finki.emt.feedreader.feeds.domain.valueObjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import lombok.Getter;
import lombok.NonNull;
import mk.finki.emt.feedreader.feeds.domain.exceptions.UrlNotValidException;
import mk.finki.emt.feedreader.sharedkernel.domain.base.ValueObject;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.MediaType;

/**
 * The link class contains the url of the feed source or the article
 * and the logic concerning the interaction with the url
 */
@Getter
@Embeddable
public class Link implements ValueObject {

  private final String url;

  /**
   * A factory used to instantiate a new stream to read from.
   * not included in the database
   */
  @Transient
  @JsonIgnore
  private static XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

  /**
   * The factory used to build the document to read and parse from in memory,
   * not used anymore (deprecated)
   * ! It was too slow
   */
  @Transient
  @JsonIgnore
  private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

  /**
   * The input stream kept so that it can be easily closed when finished.
   */
  @Transient
  @JsonIgnore
  private InputStream xmlInputStream;

  /**
   * The stream reader kept so it can be closed easier.
   */
  @Transient
  @JsonIgnore
  private XMLStreamReader reader;

  protected Link() {
    this.url = "";
  }

  /**
   * Only instantiates the object, before it called a validation method that was too slow.
   * @param url the url of the feed source or article kept here.
   * @param contentType the content type provided (HTML or XML)
   */
  public Link(@NonNull String url, @NonNull LinkContentType contentType) {
    this.url = url;
    // this.url = this.validateLink(url, contentType);
  }

  /**
   * A method to validate the url if it opens, is accessible or if its the correct content type.
   * ! Slowed down the process of adding feed sources or articles tremendously.
   * @param url the url of feed source or article
   * @param contentType the type of content (HTML or XML)
   * @return the url of the feed source or article
   * @throws IOException when there is a problem when opening the connection and reading from an empty stream
   * @throws UrlNotValidException when arriving to the conclusion that the url is not valid
   */
  public String validateLink(
    @NonNull String url,
    @NonNull LinkContentType contentType
  )
    throws IOException, UrlNotValidException {
    URL Url = new URL(url);
    HttpURLConnection urlConnection = (HttpURLConnection) Url.openConnection();
    if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
      throw new UrlNotValidException();
    }
    if (contentType == LinkContentType.HTML) {
      if (
        urlConnection
          .getContentType()
          .split(";")[0].trim()
          .equals(MediaType.TEXT_HTML_VALUE)
      ) {
        return url;
      } else {
        throw new UrlNotValidException(
          urlConnection.getContentType().split(";")[0].trim()
        );
      }
    } else if (contentType == LinkContentType.XML) {
      if (
        urlConnection
          .getContentType()
          .split(";")[0].trim()
          .equals(MediaType.APPLICATION_XML_VALUE) ||
        urlConnection
          .getContentType()
          .split(";")[0].trim()
          .equals(MediaType.APPLICATION_RSS_XML_VALUE) ||
        urlConnection
          .getContentType()
          .split(";")[0].trim()
          .equals(MediaType.APPLICATION_ATOM_XML_VALUE)
      ) {
        return url;
      } else {
        throw new UrlNotValidException(
          urlConnection.getContentType().split(";")[0].trim()
        );
      }
    } else {
      throw new UrlNotValidException();
    }
  }

  public Link change(@NonNull String url, @NonNull LinkContentType contentType)
    throws Exception {
    return new Link(url, contentType);
  }

  /**
   * The method used to open a stream to the url and attach it to a reader.
   * @return the reader of the input stream.
   */
  public XMLStreamReader openXMLStream() {
    try {
      xmlInputStream = new URL(url).openStream();
      reader = Link.xmlInputFactory.createXMLStreamReader(xmlInputStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return reader;
  }

  /**
   * The method that closes reader and the input stream to free up resources.
   */
  public void CloseXMLStream() {
    try {
      reader.close();
      xmlInputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method gets the raw html file with the contained javascript and css, that can later be displayed in the browser.
   * @return
   */
  @JsonIgnore
  public Document getHtmlContent() {
    try {
      return Jsoup.connect(url).get();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * The alternate method that was used to parse the XML contents using the DOM parser.
   * ! Tremendously slowed down the parsing.
   * @return
   */
  public org.w3c.dom.Document ReadXML() {
    try {
      DocumentBuilder builder = Link.factory.newDocumentBuilder();
      org.w3c.dom.Document doc = builder.parse(url);
      doc.getDocumentElement().normalize();
      return doc;
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    return null;
  }

  /**
   * This method parsed the HTML contents of the page and returned the first image found to be shown as an article image on teh card
   * ! No longer needed, most of the source provided an image if their own
   * @return the url to the image
   * @throws MalformedURLException  when the url is not valid
   * @throws IOException when reading from the stream
   * @throws ParserException when parsing an invalid text value
   */
  public String ReadHtmlAndReturnFirstFoundImageUrl()
    throws MalformedURLException, IOException, ParserException {
    URLConnection connection = new URL(url).openConnection();
    if (
      connection
        .getContentType()
        .split(";")[0].trim()
        .equals(MediaType.TEXT_HTML_VALUE)
    ) {
      Parser parser = new Parser(connection);
      NodeList nodeList = parser.extractAllNodesThatMatch(
        new AndFilter(new TagNameFilter("img"), new HasAttributeFilter("src"))
      );
      ImageTag node = (ImageTag) nodeList.elementAt(0);

      return node.getImageURL();
    } else {
      return null;
    }
  }

  /**
   * This method parsed the HTML contents of the page and returned the first image found to be shown as an article image on teh card
   * ! No longer needed, most of the source provided an image if their own
   * @return the url of the image
   */
  public String ReadHtmlAndReturnFirstFoundImageUrlJsoup() {
    try {
      Document doc = Jsoup.connect(url).get();
      Elements images = doc.select("img");
      if (!images.isEmpty()) {
        return images.first().attr("src");
      }
      System.out.println(images);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
