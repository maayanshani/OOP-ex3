package image_char_matching;

import ascii_art.exceptions.CharMatcherExceptions;
import ascii_art.exceptions.InputExceptions;

import java.util.*;


// TODO: add round above/ beneath

/**
 * SubImgCharMatcher is a utility class that maps characters to brightness values,
 * normalizes these values, and allows efficient retrieval of the closest matching character
 * based on a given brightness level.
 */
public class SubImgCharMatcher {

    private static final double MIN_BRIGTHNESS = 0;
    private static final double MAX_BRIGHTNESS = 1;
    private static final String ILLEGAL_BRIGHTNESS_VALUE_MESSAGE =
            "Brightness value must be between 0 and 1.";
    private static final int NUM_CELLS = 16 * 16; // Total cells in a boolean representation of a character
    private static final String ABS = "abs";
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final String ROUND_INCORRECT_FORMAT_MESSAGE =
            "Did not change rounding method due to incorrect format.";
    private static final String WRONG_METHOD_EXCEPTION = "wrong round method";

    private Map<Character, Double> charMap; // Stores raw brightness values for each character
    private Map<Character, Double> brightnessCharMap; // Stores normalized brightness values
    private double minValue; // Minimum brightness value
    private double maxValue; // Maximum brightness value
    private String roundMethod;

    /**
     * Constructor to initialize the character matcher with a set of characters.
     * @param charset Array of characters to be added to the matcher.
     */
    public SubImgCharMatcher(char[] charset) {
        this.charMap = new HashMap<>();
        this.brightnessCharMap = new HashMap<>();
        this.roundMethod = ABS;

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
    public char getCharByImageBrightness(double brightness) throws CharMatcherExceptions {
        if (brightness < MIN_BRIGTHNESS || brightness > MAX_BRIGHTNESS) {
            throw new CharMatcherExceptions(ILLEGAL_BRIGHTNESS_VALUE_MESSAGE);
        }

        char closestChar = '\0';
        double closestDistance = Double.POSITIVE_INFINITY;

        for (Map.Entry<Character, Double> entry : brightnessCharMap.entrySet()) {
            Character currentChar = entry.getKey();
            Double currentCharBrightness = entry.getValue();
//            double distance = Math.abs(charBrightness - brightness);
            double distance = calculateDistance(brightness, currentCharBrightness);

            if (distance < closestDistance ||
                    (distance == closestDistance && currentChar < closestChar)) {
                closestChar = currentChar;
                closestDistance = distance; // Correctly update closestDistance
            }
        }
        return closestChar;
    }

    /**
     * Calculates the distance between the provided brightness and the character brightness
     * using the specified rounding method.
     *
     * @param providedBrightness The brightness value provided by the user. It must be a double
     *                           between 0 and 1 inclusive.
     * @param currentCharBrightness     The brightness value associated with a character.
     * @return The calculated distance as a double.
     *
     * @throws CharMatcherExceptions if the rounding method is invalid or not recognized.
     *
     * Rounding methods:
     * - ABS: No rounding is applied. The absolute difference is returned.
     * - UP: The providedBrightness is rounded up using Math.ceil before calculating the difference.
     * - DOWN: The providedBrightness is rounded down using Math.floor before calculating the difference.
     */
    private double calculateDistance(double providedBrightness, double currentCharBrightness) {
        switch (this.roundMethod) {
            case ABS:
                return Math.abs(currentCharBrightness - providedBrightness);
            case UP:
                double upBrightness = Math.ceil(providedBrightness);
                return Math.abs(currentCharBrightness - upBrightness);
            case DOWN:
                double floorBrightness = Math.floor(providedBrightness);
                return Math.abs(currentCharBrightness - floorBrightness);
            default:
                throw new CharMatcherExceptions(WRONG_METHOD_EXCEPTION);
        }
    }

//    todo: add to readme
    /**
     * Sets the rounding method for calculations.
     *
     * @param roundMethod The rounding method to set. It must be one of the following:
     *                    - "ABS": Absolute rounding method.
     *                    - "UP": Round up method.
     *                    - "DOWN": Round down method.
     *
     * @throws InputExceptions if the provided roundMethod is invalid or not recognized.
     *         The exception message will be: ROUND_INCORRECT_FORMAT_MESSAGE.
     */
    public void setRoundMethod(String roundMethod) throws InputExceptions {
        switch (roundMethod) {
            case ABS:
                this.roundMethod = ABS;
                return;
            case UP:
                this.roundMethod = UP;
                return;
            case DOWN:
                this.roundMethod = DOWN;
                return;
            default:
                throw new InputExceptions(ROUND_INCORRECT_FORMAT_MESSAGE);
        }
    }

    /**
     * Calculates the brightness value of a character based on its boolean representation.
     * @param c The character whose brightness is to be calculated.
     * @return The normalized brightness value.
     */
    private Double getCharBrightnessValue(char c) {
        boolean[][] boolMatrix = CharConverter.convertToBoolArray(c);
        int numTrueCells = getNumTrueCells(boolMatrix);
        double value = (double) numTrueCells / NUM_CELLS;
        if (value < 0 || value > 1) {
            System.out.println("value < 0 or value > 1");
        }
        return value;
    }

    /**
     * Counts the number of true cells in a boolean representation of a character.
     * @param boolMatrix 2D boolean array representing the character.
     * @return The count of true cells.
     */
    private int getNumTrueCells(boolean[][] boolMatrix) {
        int counter = 0;

        for (boolean[] row : boolMatrix) {
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
            updateMax(value);
            minOrMaxUpdated = true;
        } else if (value < minValue) {
            updateMin(value);
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
