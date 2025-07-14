package gift.exception;

public class InvalidProductNameException extends IllegalArgumentException {
    public InvalidProductNameException(String message) {
        super(message);
    }
}