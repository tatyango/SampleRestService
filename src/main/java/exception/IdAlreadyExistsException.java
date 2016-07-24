package exception;

/**
 * Created by Tetiana on 7/23/2016.
 */
public class IdAlreadyExistsException extends RuntimeException {
    public IdAlreadyExistsException(String message) {
        super(message);
    }
}
