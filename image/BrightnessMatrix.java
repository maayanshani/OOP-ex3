package image;

import ascii_art.exceptions.ImageProcessorExceptions;

/**
 * Represents a matrix of brightness values calculated from sub-images of an input image.
 * This class provides functionality to calculate and store the brightness values of each sub-image
 * and retrieve specific pixel brightness values.
 */
public class BrightnessMatrix {
    private static final String NULL_EXCEPTION = "subImage is a null pointer";
    private final double[][] doubleImage;

    /**
     * Constructs a BrightnessMatrix by calculating brightness values for a given 2D array of sub-images.
     *
     * @param subImages A 2D array of Image objects representing the divided sub-images of an input image.
     * @throws ImageProcessorExceptions If the subImages array is null.
     */
    public BrightnessMatrix(Image[][] subImages) throws ImageProcessorExceptions {
        ImageProcessor imageProcessor = new ImageProcessor();
        if (subImages == null) {
            throw new ImageProcessorExceptions(NULL_EXCEPTION);
        }

        int numRow = subImages.length;
        int numCols = subImages[0].length;
        this.doubleImage = new double[numRow][numCols];

        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCols; j++) {
                Image curImage = subImages[i][j];
                this.doubleImage[i][j] = imageProcessor.imageBrightness(curImage);
            }
        }
    }

    /**
     * Retrieves the brightness value of a specific pixel in the matrix.
     *
     * @param row The row index of the pixel.
     * @param col The column index of the pixel.
     * @return The brightness value of the specified pixel as a double.
     */
    public double getPixel(int row, int col) {
        return this.doubleImage[row][col];
    }
}
