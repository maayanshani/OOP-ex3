package ascii_art;

import ascii_art.exceptions.*;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.BrightnessMatrix;
import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * The Shell class provides a command-line interface for creating and manipulating ASCII art from images.
 * It handles user input, image processing, character set management, and output generation.
 *
 * The class supports the following commands:
 * - exit: Exits the program
 * - chars: Displays current character set
 * - add [chars]: Adds characters to the set (single char, range a-z, 'all', or 'space')
 * - remove [chars]: Removes characters from the set
 * - res [up/down]: Adjusts resolution
 * - output [console/html]: Sets output method
 * - round: Sets rounding method for brightness calculations
 * - asciiArt: Generates and displays ASCII art
 *
 * Features:
 * - Caches brightness matrices using the Memento pattern for performance
 * - Supports both console and HTML output formats
 * - Manages a sorted set of characters for ASCII art generation
 * - Provides dynamic resolution control with bounds checking
 * - Implements comprehensive error handling
 *
 * Usage example:
 * Shell shell = new Shell();
 * shell.run("path/to/image.jpg");
 *
 * @see Image
 * @see BrightnessMatrix
 * @see SubImgCharMatcher
 */
public class Shell {
    private static final char DEFAULT_CHARS_START = '0';
    private static final char DEFAULT_CHAR_END = '9';
    private static final int DEFAULT_RESOLUTION = 2;
    
    private static final String PREFIX_MESSAGE = ">>> ";
    private static final String EXIT_MESSAGE = "exit"; // Rotem - Done
    private static final String CHARS_MESSAGE = "chars"; // Rotem - Done
    private static final String ADD_MESSAGE = "add"; // Maayan
    private static final String REMOVE_MESSAGE = "remove"; // Maayan - Done
    private static final String RES_MESSAGE = "res"; // Rotem - DONE
    private static final String ROUND_MESSAGE = "round"; // Maayan
    private static final String OUTPUT_MESSAGE = "output"; // Rotem - Done
    private static final String ASCII_ART_MESSAGE = "asciiArt"; // Rotem - Done
    private static final String WRONG_NUM_ARGS_ERROR =
            "Wrong number of arguments. Should get 1 argument: imagePath.";
    private static final int ARGS_NUM = 1;
    private static final int ALL_CHARS_LEN = 95;
    private static final int SPACE_CHAR = 32;
    private static final int RES_FACTOR_DOWN = 2;
    private static final int RES_FACTOR_UP = 2;
    private static  final String OUTPUT_OPTION_HTML = "html";
    private static  final String OUTPUT_OPTION_CONSOLE = "console";
    private static final String HTML_FILE_PATH = "out.html";

    // Exceptions messages:
    private static final String OUTPUT_FAILED_MESSAGE = "Fail to open the file";
    private static final String OUTPUT_INCORRECT_FORMAT_MESSAGE =
            "Did not change output method due to incorrect format.";
    private static final String FAILS_TO_OPEN_IMAGE_MESSAGE = "Failed to open the image";
    private static final String INVALID_COMMAND_MESSAGE = "Did not execute due to incorrect command.";
    private static final String INVALID_RES_MESSAGE =
            "Did not change resolution due to exceeding boundaries.";
    private static final String RES_INCORRECT_FORMAT_MESSAGE =
            "Did not change resolution due to incorrect format.";
    private static final String ADD_INVALID_COMMAND_MESSAGE = "Did not add due to incorrect format.";
    private static final String REMOVE_INVALID_COMMAND_MESSAGE = "Did not remove due to incorrect format.";
    private static final String IMAGE_PROCESSOR_ERROR_MESSAGE = "Error in processing the image: ";
    private static final String CHAR_MATCHER_ERROR_MESSAGE = "Error in SubImgCharMatcher: ";
    private static final String FILE_FONT_NAME = "New Courier";
    private static final int FAILURE = 1;
    private static final String WRONG_SIZE_SET_ERROR = "Did not execute. Charset is too small.";
    private static final String CHAR_FORMAT_REGEX = ".-.";

    // fields:
    /**
     * A cached state of the BrightnessMatrix, its resolution, and the associated image.
     * Used to avoid recalculating the matrix if the resolution and image remain unchanged.
     */
    private Memento memento;

    /**
     * A list of all generated BrightnessMatrix objects.
     * Stores brightness data for multiple resolutions or states.
     */
    private ArrayList<BrightnessMatrix> brightnessMatrices;

    /**
     * The file path where the ASCII art output should be written.
     * If null, the output will be printed to the console.
     */
    private String outputPath;

    /**
     * A sorted set of characters available for generating ASCII art.
     * Characters are stored in ascending order for consistency in brightness mapping.
     */
    private SortedSet<Character> sortedChars;

    /**
     * The resolution of the ASCII art output.
     * Determines the size of the grid used to divide the image for processing.
     */
    private int resolution;

