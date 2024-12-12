package image_char_matching;
import java.util.*;

// TODO

/**
 * 1. add priority to ascii
 */


public class SubImgCharMatcher {

    private final int NUM_CELLS = 16;
    private Map<Character, Double> charMap;
    private TreeMap<Double, Character> sortedCharMap;
    private double minValue;
    private double maxValue;
    private int numTimesMaxValue;
    private int numTimesMinValue;



    public SubImgCharMatcher(char[] charset) {
        this.charMap = new HashMap<>();
        this.sortedCharMap = new TreeMap<>();

        minValue = Double.POSITIVE_INFINITY;
        maxValue = Double.NEGATIVE_INFINITY;

        for (char c : charset) {
            double value = 0;
            try {
                value = getCharBrightnessValue(c);
                updateMin(value);
                updateMax(value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            this.charMap.put(c, value);
        }
        createTree();
    }

    private void updateMax(double value) {
        if (value > maxValue) {
            maxValue = value;
            numTimesMaxValue = 1;
        } else if (value == maxValue) {
            numTimesMaxValue++;
        }
    }

    private void updateMin(double value) {
        if (value < minValue) {
            minValue = value;
            numTimesMinValue = 1;
        } else if (value == minValue) {
            numTimesMinValue++;
        }
    }

    public char getCharByImageBrightness(double brightness) {
        // Get the closest lower or equal entry (floor) in the TreeMap
        Map.Entry<Double, Character> floorEntry = sortedCharMap.floorEntry(brightness);

        // Get the closest higher or equal entry (ceiling) in the TreeMap
        Map.Entry<Double, Character> ceilingEntry = sortedCharMap.ceilingEntry(brightness);

        // Handle cases where only one of the entries exists
        if (floorEntry == null) return ceilingEntry.getValue(); // No lower bound
        if (ceilingEntry == null) return floorEntry.getValue(); // No upper bound

        // Compare distances to determine the closest match
        double floorDiff = Math.abs(floorEntry.getKey() - brightness);
        double ceilingDiff = Math.abs(ceilingEntry.getKey() - brightness);

        return (floorDiff <= ceilingDiff) ? floorEntry.getValue() : ceilingEntry.getValue();
    }


    private Double getCharBrightnessValue(char c) throws Exception {
        boolean[][] boolArray = CharConverter.convertToBoolArray(c);
        return getNumTrueCells(boolArray);
    }

    private double getNumTrueCells(boolean[][] boolArray) throws Exception {
        int counter = 0;
        int numCells = NUM_CELLS * NUM_CELLS;

        for (boolean[] row : boolArray) {
            for (boolean cell : row) {
                if (cell) {
                    counter++;
                }
            }
        }
        System.out.println(counter + " " + numCells);

        double value = (double) counter / numCells;
        if (value < 0 || value > 1 ) {
            throw new Exception("value < 0 or value > 1");
        }
        return counter;
    }

    private void createTree() {
        for (Map.Entry<Character, Double> entry : charMap.entrySet()) {
            double normalizedBrightness = (entry.getValue() - minValue) / (maxValue - minValue);
            sortedCharMap.put(normalizedBrightness, entry.getKey());
            System.out.println(entry.getKey() + "   " + normalizedBrightness);
        }
    }

    public void addChar(char c) throws Exception {
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
        } else if (value == maxValue) {
            numTimesMaxValue++;
        } else if (value == minValue) {
            numTimesMinValue++;
        }

        // Recreate the TreeMap if min or max was updated
        if (minOrMaxUpdated) {
            sortedCharMap.clear();
            createTree();
        } else {
            // Add the new normalized value to the TreeMap
            double normalizedBrightness = (value - minValue) / (maxValue - minValue);
            sortedCharMap.put(normalizedBrightness, c);
        }
    }


    public void removeChar(char c) {
        Double value = charMap.remove(c);

        // If the character doesn't exist, do nothing
        if (value == null) {
            return;
        }

        // Handle cases where minValue or maxValue is affected
        if (value.equals(maxValue)) {
            numTimesMaxValue--;
            if (numTimesMaxValue == 0) {
                // Recompute maxValue
                maxValue = Double.NEGATIVE_INFINITY;
                for (double v : charMap.values()) {
                    updateMax(v);
                }
            }
        }

        if (value.equals(minValue)) {
            numTimesMinValue--;
            if (numTimesMinValue == 0) {
                // Recompute minValue
                minValue = Double.POSITIVE_INFINITY;
                for (double v : charMap.values()) {
                    updateMin(v);
                }
            }
        }

        // Recreate the TreeMap
        sortedCharMap.clear();
        createTree();
    }

}
