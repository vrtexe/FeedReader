package mk.finki.emt.feedreader.feeds.services.forms;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedSourceForm {

  @NotNull
  @NotEmpty
  private String url;
}
