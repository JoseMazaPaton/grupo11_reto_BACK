package vacantes.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordGenerator {
	
	private static final int PASSWORD_LENGTH = 10; // Longitud de la contraseña

    public static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[PASSWORD_LENGTH];
        random.nextBytes(bytes);
        return Base64.getEncoder().withoutPadding().encodeToString(bytes).substring(0, PASSWORD_LENGTH);
    }
}
