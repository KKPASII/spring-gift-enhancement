package gift.global.exception;

public class UnAuthenticatedException extends RuntimeException {
    public UnAuthenticatedException(String message) {
        super(message);
    }
}
