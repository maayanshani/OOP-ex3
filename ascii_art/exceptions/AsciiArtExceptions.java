package ascii_art.exceptions;


/**
 * The AsciiArtExceptions class serves as the base exception class for all exceptions
 * related to the ASCII art generation application.
 * It extends the RuntimeException class to handle runtime errors specifically tied to
 * issues in ASCII art processing, configuration, or user input.
 */
public class AsciiArtExceptions extends RuntimeException {

    /**
     * Constructs a new AsciiArtExceptions with the specified exception message.
     *
     * @param exceptionMessage The detailed error message explaining the cause of the exception.
     */
    public AsciiArtExceptions(String exceptionMessage) {
        super(exceptionMessage);
    }
}

