package librarymanagement.util;

import java.security.SecureRandom;

public class ISBNGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();
    public static String generateISBN() {
        StringBuilder isbn = new StringBuilder(13);
        for (int i = 0; i < 13; i++) {
            int digit = RANDOM.nextInt(10);
            isbn.append(digit);
        }
        return isbn.toString();
    }
}
