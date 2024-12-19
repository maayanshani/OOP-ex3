package image;

import ascii_art.exceptions.ImageProcessExceptions;

public class BrightnessMatrix {
    private static final String NULL_EXCEPTION = "subImage is a null pointer";
    private final double[][] doubleImage;


    public BrightnessMatrix(Image[][] subImages) {
        ImageProcessor imageProcessor = new ImageProcessor();
        if (subImages == null) {
            throw new ImageProcessExceptions(NULL_EXCEPTION);
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

    public double getPixel(int row, int col) {
        return this.doubleImage[row][col];
    }
}
