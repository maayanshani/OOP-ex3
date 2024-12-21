package ascii_art.exceptions;

/**
 * This class represents exceptions related to character set operations in the ASCII art generation process.
 * It extends the {@link AsciiArtExceptions} to provide specific error handling for character set issues.
 */
public class CharSetExceptions extends AsciiArtExceptions {

    /**
     * Constructs a new charSetExceptions with the specified exception message.
     *
     * @param exceptionMessage The detailed error message explaining the cause of the exception.
     */
    public CharSetExceptions(String exceptionMessage) {
        super(exceptionMessage);
    }
}
