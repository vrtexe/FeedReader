package mk.finki.emt.feedreader.users.services.types;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A wrapper class for the username,
 *  to avoid the jackson error
 */
@Data
@AllArgsConstructor
public class Username {

  private String username;
}
