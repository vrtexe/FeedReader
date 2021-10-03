package mk.finki.emt.feedreader.users.services.forms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * This form represents the registration form,
 * it contains the general information for the user, such as the full name,
 * the email, password and username.
 */
@Data
public class UserRegistrationForm {

  @NotNull
  @NotBlank
  @NotEmpty
  private String name;

  @NotNull
  @NotBlank
  @NotEmpty
  private String lastName;

  @NotNull
  @NotBlank
  @NotEmpty
  @Size(min = 5, max = 14)
  private String username;

  @NotNull
  @NotBlank
  @NotEmpty
  @Size(min = 6)
  private String password;

  @Email
  private String email;

  @JsonCreator
  public UserRegistrationForm(
    @JsonProperty("name") String name,
    @JsonProperty("lastName") String lastName,
    @JsonProperty("username") String username,
    @JsonProperty("password") String password,
    @JsonProperty("email") String email
  ) {
    this.name = name;
    this.lastName = lastName;
    this.username = username;
    this.password = password;
    this.email = email;
  }
}
