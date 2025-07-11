package util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.mindrot.jbcrypt.BCrypt;


public class PasswordHashing {

    private static final Logger LOGGER = Logger.getLogger(PasswordHashing.class.getName());
    private static final int BCRYPT_SALT_ROUNDS = 12; // Un valore comune, puoi aumentarlo per maggiore sicurezza a scapito delle prestazioni

    /**
     * Hashes una password usando l'algoritmo Bcrypt.
     * Questo metodo genera automaticamente un salt casuale e lo incorpora nell'hash.
     * @param password La password in chiaro da hashare.
     * @return La password hashata e salata (Stringa Bcrypt), o null in caso di errore.
     */
    public static String hashPassword(String password) {
        try {
            // Genera un salt casuale e hashes la password con il salt.
            // Il numero di rounds (es. 12) determina la complessità computazionale.
            // Valori più alti = più lento = più sicuro.
            return BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_SALT_ROUNDS));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'hashing della password con Bcrypt.", e);
            return null;
        }
    }

    /**
     * Verifica se una password in chiaro corrisponde a una password hashata precedentemente.
     * Bcrypt estrae il salt dall'hashedPassword e lo usa per hashare la password fornita,
     * poi confronta i due hash.
     * @param password La password in chiaro da verificare.
     * @param hashedPassword La password hashata (dal database) con cui confrontare.
     * @return true se le password corrispondono, false altrimenti.
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            LOGGER.warning("Tentativo di verificare password con valori null. Password fornita: " + (password != null) + ", Password hashata: " + (hashedPassword != null));
            return false;
        }
        try {
            return BCrypt.checkpw(password, hashedPassword);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore durante la verifica della password con Bcrypt.", e);
            return false;
        }
    }

    public static void main(String[] args) {
        String plainPassword = "mysecretpassword123";
        String hashedPassword = hashPassword(plainPassword);
        System.out.println("Password in chiaro: " + plainPassword);
        System.out.println("Password hashata (Bcrypt): " + hashedPassword);

        System.out.println("\nVerifica password corretta: " + verifyPassword(plainPassword, hashedPassword)); // Dovrebbe essere true
        System.out.println("Verifica password errata: " + verifyPassword("wrongpassword", hashedPassword)); // Dovrebbe essere false
        System.out.println("Verifica password con hash non valido: " + verifyPassword(plainPassword, "invalidhashstring")); // Dovrebbe essere false
    }
}