package ascii_art;

import image.ExtendImage;
import image.Image;
import image_char_matching.SubImgCharMatcher;

import java.awt.*;
import java.io.IOException;

import static java.text.NumberFormat.Field.PREFIX;

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
    private static final String EXIT_MESSAGE = "exit";


    public Shell(){}

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

    public void run(String imageName){
        // todo: create image and implement this
        try {
            Image image = new Image("asdfg");
        } catch (Exception e) {

        }
        char[] chars = createBasicCharsSet();

        System.out.println(PREFIX_MESSAGE);
        while (true) {

        String input = KeyboardInput.readLine();
        switch (input) {
            case EXIT_MESSAGE:
                return;
            }
        }
    }

    private char[] createBasicCharsSet() {
        return new char[] {ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE};
    }


    public static void main(String[] args) {
        // Create an instance of Shell and call the test method
        Shell shell = new Shell();
//        shell.testSubImgCharMatcher();
//        shell.testPadAndExtend();
        if (args.length != 1) {
            return;
        }
        String imagePath = args[0];
        shell.run(imagePath);


    }
}
