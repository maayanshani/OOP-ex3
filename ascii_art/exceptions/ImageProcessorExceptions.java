package ascii_art.exceptions;

/**
 * The ImageProcessorExceptions class is a specialized exception class
 * for handling errors that occur during image processing operations
 * in the ASCII art generation application.
 * It extends the AsciiArtExceptions class to provide a unified
 * exception handling mechanism for image-related issues.
 */
public class ImageProcessorExceptions extends AsciiArtExceptions {

    /**
     * Constructs a new ImageProcessorExceptions with the specified exception message.
     *
     * @param exceptionMessage The detailed error message describing the image processing issue.
     */
    public ImageProcessorExceptions(String exceptionMessage) {
        super(exceptionMessage);
    }

}
