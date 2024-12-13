package ascii_art;

import image.ExtendImage;
import image.Image;
import image_char_matching.SubImgCharMatcher;

import java.awt.*;
import java.io.IOException;

public class Shell {

    private void testSubImgCharMatcher() {
        char[] chars = {'0', '1', '2', '3', '`'};
        SubImgCharMatcher charMatcher = new SubImgCharMatcher(chars);
        int N = 10;
        for (int i = 0; i < N; i++) {
            System.out.println(charMatcher.getCharByImageBrightness(i/(double)N));
        }
    }

    private void testPadAndExtend() {
        // ! for the test to work, need to add the image in the "Ex3" folder !
        String imagePath = "cat.jpeg";  // Replace with the actual path

        try {
            // Load the image with the Image class constructor
            Image originalImage = new Image(imagePath);
            System.out.println("Original image brightness: " + originalImage.getBrightnessGrade());

            // Extend the image with ExtendImage
            ExtendImage extendedImage = new ExtendImage(originalImage.getPixelArray(), originalImage.getWidth(), originalImage.getHeight());
            // TODO: this part is not working because the inherits problem
            System.out.println("Extended image brightness: " + extendedImage.getBrightnessGrade());

            // Optionally, save the extended image to a file
            extendedImage.saveImage("extended_image_output");

        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid argument: " + e.getMessage());
        }

    }



    public static void main(String[] args) {
        // Create an instance of Shell and call the test method
        Shell shell = new Shell();
//        shell.testSubImgCharMatcher();
        shell.testPadAndExtend();


    }
}
