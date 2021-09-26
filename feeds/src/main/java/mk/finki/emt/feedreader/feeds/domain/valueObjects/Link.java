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

//Isto kako vo feed source vo linkot se proveruva dali e validen
@Getter
@Embeddable
public class Link implements ValueObject {

  private final String url;

  @Transient
  @JsonIgnore
  private static XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

  @Transient
  @JsonIgnore
  private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

  @Transient
  @JsonIgnore
  private InputStream xmlInputStream;

  @Transient
  @JsonIgnore
  private XMLStreamReader reader;

  protected Link() {
    this.url = "";
  }

  public Link(@NonNull String url, @NonNull LinkContentType contentType)
    throws Exception {
    this.url = url;
    // this.url = this.validateLink(url, contentType);
  }

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

  public XMLStreamReader openXMLStream() {
    InputStream xmlUrl = null;
    XMLStreamReader reader = null;
    try {
      xmlUrl = new URL(url).openStream();
      reader = Link.xmlInputFactory.createXMLStreamReader(xmlUrl);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return reader;
  }

  public void CloseXMLStream() {
    try {
      xmlInputStream.close();
      reader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @JsonIgnore
  public Document getHtmlContent() {
    try {
      return Jsoup.connect(url).get();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

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
