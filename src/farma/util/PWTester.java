package farma.util;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class PWTester {

    public static void main(String[] args) throws NoSuchAlgorithmException {

        String password = "simple";

        for (int i = 1; i <= 10; i++) {
            System.out.printf("Salt %d: %s%n", i, Arrays.toString(PasswordGenerator.getSalt()));

            System.out.printf("Password %d: %s%n",
                    i,
                    PasswordGenerator.getSHA512Password(password, PasswordGenerator.getSalt()));
        }
    }
}
