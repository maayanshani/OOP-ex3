package image;

import ascii_art.exceptions.ImageProcessorExceptions;

import java.awt.*;

// TODO: add documentation


public class ImageProcessor {
    private static final String NULL_EXCEPTION = "Image is a null pointer.";
    private static final String INVALID_RES_EXCEPTION = "Resolution must be smaller than the image dim.";
    private static final String INVALID_IMAGE_DIM_EXCEPTION = "Image dimension must be positive and even.";

    private static final int MAX_RGB = 255;

    public ImageProcessor() {

    }

    // TODO: Add to the README
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
                if (i<extendHeightPixels || j<extendWidthPixels ||
                        i>=originalHeight+extendHeightPixels || j>=originalWidth+extendWidthPixels) {
                    extendedPixelArray[i][j] = Color.WHITE;
                }
                else {
                    extendedPixelArray[i][j] = image.getPixel(i- extendHeightPixels,j-extendWidthPixels);
                }
            }
        }

        return new Image(extendedPixelArray, newWidth, newHeight);

    }

    private int calculateSidePixels(int originalDim, int extendedDim) throws ImageProcessorExceptions {
        // we can assume the dimensions are always even
        if (extendedDim%2!=0 || originalDim%2!=0) {
            throw new ImageProcessorExceptions(INVALID_IMAGE_DIM_EXCEPTION);
        }
        return (extendedDim - originalDim) / 2;
    }

    private int extendDim(int dim) throws ImageProcessorExceptions {
        if ((dim > 0) && ((dim & (dim - 1)) == 0)) {
            return dim;
        } else return nextPowerOfTwo(dim);

    }

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

    public Image[][] divideImage(Image image, int numSubImagesInRow) throws ImageProcessorExceptions {
        // TODO: what is "valid resolution"? add Exceptions
        // TODO: EXCEPTION
        if (numSubImagesInRow > image.getWidth() || numSubImagesInRow > image.getHeight()) {
            throw new ImageProcessorExceptions(INVALID_RES_EXCEPTION);
        }

        // Calculate sub-image dimensions
        int subImageWidth = image.getWidth() / numSubImagesInRow;
        int subImageHeight = image.getHeight() / numSubImagesInRow;

        int numSubImagesInCol = image.getHeight() / subImageHeight;

        Image[][] subImages = new Image[numSubImagesInRow][numSubImagesInCol];

        for (int row = 0; row < numSubImagesInRow; row++) {
            for (int col = 0; col < numSubImagesInCol; col++) {
                Color[][] curSubArray = new Color[subImageHeight][subImageWidth];

                // calculate the current sub-image:
                for (int i = 0; i < subImageHeight; i++) {
                    for (int j = 0; j < subImageWidth; j++) {
                        curSubArray[i][j] = image.getPixel(row*subImageHeight + i, col*subImageWidth + j);
                    }
                }
                Image curSubImage = new Image(curSubArray, subImageWidth, subImageHeight);
                subImages[row][col] = curSubImage;
            }
        }

        return subImages;
    }

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
        return (greyPixelsSum/numPixels)/MAX_RGB;

    }




}
