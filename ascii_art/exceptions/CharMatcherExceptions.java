package ascii_art.exceptions;

/**
 * The CharMatcherExceptions class represents exceptions that occur during the character matching process.
 * It extends the AsciiArtExceptions class to provide specific error messages related to issues
 * in brightness comparison or invalid configurations for character matching.
 */
public class CharMatcherExceptions extends AsciiArtExceptions {

    /**
     * Constructs a new CharMatcherExceptions with the specified exception message.
     *
     * @param exceptionMessage The detailed error message explaining the cause of the exception.
     */
    public CharMatcherExceptions(String exceptionMessage) {
        super(exceptionMessage);
    }

}
