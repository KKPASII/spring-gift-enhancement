package gift.exception;

public class LoginFailedException extends UnAuthenticatedException {
  public LoginFailedException(String message) {
    super(message);
  }
}
