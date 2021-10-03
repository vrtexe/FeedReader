package mk.finki.emt.feedreader.users.services.forms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * This class is a form for authenticating the user,
 * it consists of teh username and password provided by user input
 */
@Data
public class UserLoginForm {

  @NotNull
  @NotBlank
  @NotEmpty
  private String username;

  @NotNull
  @NotBlank
  @NotEmpty
  @Size(min = 6)
  private String password;

  @JsonCreator
  public UserLoginForm(
    @JsonProperty("username") String username,
    @JsonProperty("password") String password
  ) {
    this.username = username;
    this.password = password;
  }
}
