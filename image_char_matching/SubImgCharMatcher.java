package image_char_matching;

import java.util.*;

/**
 * SubImgCharMatcher is a utility class that maps characters to brightness values,
 * normalizes these values, and allows efficient retrieval of the closest matching character
 * based on a given brightness level.
 */
public class SubImgCharMatcher {

    private final int NUM_CELLS = 16 * 16; // Total cells in a boolean representation of a character
    private Map<Character, Double> charMap; // Stores raw brightness values for each character
    private Map<Character, Double> brightnessCharMap; // Stores normalized brightness values
    private double minValue; // Minimum brightness value
    private double maxValue; // Maximum brightness value

    /**
     * Constructor to initialize the character matcher with a set of characters.
     * @param charset Array of characters to be added to the matcher.
     */
    public SubImgCharMatcher(char[] charset) {
        this.charMap = new HashMap<>();
        this.brightnessCharMap = new HashMap<>();

        minValue = Double.POSITIVE_INFINITY;
        maxValue = Double.NEGATIVE_INFINITY;

        for (char c : charset) {
            double value = 0;
            value = getCharBrightnessValue(c);
            updateMin(value);
            updateMax(value);
            this.charMap.put(c, value);
        }
        createBrightnessCharMap();
    }

    /**
     * Updates the maximum brightness value based on the given value.
     * @param value Brightness value to compare against the current maxValue.
     */
    private void updateMax(double value) {
        if (value > maxValue) {
            maxValue = value;
        }
    }

    /**
     * Updates the minimum brightness value based on the given value.
     * @param value Brightness value to compare against the current minValue.
     */
    private void updateMin(double value) {
        if (value < minValue) {
            minValue = value;
        }
    }

    /**
     * Finds the character with the closest normalized brightness to the given brightness.
     * In case of ties, the character with the lower ASCII value is selected.
     * @param brightness The target brightness value.
     * @return The character closest to the given brightness.
     */
    public char getCharByImageBrightness(double brightness) {
        char closestChar = '\0';
        double closestDistance = Double.POSITIVE_INFINITY;

        for (Map.Entry<Character, Double> entry : brightnessCharMap.entrySet()) {
            Character currentChar = entry.getKey();
            Double charBrightness = entry.getValue();
            double distance = Math.abs(charBrightness - brightness);

            if (distance < closestDistance ||
                    (distance == closestDistance && currentChar < closestChar)) {
                closestChar = currentChar;
                closestDistance = distance; // Correctly update closestDistance
            }
        }
        return closestChar;
    }

    /**
     * Calculates the brightness value of a character based on its boolean representation.
     * @param c The character whose brightness is to be calculated.
     * @return The normalized brightness value.
     */
    private Double getCharBrightnessValue(char c) {
        boolean[][] boolArray = CharConverter.convertToBoolArray(c);
        int numTrueCells = getNumTrueCells(boolArray);
        double value = (double) numTrueCells / NUM_CELLS;
        if (value < 0 || value > 1) {
            System.out.println("value < 0 or value > 1");
        }
        return value;
    }

    /**
     * Counts the number of true cells in a boolean representation of a character.
     * @param boolArray 2D boolean array representing the character.
     * @return The count of true cells.
     */
    private int getNumTrueCells(boolean[][] boolArray) {
        int counter = 0;

        for (boolean[] row : boolArray) {
            for (boolean cell : row) {
                if (cell) {
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * Normalizes the brightness values and stores them in the brightnessCharMap.
     */
    private void createBrightnessCharMap() {
        for (Map.Entry<Character, Double> entry : charMap.entrySet()) {
            double normalizedBrightness = (entry.getValue() - minValue) / (maxValue - minValue);
            brightnessCharMap.put(entry.getKey(), normalizedBrightness);
            System.out.println(entry.getKey() + "   " + normalizedBrightness);
        }
    }

    /**
     * Adds a new character to the matcher and updates brightness maps if necessary.
     * @param c The character to add.
     */
    public void addChar(char c) {
        if (charMap.containsKey(c)) {
            return; // Character already exists
        }

        double value = getCharBrightnessValue(c);
        charMap.put(c, value);

        boolean minOrMaxUpdated = false;

        // Update minValue or maxValue if needed
        if (value > maxValue) {
            maxValue = value;
            minOrMaxUpdated = true;
        } else if (value < minValue) {
            minValue = value;
            minOrMaxUpdated = true;
        }

        // Recreate the brightnessCharMap if min or max was updated
        if (minOrMaxUpdated) {
            brightnessCharMap.clear();
            createBrightnessCharMap();
        } else {
            // Add the new normalized value to the TreeMap
            double normalizedBrightness = (value - minValue) / (maxValue - minValue);
            brightnessCharMap.put(c, normalizedBrightness);
        }
    }

    /**
     * Recalculates minValue and maxValue based on the current character map.
     */
    public void findNewMinMax() {
        minValue = Double.POSITIVE_INFINITY;
        maxValue = Double.NEGATIVE_INFINITY;

        for (double value : charMap.values()) {
            updateMax(value);
            updateMin(value);
        }
    }

    /**
     * Removes a character from the matcher and updates brightness maps if necessary.
     * @param c The character to remove.
     */
    public void removeChar(char c) {
        // char doesn't exist
        if (!charMap.containsKey(c)) {
            return;
        }

        Double value = charMap.get(c);
        charMap.remove(c);
        brightnessCharMap.remove(c);

        // Handle cases where minValue or maxValue is affected
        if (value == maxValue || value == minValue) {
            // Recreate the TreeMap
            brightnessCharMap.clear();
            findNewMinMax();
            createBrightnessCharMap();
        }
    }
}
