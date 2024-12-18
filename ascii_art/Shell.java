package ascii_art;

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
 * - how to save the brightnessMatrix? already in AsciiAlgo.run()
 * - how to save the padded image? already in AsciiAlgo.run()
 * - add Exceptions
 * - check the print format
 */

/** idea:
 * add BrightnessMatrix ArrayList field in shell.
 * add BrightnessMatrix ArrayList parameter in Algo constructor.
 * in algo, when you calculate it, add to the array.
 * in shell, get it from the last place in the array
 */

public class Shell {
    // TODO MAAYAN: switch to range instead
    private static final char ONE = '1';
    private static final char TWO = '2';
    private static final char THREE = '3';
    private static final char FOUR = '4';
    private static final char FIVE = '5';
    private static final char SIX = '6';
    private static final char SEVEN = '7';
    private static final char EIGHT = '8';
    private static final char NINE = '9';
    private static final char ZERO = '0';
    
    private static final int DEFAULT_RESOLUTION = 2;
    
    private static final String PREFIX_MESSAGE = ">>> ";
    private static final String EXIT_MESSAGE = "exit"; // Maayan
    private static final String CHARS_MESSAGE = "chars"; // Maayan
    private static final String ADD_MESSAGE = "add"; // Maayan
    private static final String REMOVE_MESSAGE = "remove"; // Maayan
    private static final String RES_MESSAGE = "res"; // Rotem - DONE
    private static final String ROUND_MESSAGE = "round"; // Maayan
    private static final String OUTPUT_MESSAGE = "output"; // Rotem - DONE
    private static final String ASCII_ART_MESSAGE = "asciiArt"; // Rotem
    private static final String WRONG_NUM_ARGS_ERROR =
            "Wrong number of arguments. Should get 1 argument: imagePath.";
    private static final String SPACE_AND_ETC = "(\\s.*)?";
    private static final int ARGS_NUM = 1;

    // fields:
    private Memento memento;
    private ArrayList<BrightnessMatrix> brightnessMatrices;


    private String outputPath;
    private SortedSet<Character> sortedChars;
    private int resolution;
    private Image image;



    public  Shell() {
        this.outputPath = null;
        this.sortedChars = new TreeSet<>();
        this.resolution = DEFAULT_RESOLUTION;
        this.brightnessMatrices = new ArrayList<BrightnessMatrix>();
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

    private void testAsciiArtAlgorithm(){
        System.out.println("testAsciiArtAlgorithm");
        try {
            Image image = new Image("examples/board.jpeg");
            AsciiArtAlgorithm algo = new AsciiArtAlgorithm(image, DEFAULT_RESOLUTION, new char[]{'m', 'o'}, new ArrayList<>());
            char[][] newPhoto = algo.run();
            for (char[] row: newPhoto){
                for (char c: row) {
                    System.out.println(c);
                }
            }
        } catch (Exception e) {
            System.out.println("error: "+ e);
        }
    }

    private void testHandleChars() {
        initializeDefaultChars();
        handleCharsInput();
    }

    private void initializeDefaultChars() {
        for (char c = ZERO; c <= NINE; c++) {
            sortedChars.add(c);
        }
    }

    private void handleInput(String input) {
        if (input.equals(EXIT_MESSAGE)) {
            return;
        } else if (input.matches(CHARS_MESSAGE + SPACE_AND_ETC)) {
            handleCharsInput();
        }

    }

    private void printAsciiArt(char[][] finalImage) {
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
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
                for (int i = 0; i < finalImage.length; i++) {
                    writer.write(new String(finalImage[i]));
                    writer.newLine();
                }
            } catch (IOException e) {
                // TODO: deal with exceptions
                e.printStackTrace();
            }
        }
    }

    private void handleAsciiArt() {
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

        char[] charsArray = setToArray(this.sortedChars);

        AsciiArtAlgorithm asciiArtAlgorithm =
                new AsciiArtAlgorithm(this.image, this.resolution, charsArray, this.brightnessMatrices);
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

    private char[] setToArray(SortedSet<Character> chars) {
        char[] charsArray = new char[chars.size()];
        int i = 0;
        for (Character c : chars) {
            charsArray[i++] = c;
        }
        return charsArray;
    }

    private void handleRound() {
    }

    private void handleOutput(String[] commandTokens) {
        if (commandTokens.length < 2) {
            System.out.println("Did not change output method due to incorrect format.");
            return;
        }
        switch (commandTokens[1]){
            case "html":
                this.outputPath = "out.html";
                return;
            case "console":
                this.outputPath = null;
                return;
            default:
                System.out.println("Did not change output method due to incorrect format.");
        }

    }

    private void handleRes(String[] commandTokens) {
        ImageProcessor imageProcessor = new ImageProcessor();
        int extendedWidth = imageProcessor.extendImage(image).getWidth();
        int extendedHeight = imageProcessor.extendImage(image).getHeight();

        int maxRes = extendedWidth;
        int minRes = Math.max(1, extendedWidth/extendedHeight);

        if (commandTokens.length > 1) {
            switch (commandTokens[1]) {
                case "up":
                    if (this.resolution * 2 <= maxRes) {
                        this.resolution *= 2;
                        break;
                    } else {
                        System.out.println("Did not change resolution due to exceeding boundaries.");
                        return;
                    }
                case "down":
                    if (this.resolution / 2 >= minRes) {
                        this.resolution /= 2;
                        break;
                    } else {
                        System.out.println("Did not change resolution due to exceeding boundaries.");
                        return;
                    }
                default:
                    System.out.println("Did not change resolution due to incorrect format.");
                    return;
            }
        }
        System.out.println("Resolution set to " + this.resolution + ".");
        // TODO: i dont get 2.6.5
    }

    private void handleRemove() {
    }

    private void handleCharsInput() {
        for (char c : sortedChars) {
            System.out.print(c + " ");
        }
    }
    
    private void handleAdd() {
    }

    private void handleExit() {
    }


    private char[] createBasicCharsSet() {
        // TODO MAAYAN: maybe change to array<>
        return new char[] {ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE};
    }

    public void run(String imageName){
        try {
            this.image = new Image(imageName);
        }
        catch (Exception e) {}

        // TODO: how can i save the padded image? how can i save the brightness image?


        char[] chars = createBasicCharsSet();

        System.out.println(PREFIX_MESSAGE);
        while (true) {
            String input = KeyboardInput.readLine();
            String[] commandTokens = input.split(" ");

            if (commandTokens.length==0) {
                System.out.println("Did not execute due to incorrect command.");
            }
//            handleInput(input);
            switch (commandTokens[0]) {
                case EXIT_MESSAGE:
                    handleExit();
                    return;
                case CHARS_MESSAGE:
                    handleCharsInput();
                    break;
                case ADD_MESSAGE:
                    handleAdd();
                    break;
                case REMOVE_MESSAGE:
                    handleRemove();
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
                    handleAsciiArt();
                    break;
                default:
                    System.out.println("Did not execute due to incorrect command.");
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
        shell.testHandleChars();
        if (args.length != ARGS_NUM) {
            throw new RuntimeException(WRONG_NUM_ARGS_ERROR);
        }
        String imagePath = args[0];

        try {
            Image image = new Image(imagePath);
        } catch (Exception e) {
            System.out.println("main error: "+ e);
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
