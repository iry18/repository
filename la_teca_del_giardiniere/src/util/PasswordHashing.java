package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordHashing {

    private static final String HASHING_ALGORITHM = "SHA-256";

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASHING_ALGORITHM);
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            // Gestisci l'eccezione in modo appropriato (es. log)
            System.err.println("Errore nell'algoritmo di hashing: " + e.getMessage());
            return null;
        }
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        String newHash = hashPassword(password);
        return newHash != null && newHash.equals(hashedPassword);
    }

    public static void main(String[] args) {
        String plainPassword = "mysecretpassword";
        String hashedPassword = hashPassword(plainPassword);
        System.out.println("Password in chiaro: " + plainPassword);
        System.out.println("Password hashata: " + hashedPassword);

        String correctPassword = "mysecretpassword";
        String wrongPassword = "wrongpassword";

        System.out.println("Verifica password corretta: " + verifyPassword(correctPassword, hashedPassword));
        System.out.println("Verifica password errata: " + verifyPassword(wrongPassword, hashedPassword));
    }

    public static boolean checkPassword(String passwordFornita, String hashedPasswordDalDatabase) {
        return PasswordHashing.verifyPassword(passwordFornita, hashedPasswordDalDatabase);
    }
}