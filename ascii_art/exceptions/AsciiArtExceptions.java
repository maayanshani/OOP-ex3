package ascii_art.exceptions;

// TODO: add documentation


/** Exceptions:
 * - AsciiAlgo:
 *      V wrong num of args[] in main - WRONG_NUM_ARGS_ERROR, exit the program
 *      V cant open Image - FAILS_TO_OPEN_IMAGE_MESSAGE, exit the program
 *      V wrong input (commands):
 *          V general (command not in the 8) - INVALID_COMMAND_MESSAGE
 *          V output (incorrect format) - OUTPUT_INCORRECT_FORMAT_MESSAGE
 *          V res (out of range, incorrect format) - INVALID_RES_MESSAGE, RES_INCORRECT_FORMAT_MESSAGE
 *          V add and remove (incorrect format) - ADD_INVALID_COMMAND_MESSAGE, REMOVE_INVALID_COMMAND_MESSAGE
 *          V round (incorrect format) - ROUND_INCORRECT_FORMAT_MESSAGE
 *      V output (opening file) - OUTPUT_FAILED_MESSAGE, exit the program
 * - ImageProcessor (extends AsciiAlgo):
 *      V BrightnessMatrix - if got null Image[][] (can be the same as ImageProcessor)
 *      V ImageProcessor.extendImage: if got null Image
 *      V ImageProcessor.calculateSidePixels: if the dimensions are odd
 *      V ImageProcessor.nextPowerOfTwo: if got negative dimensions
 *      V ImageProcessor.divideImage: is not valid res
 *      V ImageProcessor.imageBrightness: width or height are 0
 * - CharMather (extends AsciiAlgo)::
 *      V subImgCharMatcher.getCharByImageBrightness: if brightness <0 or brightness > 1
 */

public class AsciiArtExceptions extends RuntimeException{
    public AsciiArtExceptions(String exceptionMessage){
        super(exceptionMessage);
    }


}
