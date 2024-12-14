package ascii_art;


import image.Image;
import image_char_matching.SubImgCharMatcher;

import java.util.Map;

public class AsciiArtAlgorithm {
    private Image image;
    private int resolution;
    // TODO: check with Maayan about the chars array
    private Map<Character, Double> charMap;
    private SubImgCharMatcher subImgCharMatcher;

    public AsciiArtAlgorithm(Image image, int resolution, char[] charset) {
        this.image = image;
        this.resolution = resolution;
        this.subImgCharMatcher = new SubImgCharMatcher(charset);
    }

    public char[][] run() {
        // todo: how to decrease the size of the image?
        char[][] newImage = new char[resolution][resolution];
        for (int i = 0; i < resolution; i++) {
            for (int j = 0; j < resolution; j++) {
                // todo: add here your implementation
                double brightness = 0;
                newImage[i][j] = subImgCharMatcher.getCharByImageBrightness(brightness);
            }
        }
        return newImage;
    }
}