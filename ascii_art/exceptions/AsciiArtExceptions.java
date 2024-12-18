package ascii_art.exceptions;

/** Exceptions:
 * - AsciiAlgo:
 *      V wrong num of args[] in main - WRONG_NUM_ARGS_ERROR, exit the program
 *      - cant open Image - FAILS_TO_OPEN_IMAGE_MESSAGE, exit the program - TODO
 *      V wrong input (commands):
 *          V general (command not in the 8) - INVALID_COMMAND_MESSAGE
 *          V output (incorrect format) - OUTPUT_INCORRECT_FORMAT_MESSAGE
 *          V res (out of range, incorrect format) - INVALID_RES_MESSAGE, RES_INCORRECT_FORMAT_MESSAGE
 *          V add and remove (incorrect format) - ADD_INVALID_COMMAND_MESSAGE, REMOVE_INVALID_COMMAND_MESSAGE
 *          - round
*       V output (opening file) - OUTPUT_FAILED_MESSAGE, exit the program
 * - ? BrightnessMatrix (extends AsciiAlgo):
 *      - if got null Image[][] (can be the same as ImageProcessor)
 * - ? ImageProcessor (extends AsciiAlgo):
 *      - ImageProcessor.extendImage: if got null Image
 *      - ImageProcessor.calculateSidePixels: if the dimensions are odd
 *      - ImageProcessor.nextPowerOfTwo: if got negative dimensions
 *      - ImageProcessor.divideImage: is not valid res
 *      - ImageProcessor.imageBrightness: width or height are 0
 *
 */

public class AsciiArtExceptions extends RuntimeException{
    public AsciiArtExceptions(String exceptionMessage){
        super(exceptionMessage);
    }


}
