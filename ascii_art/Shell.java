package ascii_art;

import image.Image;
import image_char_matching.SubImgCharMatcher;

import java.util.SortedSet;
import java.util.TreeSet;

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
    private static final String PREFIX_MESSAGE = ">>> ";
    private static final String EXIT_MESSAGE = "exit"; // Maayan
    private static final String CHARS_MESSAGE = "chars"; // Maayan
    private static final String ADD_MESSAGE = "add"; // Maayan
    private static final String REMOVE_MESSAGE = "remove"; // Maayan
    private static final String RES_MESSAGE = "res"; // Rotem
    private static final String OUTPUT_MESSAGE = "output"; // Rotem
    private static final String ROUND_MESSAGE = "round"; // Maayan
    private static final String ASCII_ART_MESSAGE = "asciiArt"; // Rotem
    private static final String WRONG_NUM_ARGS_ERROR =
            "Wrong number of arguments. Should get 1 argument: imagePath.";
    private static final String SPACE_AND_ETC = "(\\s.*)?";

    private SortedSet<Character> sortedChars;


    public Shell(){
        sortedChars = new TreeSet<>();
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

//    private void testPadAndExtend() {
//        // ! for the test to work, need to add the image in the "Ex3" folder !
//        String imagePath = "examples/cat.jpeg";  // Replace with the actual path
//
//        try {
//            // Load the image with the Image class constructor
//            Image originalImage = new Image(imagePath);
//            System.out.println("Original image brightness: " + originalImage.getBrightnessGrade());
//
//            // Extend the image with ExtendImage
//            ExtendImage extendedImage = new ExtendImage(originalImage.getPixelArray(), originalImage.getWidth(), originalImage.getHeight());
//            // TODO: this part is not working because the inherits problem
//            System.out.println("Extended image brightness: " + extendedImage.getBrightnessGrade());
//
//            // Optionally, save the extended image to a file
//            extendedImage.saveImage("extended_image_output");
//
//        } catch (IOException e) {
//            System.err.println("Error loading image: " + e.getMessage());
//        } catch (IllegalArgumentException e) {
//            System.err.println("Invalid argument: " + e.getMessage());
//        }
//
//    }

    private void testHandleChars() {
        initializeDefaultChars();
        handleCharsInput();
    }

    private void initializeDefaultChars() {
        for (char c = ZERO; c <= NINE; c++) {
            sortedChars.add(c);
        }
    }

    public void run(String imageName){
        // todo: create image and implement this
        try {
            Image image = new Image("asdfg");
        } catch (Exception e) {}
        char[] chars = createBasicCharsSet();

        System.out.println(PREFIX_MESSAGE);
        while (true) {
            String input = KeyboardInput.readLine();
            /**
             * MAAYAN:
             * I change to this method because the inputs might be a bit different
             * see the documentation of each section
             */
            handleInput(input);
//            switch (input) {
//                case EXIT_MESSAGE:
//                    handleExit();
//                    return;
//                case CHARS_MESSAGE:
//                    handleChars();
//                case ADD_MESSAGE:
//                    handleAdd();
//                case REMOVE_MESSAGE:
//                    handleRemove();
//                case RES_MESSAGE:
//                    handleRes();
//                case OUTPUT_MESSAGE:
//                    handleOutput();
//                case ROUND_MESSAGE:
//                    handleRound();
//                case ASCII_ART_MESSAGE:
//                    handleAsciiArt();
//                }
            System.out.println(PREFIX_MESSAGE);
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

    }

    private void handleRound() {
    }

    private void handleOutput() {
    }

    private void handleRes() {
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
