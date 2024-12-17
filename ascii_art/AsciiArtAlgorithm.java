package ascii_art;


import image.BrightnessMatrix;
import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;

import java.util.Map;

public class AsciiArtAlgorithm {
    private Image image;
    private ImageProcessor imageProcessor;
    private int resolution;
    private SubImgCharMatcher subImgCharMatcher;

    public AsciiArtAlgorithm(Image image, int resolution, char[] charset) {
        this.image = image;
        this.imageProcessor = new ImageProcessor();
        this.resolution = resolution;
        this.subImgCharMatcher = new SubImgCharMatcher(charset);
    }

    public char[][] run() {
        // Padd:
        this.image = imageProcessor.extendImage(image);
        // Divide the image according to given resolution:
        Image[][] subImages = imageProcessor.divideImage(image, resolution);
        // Create BrightnessMatrix:
        BrightnessMatrix brightnessMatrix = new BrightnessMatrix(subImages);

        // Create AsciiMatrix:
        char[][] newImage = new char[resolution][resolution];
        for (int row = 0; row < resolution; row++) {
            for (int col = 0; col < resolution; col++) {
                double brightness = brightnessMatrix.getPixel(row, col);
                System.out.println("brightenss: "+ row + col + ": "+ brightness);
                newImage[row][col] = subImgCharMatcher.getCharByImageBrightness(brightness);
            }
        }
        return newImage;
    }
}