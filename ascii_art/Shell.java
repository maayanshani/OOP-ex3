package ascii_art;

import image_char_matching.SubImgCharMatcher;

public class Shell {

    private void testSubImgCharMatcher() {
        char[] chars = {'0', '1', '2', '3', '`'};
        SubImgCharMatcher charMatcher = new SubImgCharMatcher(chars);
        int N = 10;
        for (int i = 0; i < N; i++) {
            System.out.println(charMatcher.getCharByImageBrightness(i/(double)N));
        }
    }

    public static void main(String[] args) {
        // Create an instance of Shell and call the test method
        Shell shell = new Shell();
        shell.testSubImgCharMatcher();
    }
}