    /**
     * The image being processed to generate ASCII art.
     * Contains the pixel data of the input image.
     */
    private Image image;

    /**
     * A utility class used for matching characters to image brightness levels.
     * Facilitates the mapping of brightness values to corresponding characters.
     */
    private SubImgCharMatcher subImgCharMatcher;


    /**
     * Constructs a Shell instance with default parameters.
     * Initializes the sorted character set, resolution, and other configurations.
     */
    public Shell() {
        this.outputPath = null;
        this.sortedChars = new TreeSet<>();
        this.resolution = DEFAULT_RESOLUTION;
        this.brightnessMatrices = new ArrayList<BrightnessMatrix>();
        initializeDefaultChars();
        this.subImgCharMatcher = new SubImgCharMatcher(sortedSetToArray(sortedChars));
    }

    /**
     * Initializes the default set of characters to the digits 0-9.
     * These characters will be added to the sorted set for use in ASCII art generation.
     */
    private void initializeDefaultChars() {
        for (char c = DEFAULT_CHARS_START; c <= DEFAULT_CHAR_END; c++) {
            sortedChars.add(c);
        }
    }

    /**
     * Prints the generated ASCII art either to the console or to a specified file.
     *
     * @param finalImage The ASCII art represented as a 2D character array.
     * @throws IOException if writing to a file fails.
     */
    private void printAsciiArt(char[][] finalImage) throws IOException {
        // print to the console:
        if (this.outputPath==null) {
            ConsoleAsciiOutput consoleAsciiOutput = new ConsoleAsciiOutput();
            consoleAsciiOutput.out(finalImage);
        }

        // print to a file:
        else {
            HtmlAsciiOutput htmlAsciiOutput = new HtmlAsciiOutput(HTML_FILE_PATH, FILE_FONT_NAME);
            htmlAsciiOutput.out(finalImage);
        }
    }

    /**
     * Converts a SortedSet of characters into an array.
     *
     * @param chars The SortedSet of characters to convert.
     * @return A character array containing all characters in the SortedSet.
     */
    private char[] sortedSetToArray(SortedSet<Character> chars) {
        char[] charsArray = new char[chars.size()];
        int i = 0;
        for (Character c : chars) {
            charsArray[i++] = c;
        }
        return charsArray;
    }

