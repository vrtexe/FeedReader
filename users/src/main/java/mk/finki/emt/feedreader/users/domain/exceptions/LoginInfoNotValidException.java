package mk.finki.emt.feedreader.users.domain.exceptions;

public class LoginInfoNotValidException extends Exception {
    public LoginInfoNotValidException() {
        super("Invalid login information provided");
    }
}
