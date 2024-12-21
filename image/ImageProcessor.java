package image;

import ascii_art.exceptions.ImageProcessorExceptions;

import java.awt.*;

/**
 * The ImageProcessor class provides various utilities for processing images, such as extending
 * their dimensions to powers of two, dividing them into smaller sub-images, and calculating brightness levels.
 */
public class ImageProcessor {
    private static final String NULL_EXCEPTION = "Image is a null pointer.";
    private static final String INVALID_RES_EXCEPTION = "Resolution must be smaller than the image dim.";
    private static final String INVALID_IMAGE_DIM_EXCEPTION = "Image dimension must be positive and even.";
    private static final int MAX_RGB = 255;

    /**
     * Default constructor for ImageProcessor.
     */
    public ImageProcessor() {
    }

    /**
     * Extends the dimensions of an image to the nearest power of two, filling added areas with white pixels.
     *
     * @param image The image to extend.
     * @return A new Image object with extended dimensions.
     * @throws ImageProcessorExceptions If the provided image is null or its dimensions are invalid.
     */
    public Image extendImage(Image image) throws ImageProcessorExceptions {
        if (image == null) {
            throw new ImageProcessorExceptions(NULL_EXCEPTION);
        }
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        int newWidth = extendDim(originalWidth);
        int newHeight = extendDim(originalHeight);

        Color[][] extendedPixelArray = new Color[newHeight][newWidth];

        int extendWidthPixels = calculateSidePixels(originalWidth, newWidth);
        int extendHeightPixels = calculateSidePixels(originalHeight, newHeight);

        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                if (i < extendHeightPixels || j < extendWidthPixels ||
                        i >= originalHeight + extendHeightPixels || j >= originalWidth + extendWidthPixels) {
                    extendedPixelArray[i][j] = Color.WHITE;
                } else {
                    extendedPixelArray[i][j] = image.getPixel(i - extendHeightPixels, j - extendWidthPixels);
                }
            }
        }

        return new Image(extendedPixelArray, newWidth, newHeight);
    }

    /**
     * Calculates the number of pixels to add on each side of the image to extend its dimensions.
     *
     * @param originalDim The original dimension of the image (width or height).
     * @param extendedDim The extended dimension of the image (width or height).
     * @return The number of pixels to add on each side.
     * @throws ImageProcessorExceptions If the dimensions are not even.
     */
    private int calculateSidePixels(int originalDim, int extendedDim) throws ImageProcessorExceptions {
        if (extendedDim % 2 != 0 || originalDim % 2 != 0) {
            throw new ImageProcessorExceptions(INVALID_IMAGE_DIM_EXCEPTION);
        }
        return (extendedDim - originalDim) / 2;
    }

    /**
     * Extends a given dimension to the nearest power of two.
     *
     * @param dim The original dimension of the image (width or height).
     * @return The extended dimension, which is the nearest power of two.
     * @throws ImageProcessorExceptions If the original dimension is invalid.
     */
    private int extendDim(int dim) throws ImageProcessorExceptions {
        if ((dim > 0) && ((dim & (dim - 1)) == 0)) {
            return dim;
        } else {
            return nextPowerOfTwo(dim);
        }
    }

    /**
     * Calculates the next power of two for a given number.
     *
     * @param n The input number.
     * @return The next power of two greater than or equal to the input number.
     * @throws ImageProcessorExceptions If the input number is less than or equal to zero.
     */
    private static int nextPowerOfTwo(int n) throws ImageProcessorExceptions {
        if (n <= 0) {
            throw new ImageProcessorExceptions(INVALID_IMAGE_DIM_EXCEPTION);
        }
        n--;
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        return n + 1;
    }

    /**
     * Divides an image into smaller sub-images of equal size based on the specified number of rows.
     *
     * @param image             The image to divide.
     * @param numSubImagesInRow The number of sub-images in a row.
     * @return A 2D array of sub-images.
     * @throws ImageProcessorExceptions If the resolution exceeds the image's dimensions.
     */
    public Image[][] divideImage(Image image, int numSubImagesInRow) throws ImageProcessorExceptions {
        if (numSubImagesInRow > image.getWidth() || numSubImagesInRow > image.getHeight()) {
            throw new ImageProcessorExceptions(INVALID_RES_EXCEPTION);
        }

        int subImageWidth = image.getWidth() / numSubImagesInRow;
        int subImageHeight = image.getHeight() / numSubImagesInRow;
        int numSubImagesInCol = image.getHeight() / subImageHeight;

        Image[][] subImages = new Image[numSubImagesInRow][numSubImagesInCol];

        for (int row = 0; row < numSubImagesInRow; row++) {
            for (int col = 0; col < numSubImagesInCol; col++) {
                Color[][] curSubArray = new Color[subImageHeight][subImageWidth];
                for (int i = 0; i < subImageHeight; i++) {
                    for (int j = 0; j < subImageWidth; j++) {
                        curSubArray[i][j] = image.getPixel(row * subImageHeight + i, col * subImageWidth + j);
                    }
                }
                subImages[row][col] = new Image(curSubArray, subImageWidth, subImageHeight);
            }
        }

        return subImages;
    }

    /**
     * Calculates the brightness of an image by averaging the brightness values of all its pixels.
     * The brightness is calculated using the formula:
     * brightness = (0.2126 * R + 0.7152 * G + 0.0722 * B) / 255.
     *
     * @param image The image for which the brightness is calculated.
     * @return The brightness value as a double between 0 and 1.
     * @throws ImageProcessorExceptions If the image dimensions are invalid.
     */
    public double imageBrightness(Image image) throws ImageProcessorExceptions {
        int curWidth = image.getWidth();
        int curHeight = image.getHeight();
        double greyPixelsSum = 0;
        int numPixels = curHeight * curWidth;
        if (numPixels == 0) {
            throw new ImageProcessorExceptions(INVALID_IMAGE_DIM_EXCEPTION);
        }

        for (int i = 0; i < curHeight; i++) {
            for (int j = 0; j < curWidth; j++) {
                Color curPixel = image.getPixel(i, j);
                double greyPixel = curPixel.getRed() * 0.2126 +
                        curPixel.getGreen() * 0.7152 + curPixel.getBlue() * 0.0722;
                greyPixelsSum += greyPixel;
            }
        }
        return (greyPixelsSum / numPixels) / MAX_RGB;
    }
}
