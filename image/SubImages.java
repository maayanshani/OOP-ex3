package image;

import java.awt.*;

public class SubImages {
    private Image originalImage;
//    private int resolution;
    Image[][] subImages;

    public SubImages(Image originalImage, int resolution) {
        this.originalImage = originalImage;
//        this.resolution = resolution;
        this.subImages = divideImage(resolution);



    }

    private Image[][] divideImage(int numSubImagesInRow) {
        // TODO: what is "valid resolution"?
        int subImageDim = originalImage.getWidth() / numSubImagesInRow;
        int numSubImagesInCol = originalImage.getHeight() / subImageDim;
        Image[][] subImages = new Image[numSubImagesInRow][numSubImagesInCol];
        Color[][] curSubArray = new Color[subImageDim][subImageDim];

        for (int row = 0; row < numSubImagesInRow; row++) {
            for (int col = 0; col < numSubImagesInCol; col++) {

                // calculate the current sub-image:
                for (int i = 0; i < subImageDim; i++) {
                    for (int j = 0; j < subImageDim; j++) {
                        curSubArray[i][j] = originalImage.getPixel(row + i, col + j);
                    }
                }
                Image curSubImage = new Image(curSubArray, subImageDim, subImageDim);
                subImages[row][col] = curSubImage;
            }
        }

        return subImages;
    }

    public Image getSubImage(int row, int col) {
        return subImages[row][col];
    }


}
