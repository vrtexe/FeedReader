package mk.finki.emt.feedreader.feeds.domain.valueObjects;

import javax.persistence.Embeddable;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import lombok.Getter;
import mk.finki.emt.feedreader.feeds.domain.exceptions.UnsupportedXMLStreamReaderState;

@Embeddable
@Getter
public class Author {

  private final String name;

  private final String email;

  private final String uri;

  public Author() {
    this.name = "";
    this.email = "";
    this.uri = "";
  }

  public Author(String name) {
    this.name = name;
    this.email = null;
    this.uri = null;
  }

  public Author(String name, String email, String uri) {
    this.name = name;
    this.email = email;
    this.uri = uri;
  }

  public static Author createFromXmlStream(XMLStreamReader reader) throws XMLStreamException {
    String name = "";
    String uri = null;
    String email = null;
    if (reader.isStartElement() && reader.getLocalName().equals("author")) {
      while (reader.hasNext()) {
        if (reader.isEndElement() && !reader.getLocalName().equals("author")) {
          break;
        }
        reader.next();
        if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
          switch (reader.getLocalName()) {
            case "name" -> {
              name = reader.getElementText();
            }
            case "uri" -> {
              uri = reader.getElementText();
            }
            case "email" -> {
              email = reader.getElementText();
            }
          }
        }
      }
    } else {
      throw new UnsupportedXMLStreamReaderState();
    }

    return new Author(name, email, uri);
  }
}
