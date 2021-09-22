package mk.finki.emt.feedreader.feeds.domain.valueObjects;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.persistence.Embeddable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
import org.springframework.http.MediaType;
import org.w3c.dom.Document;

@Getter
@Embeddable
public class Link implements ValueObject {

  private final String url;

  protected Link() {
    this.url = "";
  }

  public Link(@NonNull String url, @NonNull LinkContentType contentType)
    throws Exception {
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
        this.url = url;
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
        this.url = url;
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

  public Document ReadXML() {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(url);
      doc.getDocumentElement().normalize();
      return doc;
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    return null;
  }

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
}
