package com.codehows.taelim.security;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

@Component
public class PasswordHasher3 {

    private static final byte FORMAT_MARKER = 0x01; // Format marker for version 3
    private static final int PRF_HMACSHA512 = 0x02; // .NET uses 0x02 for HMACSHA512
    private static final int ITERATIONS = 100000; // 100,000 iterations
    private static final int SALT_SIZE = 16; // 128-bit salt (16 bytes)
    private static final int SUBKEY_LENGTH = 32; // 256-bit subkey (32 bytes)
    private static final int HEADER_SIZE = 13; // 1 byte for marker, 4 bytes for PRF, 4 bytes for iterations, 4 bytes for salt length

    // Method to hash the password (generates the full hash with salt)
    // This method generates a new password hash.
    // It randomly generates a salt and uses PBKDF2 with HMAC-SHA512 to derive a subkey.
    // Then, it encodes the result in Base64, which includes the format marker, PRF, iteration count, salt size, salt, and subkey.
    public static String hashPasswordV3(String password) {
        byte[] salt = generateSalt();
        byte[] subkey = hashPasswordWithSalt(password.toCharArray(), salt, ITERATIONS, SUBKEY_LENGTH);

        // Prepare the output buffer (1 byte marker + 4 bytes PRF + 4 bytes iterations + 4 bytes salt size + salt + subkey)
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE + salt.length + subkey.length);
        buffer.put(FORMAT_MARKER); // Format marker (0x01 for version 3)
        buffer.putInt(PRF_HMACSHA512); // PRF (HMACSHA512 = 0x02)
        buffer.putInt(ITERATIONS); // Iterations (100,000)
        buffer.putInt(SALT_SIZE); // Salt size (16 bytes)

        // Copy salt and subkey into buffer
        buffer.put(salt); // Salt
        buffer.put(subkey); // Subkey

        System.out.println("salt=" + byteArrayToHexString(salt));
        System.out.println("subkey=" + byteArrayToHexString(subkey));