    // 2.2
    /**
     * Prints the current set of characters available for ASCII art generation in a single line.
     * Each character is separated by a space. If the set is empty, nothing is printed.
     */
    private void handleCharsInput() {
        for (char c : sortedChars) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    // 2.3 + 2.4
    /**
     * Handles the addition or removal of characters from the sorted character set
     * based on the given command tokens.
     *
     * @param commandTokens The command tokens provided by the user, containing the operation and parameters.
     * @param add           A boolean flag indicating whether to add (true) or remove (false) characters.
     * @throws InputExceptions if the input format is invalid or cannot be processed.
     */
    private void handleAddOrRemove(String[] commandTokens, boolean add) throws InputExceptions {
        try {
            char[] charsArray;

            switch (commandTokens[1]) {
                case "all":
                    charsArray = createAllCharsArray(); // `null` indicates "all"
                    break;

                case "space":
                    charsArray = new char[]{' '}; // Single space character
                    break;

                default:
                    if (commandTokens[1].length() == 1) {
                        charsArray = new char[]{commandTokens[1].charAt(0)};
                    } else if (commandTokens[1].length() == 3) {
                        charsArray = createCharArr(commandTokens[1]);
                    } else {
                        throw new InputExceptions(ADD_INVALID_COMMAND_MESSAGE);
                    }
                    break;
            }

            // Perform add or remove action
            if (add) {
                addToChars(charsArray);
            } else {
                removeFromChars(charsArray);
            }
        }
        catch (Exception e) {
            if (add) {
                throw new InputExceptions(ADD_INVALID_COMMAND_MESSAGE);
            }
            else {
                throw new InputExceptions(REMOVE_INVALID_COMMAND_MESSAGE);
            }
        }
    }

    /**
     * Creates an array of characters based on a given range input string (e.g., "a-z").
     *
     * @param input The input string representing a character range in the format "a-z".
     * @return A character array containing all characters in the specified range.
     * @throws InputExceptions if the input format is invalid or cannot be parsed into a valid range.
     */
    private char[] createCharArr(String input) throws InputExceptions {
        if (!input.matches(CHAR_FORMAT_REGEX)) {
            throw new InputExceptions(ADD_INVALID_COMMAND_MESSAGE);
        }

        char firstChar = (char) Math.min(input.charAt(0), input.charAt(2));
        char lastChar = (char) Math.max(input.charAt(0), input.charAt(2));
        int arrayLength = Math.abs(firstChar - lastChar) + 1;

        char[] charsArray = new char[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            charsArray[i] = (char) (firstChar + i);
        }
        return charsArray;

    }

    /**
     * Creates an array of all printable ASCII characters (from SPACE to ~).
     *
     * @return A character array containing all printable ASCII characters.
     */
    private char[] createAllCharsArray() {
        char[] asciiArray = new char[ALL_CHARS_LEN]; // 126 - 32 + 1 = 95 characters
        for (int i = 0; i < asciiArray.length; i++) {
            asciiArray[i] = (char)(i + SPACE_CHAR);
        }
        return asciiArray;
    }

    // 2.3
    /**
     * Adds a set of characters to the existing sorted character set and updates the character matcher.
     *
     * @param chars The array of characters to add to the sorted set.
     */
    private void addToChars(char[] chars) {
        for (char c : chars) {
            this.sortedChars.add(c);
            this.subImgCharMatcher.addChar(c);
        }
    }

    // 2.4
    /**
     * Removes a set of characters from the existing sorted character set and updates the character matcher.
     *
     * @param chars The array of characters to remove from the sorted set.
     */
    private void removeFromChars(char[] chars) {

        for (char c : chars) {
            sortedChars.remove(c);
            this.subImgCharMatcher.removeChar(c);
        }
    }

    // 2.5
    /**
     * Adjusts the resolution of the image based on the user's input command.
     * Valid commands are "up" to increase resolution or "down" to decrease resolution.
     *
     * @param commandTokens The command tokens provided by the user.
     * @throws InputExceptions if the resolution adjustment exceeds boundaries or the format is invalid.
     */
    private void handleRes(String[] commandTokens) throws InputExceptions {
        ImageProcessor imageProcessor = new ImageProcessor();
        int extendedWidth = imageProcessor.extendImage(image).getWidth();
        int extendedHeight = imageProcessor.extendImage(image).getHeight();

        int maxRes = extendedWidth;
        int minRes = Math.max(1, extendedWidth/extendedHeight);

        if (commandTokens.length > 1) {
            switch (commandTokens[1]) {
                case "up":
                    if (this.resolution * RES_FACTOR_UP <= maxRes) {
                        this.resolution *= RES_FACTOR_UP;
                        break;
                    } else {
                        throw new InputExceptions(INVALID_RES_MESSAGE);
                    }
                case "down":
                    if (this.resolution / RES_FACTOR_DOWN >= minRes) {
                        this.resolution /= RES_FACTOR_DOWN;
                        break;
                    } else {
                        throw new InputExceptions(INVALID_RES_MESSAGE);
                    }
                default:
                    throw new InputExceptions(RES_INCORRECT_FORMAT_MESSAGE);
            }
        }
        System.out.println("Resolution set to " + this.resolution + ".");
    }

    // 2.6
    /**
     * Sets the output method for the ASCII art, either to the console or an HTML file.
     *
     * @param commandTokens The command tokens provided by the user, containing the output method.
     * @throws InputExceptions if the output method format is invalid.
     */
    private void handleOutput(String[] commandTokens) throws InputExceptions{
        if (commandTokens.length < 2) {
            throw new InputExceptions(OUTPUT_INCORRECT_FORMAT_MESSAGE);
        }
        switch (commandTokens[1]){
            case OUTPUT_OPTION_HTML:
                this.outputPath = HTML_FILE_PATH;
                return;
            case OUTPUT_OPTION_CONSOLE:
                this.outputPath = null;
                return;
            default:
                throw new InputExceptions(OUTPUT_INCORRECT_FORMAT_MESSAGE);
        }
    }

    // 2.7
    /**
     * Handles the rounding method for brightness calculations based on user input.
     * (Placeholder for implementation details).
     */
    private void handleRound(String[] commandTokens) throws InputExceptions {
        if (commandTokens.length < 2) {
            throw new InputExceptions(OUTPUT_INCORRECT_FORMAT_MESSAGE);
        }
        this.subImgCharMatcher.setRoundMethod(commandTokens[1]);
    }

    // 2.8
    /**
     * Generates ASCII art based on the current image, resolution, and character set.
     * Prints the result either to the console or saves it to a file based on the output configuration.
     *
     * @throws IOException if writing to a file fails.
     */
    private void handleAsciiArt() throws IOException, CharSetExceptions {
        if (sortedChars.size() <= 1) {
            throw new CharSetExceptions(WRONG_SIZE_SET_ERROR);
        }
        // check if BrightnessMatrix needs to be calculated:
        if (this.memento != null && memento.wasCalculated(this.resolution, this.image)) {
            this.brightnessMatrices.add(memento.getMatrix());

        } else {
            // No matching cached matrix; create a new one and save it in the memento
            BrightnessMatrix newMatrix = createBrightnessMatrix();
            this.brightnessMatrices.add(newMatrix);
            this.memento = new Memento(newMatrix, this.resolution, this.image);
        }

        AsciiArtAlgorithm asciiArtAlgorithm =
                new AsciiArtAlgorithm(this.image, this.resolution, this.subImgCharMatcher,
                        this.brightnessMatrices);
        char[][] finalImage = asciiArtAlgorithm.run();

        printAsciiArt(finalImage);
    }

    /**
     * Creates a BrightnessMatrix for the current image by processing and dividing it into sub-images
     * based on the current resolution.
     *
     * @return A new BrightnessMatrix object.
     */
    private BrightnessMatrix createBrightnessMatrix() {
        ImageProcessor imageProcessor = new ImageProcessor();
        // Padd:
        Image newImage = imageProcessor.extendImage(this.image);
        // Divide the image according to given resolution:
        Image[][] subImages = imageProcessor.divideImage(newImage, this.resolution);
        // Create BrightnessMatrix:
        return new BrightnessMatrix(subImages);
    }

    public void run(String imageName){
        try {
            this.image = new Image(imageName);
        } catch (IOException e) {
            System.out.println(e + FAILS_TO_OPEN_IMAGE_MESSAGE);
        }

        System.out.println(PREFIX_MESSAGE);
        while (true) {
            String input = KeyboardInput.readLine();
            String[] commandTokens = input.split(" ");

            try {
                if (commandTokens.length==0) {
                    throw new InputExceptions(INVALID_COMMAND_MESSAGE);
                }
                switch (commandTokens[0]) {
                    case EXIT_MESSAGE:
                        System.exit(0);
                    case CHARS_MESSAGE:
                        handleCharsInput();
                        break;
                    case ADD_MESSAGE:
                        handleAddOrRemove(commandTokens, true);
                        break;
                    case REMOVE_MESSAGE:
                        handleAddOrRemove(commandTokens, false);
                        break;
                    case RES_MESSAGE:
                        handleRes(commandTokens);
                        break;
                    case OUTPUT_MESSAGE:
                        handleOutput(commandTokens);
                        break;
                    case ROUND_MESSAGE:
                        handleRound(commandTokens);
                        break;
                    case ASCII_ART_MESSAGE:
                        handleAsciiArt();
                        break;
                    default:
                        throw new InputExceptions(INVALID_COMMAND_MESSAGE);
                }
            } catch (InputExceptions | CharSetExceptions e) {
                System.out.println(e.getMessage());
                System.out.println(PREFIX_MESSAGE);
                continue;
            } catch (ImageProcessorExceptions e) {
                System.out.println(IMAGE_PROCESSOR_ERROR_MESSAGE + e.getMessage());
                System.exit(1);
            } catch (CharMatcherExceptions e) {
                System.out.println(CHAR_MATCHER_ERROR_MESSAGE + e.getMessage());
                System.exit(FAILURE);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(FAILURE);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.exit(FAILURE);
            }

            System.out.println(PREFIX_MESSAGE);
        }
    }

    /**
     * The main method to run the program
     */
    public static void main(String[] args) {
        Shell shell = new Shell();
        if (args.length != ARGS_NUM) {
            throw new AsciiArtExceptions(WRONG_NUM_ARGS_ERROR);
        }
        String imagePath = args[0];
        shell.run(imagePath);
    }

    /**
     * The Memento class is part of the Memento design pattern, used to store the state of an object
     * (specifically a BrightnessMatrix, its resolution, and the hash of an associated Image)
     * so it can be restored later.
     */
    public static class Memento {
        private final BrightnessMatrix matrix; // The brightness matrix associated with the image.
        private final int resolution;         // The resolution of the saved brightness matrix.
        private final int imageHash;          // A hash uniquely identifying the image.

        /**
         * Constructs a Memento object that captures the state of a BrightnessMatrix, its resolution,
         * and the hash of an associated Image.
         *
         * @param matrix    The brightness matrix of the image.
         * @param resolution The resolution at which the brightness matrix was calculated.
         * @param image      The image whose state is being saved, identified by its hash.
         */
        public Memento(BrightnessMatrix matrix, int resolution, Image image) {
            this.matrix = matrix;
            this.resolution = resolution;
            this.imageHash = image.hashCode();
        }

        /**
         * Checks if the Memento matches the given resolution and image by comparing
         * the resolution and the hash of the image.
         *
         * @param resolution The resolution to compare with the saved resolution.
         * @param image      The image to compare with the saved image using its hash.
         * @return True if the resolution and image hash match the saved state, otherwise false.
         */
        public boolean wasCalculated(int resolution, Image image) {
            return this.resolution == resolution && this.imageHash == image.hashCode();
        }

        /**
         * Retrieves the BrightnessMatrix stored in this Memento.
         *
         * @return The stored BrightnessMatrix object.
         */
        public BrightnessMatrix getMatrix() {
            return this.matrix;
        }
    }
}
