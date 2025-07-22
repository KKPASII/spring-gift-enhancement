package gift.global.exception;

public class TokenExpiredException extends UnAuthenticatedException {
  public TokenExpiredException(String message) {
    super(message);
  }
}
