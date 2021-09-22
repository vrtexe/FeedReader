package mk.finki.emt.feedreader.users.domain.valueobjects;

import javax.annotation.PostConstruct;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import lombok.Getter;
import mk.finki.emt.feedreader.users.domain.exceptions.LoginInfoNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Getter
@Embeddable
@Component
public class AuthInfo {

  private final String username;

  private final String password;

  @Transient
  private static PasswordEncoder passwordEncoder;

  protected AuthInfo(){
    this.username = null;
    this.password = null;
  }

  public AuthInfo(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @PostConstruct
  public void init() {
    System.out.println("loading encoder as [" + AuthInfo.passwordEncoder + "]");
  }

  @Autowired
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    AuthInfo.passwordEncoder = passwordEncoder;
  }

  public String login(String username, String password)
    throws LoginInfoNotValidException {
    if (
      this.username.equals(username) &&
      AuthInfo.passwordEncoder.matches(password, this.password)
    ) {
      return username;
    }
    throw new LoginInfoNotValidException();
  }
}
