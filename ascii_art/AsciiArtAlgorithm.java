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
    // TODO: check with Maayan about the chars array
    private Map<Character, Double> charMap;
    private SubImgCharMatcher subImgCharMatcher;

    public AsciiArtAlgorithm(Image image, int resolution, char[] charset) {
        this.image = image;
        this.imageProcessor = new ImageProcessor();
        this.resolution = resolution;
        this.subImgCharMatcher = new SubImgCharMatcher(charset);
    }

    public char[][] run() {
        Image[][] subImages = imageProcessor.divideImage(image, resolution);
        BrightnessMatrix brightnessMatrix = new BrightnessMatrix(subImages);
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