package ascii_art;


import image.Image;

import java.util.Map;

public class AsciiArtAlgorithm {
    private Image image;
    private int resolution;
    // TODO: check with Maayan about the chars array
    private Map<Character, Double> charMap;

    public AsciiArtAlgorithm(Image image, int resolution, Map<Character, Double> charMap) {
        this.image = image;
        this.resolution = resolution;
        this.charMap = charMap;
    }

    public char[][] run() {
        // TODO: write this method
        return new char[0][0];
    }
}