        // Return the hashed password in Base64 encoding
        return Base64.getEncoder().encodeToString(buffer.array());
    }

    // Method to verify a password against the stored hashed password
    // This method verifies if a given password matches the hashed password.
    // It extracts the PRF, iteration count, salt size, salt, and subkey from the stored hash,
    // derives a subkey from the provided password,
    // and compares it with the stored subkey using a constant-time comparison.
    public static boolean verifyHashedPasswordV3(String hashedPasswordBase64, String password) {
        byte[] hashedPassword = Base64.getDecoder().decode(hashedPasswordBase64);

        // Check if the format marker is correct (should be 0x01 for version 3)
        if (hashedPassword[0] != FORMAT_MARKER) {
            System.out.println("Invalid format marker");
            return false;
        }

        // Read the PRF, iteration count, and salt length from the hash
        int prf = readNetworkByteOrder(hashedPassword, 1);
        int iterCount = readNetworkByteOrder(hashedPassword, 5);
        int saltLength = readNetworkByteOrder(hashedPassword, 9);

        // Ensure the salt length is valid
        if (saltLength < SALT_SIZE) {
            System.out.println("Invalid salt size");
            return false;
        }

        // Extract the salt from the hash
        byte[] salt = Arrays.copyOfRange(hashedPassword, HEADER_SIZE, HEADER_SIZE + saltLength);

        System.out.println("salt=" + byteArrayToHexString(salt));

        // Extract the subkey (derived key) from the hash
        int subkeyLength = hashedPassword.length - HEADER_SIZE - saltLength;
        if (subkeyLength < SUBKEY_LENGTH) {
            System.out.println("Invalid subkey size");
            return false;
        }
        byte[] expectedSubkey = Arrays.copyOfRange(hashedPassword, HEADER_SIZE + saltLength, hashedPassword.length);

        // Derive the subkey using PBKDF2WithHmacSHA512 with the same salt, iterations, and PRF
        byte[] actualSubkey = hashPasswordWithSalt(password.toCharArray(), salt, iterCount, subkeyLength);


        System.out.println("expectedSubkey=" + byteArrayToHexString(expectedSubkey));
        System.out.println("actualSubkey=" + byteArrayToHexString(actualSubkey));

        // Compare the derived subkey with the expected subkey
        return slowEquals(expectedSubkey, actualSubkey);
    }

    // Hash the password with the provided salt using PBKDF2WithHmacSHA512
    private static byte[] hashPasswordWithSalt(char[] password, byte[] salt, int iterations, int keyLength) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength * 8);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }

    // Generate a random salt of 128 bits (16 bytes)
    private static byte[] generateSalt() {
        SecureRandom rng = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        rng.nextBytes(salt);
        return salt;
    }

    // Read an integer (UInt32) from the byte array in network byte order (big-endian)
    private static int readNetworkByteOrder(byte[] buffer, int offset) {
        return ((buffer[offset] & 0xff) << 24) |
                ((buffer[offset + 1] & 0xff) << 16) |
                ((buffer[offset + 2] & 0xff) << 8) |
                (buffer[offset + 3] & 0xff);
    }

    // A constant-time comparison method to prevent timing attacks
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    private static String byteArrayToHexString(byte[] byteArray)
    {
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteArray) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }


    //
    // How to Test
    // Run the main method. It will generate a hash for the password and then immediately verify that the password matches the generated hash.
    //

    /*
    public static void main(String[] args) {
        // Example password
        String password = "abc.defZ123!";

        // Hash the password
        String hashedPassword = hashPasswordV3(password);
        System.out.println("Hashed Password (V3) in Base64: " + hashedPassword);

        // Verify the hashed password
        boolean isPasswordValid = verifyHashedPasswordV3(hashedPassword, password); // should be true
        System.out.println("Password verification result: " + isPasswordValid);

        hashedPassword = "AQAAAAIAAYagAAAAELsSVXAUdYQCmTr88HV5rJ+9kZeFNeM9ZsWTLNBSzbsewv69oZjkBtj+Clq7PwrVmg==";
        isPasswordValid = verifyHashedPasswordV3(hashedPassword, password); // should be true
        System.out.println("Password verification result: " + isPasswordValid);

        isPasswordValid = verifyHashedPasswordV3(hashedPassword, "wrong password!"); // should be false
        System.out.println("Password verification result: " + isPasswordValid);


    }
    */
}

/* Expected output

salt=83438C8870EA14005569484F12A983BB
subkey=6BDAEE7E510CE29DAF81D454E58745E6422329D6FB0B8F2523C531AF54675E57

Hashed Password (V3) in Base64: AQAAAAIAAYagAAAAEINDjIhw6hQAVWlITxKpg7tr2u5+UQzina+B1FTlh0XmQiMp1vsLjyUjxTGvVGdeVw==

salt=83438C8870EA14005569484F12A983BB
expectedSubkey=6BDAEE7E510CE29DAF81D454E58745E6422329D6FB0B8F2523C531AF54675E57
actualSubkey=6BDAEE7E510CE29DAF81D454E58745E6422329D6FB0B8F2523C531AF54675E57
Password verification result: true

salt=BB12557014758402993AFCF07579AC9F
expectedSubkey=BD91978535E33D66C5932CD052CDBB1EC2FEBDA198E406D8FE0A5ABB3F0AD59A
actualSubkey=BD91978535E33D66C5932CD052CDBB1EC2FEBDA198E406D8FE0A5ABB3F0AD59A
Password verification result: true

salt=BB12557014758402993AFCF07579AC9F
expectedSubkey=BD91978535E33D66C5932CD052CDBB1EC2FEBDA198E406D8FE0A5ABB3F0AD59A
actualSubkey=97D8E699D47118459805B87475B229CD1C8F998F11493A215F062E81DB188F47
Password verification result: false


*/
