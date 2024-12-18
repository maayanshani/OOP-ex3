package image;

public class BrightnessMatrix {
    private double[][] doubleImage;


    public BrightnessMatrix(Image[][] subImages) {
        ImageProcessor imageProcessor = new ImageProcessor();
        // TODO: EXCEPTION
        if (subImages!=null) {
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
    }

    public double getPixel(int row, int col) {
        return this.doubleImage[row][col];
    }


}
