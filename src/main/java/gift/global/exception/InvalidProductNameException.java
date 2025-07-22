package gift.global.exception;

public class InvalidProductNameException extends IllegalArgumentException {
    public InvalidProductNameException(String message) {
        super(message);
    }
}