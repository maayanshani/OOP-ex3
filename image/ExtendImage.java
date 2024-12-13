package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ExtendImage extends Image {
    private final Color[][] pixelArray;
    private final int width;
    private final int height;

    // TODO: figure a better solution for not override getters, save image, brightness
    // TODO: catch the extension in shell
    public ExtendImage(Color[][] pixelArray, int width, int height) throws IllegalArgumentException {
        super(pixelArray, width, height);

        int newWidth = extendDim(width);
        int newHeight = extendDim(height);

        Color[][] extendedPixelArray = new Color[newHeight][newWidth];

        int extendWidthPixels = calculateSidePixels(width, newWidth);
        int extendHeightPixels = calculateSidePixels(height, newHeight);

        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                if (i<extendHeightPixels || j<extendWidthPixels ||
                    i>=height+extendHeightPixels || j>=width+extendWidthPixels) {
                    extendedPixelArray[i][j] = Color.WHITE;
                }
                else {
                    extendedPixelArray[i][j] = pixelArray[i- extendHeightPixels][j-extendWidthPixels];
                }
            }
        }

        this.pixelArray = extendedPixelArray;
        this.width = newWidth;
        this.height = newHeight;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Color getPixel(int x, int y) {
        return pixelArray[y][x];
    }

    private int calculateSidePixels(int originalDim, int extendedDim) {
        // we can assume the dimensions are always even
        return  (extendedDim-originalDim)/2;
    }


    private int extendDim(int dim) throws IllegalArgumentException {
        if ((dim > 0) && ((dim & (dim - 1)) == 0)) {
            return dim;
        }
        else return nextPowerOfTwo(dim);

    }

    private static int nextPowerOfTwo(int n) throws IllegalArgumentException {
        if (n <= 0) {
            throw new IllegalArgumentException("Dimension must be positive.");
        }
        n--;
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        return n + 1;
    }

    @Override
    public void saveImage(String fileName){
        // Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(pixelArray[0].length, pixelArray.length,
                BufferedImage.TYPE_INT_RGB);
        // Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < pixelArray.length; x++) {
            for (int y = 0; y < pixelArray[x].length; y++) {
                bufferedImage.setRGB(y, x, pixelArray[x][y].getRGB());
            }
        }
        File outputfile = new File(fileName+".jpeg");
        try {
            ImageIO.write(bufferedImage, "jpeg", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
