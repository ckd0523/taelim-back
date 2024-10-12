package com.codehows.taelim.security;

import org.springframework.stereotype.Component;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;

@Component
public class PasswordHasher {

    private static final int ITERATION_COUNT = 10000;
    private static final int SALT_SIZE = 128 / 8;
    private static final int HASH_SIZE = 256 / 8;

    public byte[] hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);

        byte[] hash = pbkdf2(password.toCharArray(), salt, ITERATION_COUNT, HASH_SIZE);

        byte[] output = new byte[1 + 4 + 4 + salt.length + hash.length];
        output[0] = 0x01; // format marker
        ByteBuffer.wrap(output, 1, 4).putInt(1); // PRF (1 = HMACSHA512)
        ByteBuffer.wrap(output, 5, 4).putInt(ITERATION_COUNT);
        ByteBuffer.wrap(output, 9, 4).putInt(SALT_SIZE);
        System.arraycopy(salt, 0, output, 13, salt.length);
        System.arraycopy(hash, 0, output, 13 + salt.length, hash.length);

        return output;
    }

    public boolean verifyPassword(String password, String storedHash) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] storedHashBytes = Base64.getDecoder().decode(storedHash);

        if (storedHashBytes[0] != 0x01) {
            throw new IllegalArgumentException("Invalid hash version");
        }

        int iterations = ByteBuffer.wrap(storedHashBytes, 5, 4).getInt();
        int saltLength = ByteBuffer.wrap(storedHashBytes, 9, 4).getInt();

        byte[] salt = Arrays.copyOfRange(storedHashBytes, 13, 13 + saltLength);
        byte[] hash = Arrays.copyOfRange(storedHashBytes, 13 + saltLength, storedHashBytes.length);

        byte[] testHash = pbkdf2(password.toCharArray(), salt, iterations, hash.length);

        return Arrays.equals(hash, testHash);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        return skf.generateSecret(spec).getEncoded();
    }
}