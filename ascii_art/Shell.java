package ascii_art;

import ascii_art.exceptions.AsciiArtExceptions;
import ascii_art.exceptions.InputExceptions;
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

import static java.lang.Math.max;

/**
 * TODO:
 * - add Exceptions
 * - check the print format
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

    // Exceptions messages:
    private static final String OUTPUT_FAILED_MESSAGE = "Fail to open the file";
    private static final String OUTPUT_INCORRECT_FORMAT_MESSAGE = "Did not change output method due to incorrect format.";
    private static final String FAILS_TO_OPEN_IMAGE_MESSAGE = "Failed to open the image";
    private static final String INVALID_COMMAND_MESSAGE = "Did not execute due to incorrect command.";
    private static final String INVALID_RES_MESSAGE = "Did not change resolution due to exceeding boundaries.";
    private static final String RES_INCORRECT_FORMAT_MESSAGE = "Did not change resolution due to incorrect format.";
    private static final String ADD_INVALID_COMMAND_MESSAGE = "Did not add due to incorrect format.";
    private static final String REMOVE_INVALID_COMMAND_MESSAGE = "Did not remove due to incorrect format.";
    private static final int ALL_CHARS_LEN = 95;
    private static final int SPACE_CHAR = 32;
    private static final int RES_FACTOR_DOWN = 2;
    private static final int RES_FACTOR_UP = 2;

    // fields:
    private Memento memento;
    private ArrayList<BrightnessMatrix> brightnessMatrices;


    private String outputPath;
    private SortedSet<Character> sortedChars;
    private int resolution;
    private Image image;
    private SubImgCharMatcher subImgCharMatcher;


    public  Shell() {
        this.outputPath = null;
        this.sortedChars = new TreeSet<>();
        this.resolution = DEFAULT_RESOLUTION;
        this.brightnessMatrices = new ArrayList<BrightnessMatrix>();
        initializeDefaultChars();
        this.subImgCharMatcher = new SubImgCharMatcher(sortedSetToArray(sortedChars));
    }

    private void testSubImgCharMatcher() {
        char[] chars = {'o', 'm'};
        SubImgCharMatcher charMatcher = new SubImgCharMatcher(chars);
        int N = 10;
        System.out.println(chars);
        for (int i = 0; i < N + 1; i++) {
            System.out.println(charMatcher.getCharByImageBrightness(i/(double)N));
        }
        charMatcher.addChar('s');
        charMatcher.addChar('n');
        chars = new char[]{'o', 'm', 's', 'n'};
        System.out.println(chars);
        for (int i = 0; i < N + 1; i++) {
            System.out.println(charMatcher.getCharByImageBrightness(i/(double)N));
        }
        charMatcher.removeChar('o');
        chars = new char[]{'m', 's', 'n'};
        System.out.println(chars);
        for (int i = 0; i < N + 1; i++) {
            System.out.println(charMatcher.getCharByImageBrightness(i/(double)N));
        }
    }

//    private void testAsciiArtAlgorithm(){
//        System.out.println("testAsciiArtAlgorithm");
//        try {
//            Image image = new Image("examples/board.jpeg");
//            AsciiArtAlgorithm algo = new AsciiArtAlgorithm(image, DEFAULT_RESOLUTION, new char[]{'m', 'o'}, new ArrayList<>());
//            char[][] newPhoto = algo.run();
//            for (char[] row: newPhoto){
//                for (char c: row) {
//                    System.out.println(c);
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("error: "+ e);
//        }
//    }

    private void testHandleChars() {
        initializeDefaultChars();
        handleCharsInput();
    }

    private void initializeDefaultChars() {
        for (char c = DEFAULT_CHARS_START; c <= DEFAULT_CHAR_END; c++) {
            sortedChars.add(c);
        }
    }
    
    private void printAsciiArt(char[][] finalImage) throws IOException {
        // TODO: check print format
        // print to the console:
        if (this.outputPath==null) {
            for (int i = 0; i < finalImage.length; i++) {
                for (int j = 0; j < finalImage[i].length; j++) {
                    System.out.print(finalImage[i][j]);
                }
                System.out.println();
            }
        }
        // print to a file:
        else {
            try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
                for (int i = 0; i < finalImage.length; i++) {
                    writer.write(new String(finalImage[i]));
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new IOException(OUTPUT_FAILED_MESSAGE);
            }
        }
    }

    private void handleAsciiArt() throws IOException {
        // TODO: delete the prints
        // check if BrightnessMatrix needs to be calculated:
        if (memento != null && memento.wasCalculated(this.resolution, this.image)) {
            System.out.println("Using cached brightness matrix.");
        } else {
            // No matching cached matrix; create a new one and save it in the memento
            System.out.println("Calculating new brightness matrix.");
            BrightnessMatrix newMatrix = createBrightnessMatrix();
            this.brightnessMatrices.add(newMatrix);
            memento = new Memento(newMatrix, this.resolution, this.image);
        }

        char[] charsArray = sortedSetToArray(this.sortedChars);

        AsciiArtAlgorithm asciiArtAlgorithm =
                new AsciiArtAlgorithm(this.image, this.resolution, this.subImgCharMatcher,
                        this.brightnessMatrices);
        char[][] finalImage = asciiArtAlgorithm.run();

        printAsciiArt(finalImage);

    }

    private BrightnessMatrix createBrightnessMatrix() {
        ImageProcessor imageProcessor = new ImageProcessor();
        // Padd:
        Image newImage = imageProcessor.extendImage(this.image);
        // Divide the image according to given resolution:
        Image[][] subImages = imageProcessor.divideImage(newImage, this.resolution);
        // Create BrightnessMatrix:
        return new BrightnessMatrix(subImages);
    }

    private char[] sortedSetToArray(SortedSet<Character> chars) {
        char[] charsArray = new char[chars.size()];
        int i = 0;
        for (Character c : chars) {
            charsArray[i++] = c;
        }
        return charsArray;
    }

    private void handleRound() {
    }

    private void handleOutput(String[] commandTokens) throws InputExceptions{
        if (commandTokens.length < 2) {
            throw new InputExceptions(OUTPUT_INCORRECT_FORMAT_MESSAGE);
        }
        switch (commandTokens[1]){
            case "html":
                this.outputPath = "out.html";
                return;
            case "console":
                this.outputPath = null;
                return;
            default:
                throw new InputExceptions(OUTPUT_INCORRECT_FORMAT_MESSAGE);
        }

    }

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
        // TODO: i dont get 2.6.5
    }


    private void handleCharsInput() {
        for (char c : sortedChars) {
            System.out.print(c + " ");
        }
        System.out.println();
    }


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


    private char[] createCharArr(String input) throws InputExceptions {
        // TODO: check about " -~"
        if (!input.matches(".-.")) {
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

    private char[] createAllCharsArray() {
        char[] asciiArray = new char[ALL_CHARS_LEN]; // 126 - 32 + 1 = 95 characters
        for (int i = 0; i < asciiArray.length; i++) {
            asciiArray[i] = (char)(i + SPACE_CHAR);
        }
        return asciiArray;
    }

    private void addToChars(char[] chars) {
        for (char c : chars) {
            this.sortedChars.add(c);
            this.subImgCharMatcher.addChar(c);
        }
    }


    private void removeFromChars(char[] chars) {

        for (char c : chars) {
            sortedChars.remove(c);
            this.subImgCharMatcher.removeChar(c);
        }
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
                        handleRound();
                        break;
                    case ASCII_ART_MESSAGE:
                        try {
                            handleAsciiArt();
                        } catch (Exception _) {
                            // TODO: how to handle it?
                        }
                        break;
                    default:
                        throw new InputExceptions(INVALID_COMMAND_MESSAGE);
                }
            } catch (InputExceptions e) {
                System.out.println(e.getMessage());
                System.out.println(PREFIX_MESSAGE);
                continue;
            }

            System.out.println(PREFIX_MESSAGE);

        }
    }



    public static void main(String[] args) {
        // Create an instance of Shell and call the test method
        Shell shell = new Shell();
//        shell.testSubImgCharMatcher();
//        shell.testPadAndExtend();
//        shell.testAsciiArtAlgorithm();
//        shell.testHandleChars();
        if (args.length != ARGS_NUM) {
            throw new AsciiArtExceptions(WRONG_NUM_ARGS_ERROR);
        }
        String imagePath = args[0];

        // TODO: EXCEPTION
        try {
            Image image = new Image(imagePath);
        } catch (Exception e) {
            throw new AsciiArtExceptions(FAILS_TO_OPEN_IMAGE_MESSAGE);
        }

        shell.run(imagePath);
    }

    public static class Memento {
        private  final BrightnessMatrix matrix;
        private final int resolution;
        private final int imageHash;


        public Memento(BrightnessMatrix matrix, int resolution, Image image) {
            this.matrix = matrix;
            this.resolution = resolution;
            this.imageHash = image.hashCode();
        }

        public boolean wasCalculated(int resolution, Image image) {
            return this.resolution==resolution && this.imageHash==image.hashCode();
        }

        public BrightnessMatrix getMatrix() {
            return this.matrix;
        }


    }
}
