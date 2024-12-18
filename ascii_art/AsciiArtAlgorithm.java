package ascii_art;


import image.BrightnessMatrix;
import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;

import java.util.ArrayList;
import java.util.Map;

public class AsciiArtAlgorithm {
    private Image image;
    private final ImageProcessor imageProcessor;
    private final int resolution;
    private final SubImgCharMatcher subImgCharMatcher;
    private final ArrayList<BrightnessMatrix> brightnessMatrices;

    public AsciiArtAlgorithm(Image image, int resolution, char[] charset, ArrayList<BrightnessMatrix> brightnessMatrices) {
        this.image = image;
        this.imageProcessor = new ImageProcessor();
        this.resolution = resolution;
        this.subImgCharMatcher = new SubImgCharMatcher(charset);
        this.brightnessMatrices = brightnessMatrices;
    }

    public char[][] run() {
        // if no brightness matrix was calculated, calculated and add to the array:
        if (brightnessMatrices.isEmpty()) {
            // Padd:
            this.image = imageProcessor.extendImage(image);
            // Divide the image according to given resolution:
            Image[][] subImages = imageProcessor.divideImage(image, resolution);
            // Create BrightnessMatrix:
            brightnessMatrices.add(new BrightnessMatrix(subImages));
        }

        // Create AsciiMatrix:
        char[][] newImage = new char[resolution][resolution];
        for (int row = 0; row < resolution; row++) {
            for (int col = 0; col < resolution; col++) {
                double brightness = brightnessMatrices.get(brightnessMatrices.size()-1).getPixel(row, col);
                newImage[row][col] = subImgCharMatcher.getCharByImageBrightness(brightness);
            }
        }
        return newImage;
    }
}