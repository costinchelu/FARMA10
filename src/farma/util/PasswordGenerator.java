package farma.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordGenerator {

    public static String getSHA512Password(String passwordToEncript, byte[] salt) {
        String generatedPassword = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);

            // convertim parola primita in byte[]
            byte[] bytes = md.digest(passwordToEncript.getBytes());

            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
            //procesam vectorul de tip byte[], iar apoi expresia rezultata o transformam in String pentru a fi returnata

        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
        return generatedPassword;
    }

    public static byte[] getSalt() throws NoSuchAlgorithmException {
        // salt = seed (cuvant aleator generat pentru fiecare parola)
        SecureRandom sr = SecureRandom.getInstanceStrong();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}
