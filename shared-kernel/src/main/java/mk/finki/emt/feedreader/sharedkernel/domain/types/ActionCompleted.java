package mk.finki.emt.feedreader.sharedkernel.domain.types;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A class used to wrap the boolean response to avoid errors from jackson parsing
 */
@Data
@AllArgsConstructor
public class ActionCompleted {

  private Boolean completed;
}
