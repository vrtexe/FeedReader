package mk.finki.emt.feedreader.feeds.services.forms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * The form used hen adding a feed source,
 * can be replaced with only a string
 */
@Data
public class FeedSourceForm {

  @NotNull
  @NotEmpty
  private String url;

  @JsonCreator
  public FeedSourceForm(@JsonProperty("url") String url) {
    this.url = url;
  }
}
