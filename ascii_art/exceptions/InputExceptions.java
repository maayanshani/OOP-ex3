package ascii_art.exceptions;

/**
 * The InputExceptions class represents errors related to invalid input
 * provided to the ASCII art generation application.
 * It extends the AsciiArtExceptions class to ensure all input-related
 * errors are handled in a consistent way within the application.
 */
public class InputExceptions extends AsciiArtExceptions {

    /**
     * Constructs a new InputExceptions with the specified exception message.
     *
     * @param exceptionMessage The detailed error message describing the invalid input issue.
     */
    public InputExceptions(String exceptionMessage) {
        super(exceptionMessage);
    }
}
