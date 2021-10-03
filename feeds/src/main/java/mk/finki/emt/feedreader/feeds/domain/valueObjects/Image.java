package mk.finki.emt.feedreader.feeds.domain.valueObjects;

import javax.persistence.Embeddable;
import lombok.Getter;

/**
 * The class containing all the image properties, it is used to represent the logo of the feed source, or an image from the article.
 */
@Getter
@Embeddable
public class Image {

  private final String imageUrl;

  private final String imageAlt;

  protected Image() {
    this.imageUrl = "";
    this.imageAlt = "";
  }

  public Image(String imageUrl, String imageAlt) {
    this.imageUrl = imageUrl;
    this.imageAlt = imageAlt;
  }
}
