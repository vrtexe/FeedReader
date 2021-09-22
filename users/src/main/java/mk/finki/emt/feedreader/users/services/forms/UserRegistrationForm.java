package mk.finki.emt.feedreader.users.services.forms;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
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
}
