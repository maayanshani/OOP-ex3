package ascii_art;

import image.BrightnessMatrix;
import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;

import java.util.ArrayList;

/**
 * The AsciiArtAlgorithm class is responsible for generating an ASCII art representation
 * of an image by dividing it into sub-images, calculating their brightness, and matching
 * the brightness to a set of characters.
 */
public class AsciiArtAlgorithm {
    private Image image; // The input image to be converted into ASCII art.
    private final ImageProcessor imageProcessor; // Used for processing the image.
    private final int resolution; // The resolution for dividing the image into sub-images.
    private final SubImgCharMatcher subImgCharMatcher; // Matches characters to brightness levels.
    private final ArrayList<BrightnessMatrix> brightnessMatrices; // Stores precomputed brightness matrices.

    /**
     * Constructs an AsciiArtAlgorithm instance.
     *
     * @param image              The image to be converted into ASCII art.
     * @param resolution         The resolution for dividing the image into sub-images.
     * @param subImgCharMatcher  The character matcher for mapping brightness to characters.
     * @param brightnessMatrices A list of precomputed brightness matrices.
     */
    public AsciiArtAlgorithm(Image image, int resolution, SubImgCharMatcher subImgCharMatcher,
                             ArrayList<BrightnessMatrix> brightnessMatrices) {
        this.image = image;
        this.imageProcessor = new ImageProcessor();
        this.resolution = resolution;
        this.subImgCharMatcher = subImgCharMatcher;
        this.brightnessMatrices = brightnessMatrices;
    }

    /**
     * Runs the ASCII art generation algorithm.
     *
     * Steps:
     * 1. If no brightness matrix exists, it calculates and stores one.
     * 2. Divides the image into sub-images of the given resolution.
     * 3. Computes brightness for each sub-image and maps it to a corresponding character.
     *
     * @return A 2D character array representing the ASCII art.
     */
    public char[][] run() {
        // If no brightness matrix was calculated, calculate and add to the array.
        if (brightnessMatrices.isEmpty()) {
            // Pad the image:
            this.image = imageProcessor.extendImage(image);
            // Divide the image according to the given resolution:
            Image[][] subImages = imageProcessor.divideImage(image, resolution);
            // Create a brightness matrix:
            brightnessMatrices.add(new BrightnessMatrix(subImages));
        }

        // Create ASCII art matrix:
        char[][] newImage = new char[resolution][resolution];
        for (int row = 0; row < resolution; row++) {
            for (int col = 0; col < resolution; col++) {
                double brightness = brightnessMatrices.get(brightnessMatrices.size() - 1).getPixel(row, col);
                newImage[row][col] = subImgCharMatcher.getCharByImageBrightness(brightness);
            }
        }
        return newImage;
    }
}
