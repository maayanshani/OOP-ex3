package ascii_art;

import image.BrightnessMatrix;
import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

public class Shell {
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

    // fields:
    private String outputPath;
    private SortedSet<Character> sortedChars;
    private int resolution;
    private BrightnessMatrix lastBrightnessMatrix;
    private Image image;
    private boolean hasChanged;



    public  Shell(int resolution) {
        this.outputPath = null;
        this.sortedChars = new TreeSet<>();
        this.resolution = resolution;
        this.lastBrightnessMatrix = null;
        this.hasChanged = false;
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
            AsciiArtAlgorithm algo = new AsciiArtAlgorithm(image, DEFAULT_RESOLUTION, new char[]{'m', 'o'});
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

    private void handleAsciiArt() {
        // TODO: check with Maayan for the 3th parameter (instead of the method createBasicCharsSet)
        AsciiArtAlgorithm asciiArtAlgorithm =
                new AsciiArtAlgorithm(this.image, this.resolution, this.createBasicCharsSet());
        char[][] finalImage = asciiArtAlgorithm.run();

        // TODO: check print format
        // print to the console:
        if (this.outputPath==null) {
            for (int i = 0; i < finalImage.length; i++) {
                System.out.println(finalImage[i]);
            }
        }
        // print to a file:
        else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
                for (int i = 0; i < finalImage.length; i++) {
                    writer.write(finalImage[i]);
                    writer.newLine();
                }
            } catch (IOException e) {
                // TODO: deal with exceptions
                e.printStackTrace();
            }
        }
    }

    private void handleRound() {
    }

    private void handleOutput(String[] commandTokens) {
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
        int maxRes = image.getWidth();
        int minRes = Math.max(1, image.getWidth()/image.getHeight());
        switch (commandTokens[1]){
            case "":
                break;
            case "up":
                if (this.resolution*2 <= maxRes) {
                    this.resolution *= 2;
                    break;
                }
                else {
                    System.out.println("Did not change resolution due to exceeding boundaries.");
                    return;
                }
            case "down":
                if (this.resolution/2 >= minRes) {
                    this.resolution /= 2;
                    break;
                }
                else {
                    System.out.println("Did not change resolution due to exceeding boundaries.");
                    return;
                }
            default:
                System.out.println("Did not change resolution due to incorrect format.");
                return;
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
//        System.out.println();
    }
    
    private void handleAdd() {
    }

    private void handleExit() {
    }


    private char[] createBasicCharsSet() {
        // todo: maybe change to array<>
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

//            handleInput(input);
            switch (commandTokens[0]) {
                case EXIT_MESSAGE:
                    handleExit();
                    return;
                case CHARS_MESSAGE:
                    handleCharsInput();
                case ADD_MESSAGE:
                    handleAdd();
                case REMOVE_MESSAGE:
                    handleRemove();
                case RES_MESSAGE:
                    handleRes(commandTokens);
                case OUTPUT_MESSAGE:
                    handleOutput();
                case ROUND_MESSAGE:
                    handleRound();
                case ASCII_ART_MESSAGE:
                    handleAsciiArt();
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
        if (args.length != 1) {
            throw new RuntimeException(WRONG_NUM_ARGS_ERROR);
        }
        String imagePath = args[0];
        shell.run(imagePath);
    }
}
