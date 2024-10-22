package com.codehows.taelim.security;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

@Component
public class PasswordHasher2 {

    private static final int PBKDF2_WITH_HMAC_SHA256 = 1; // PBKDF2 알고리즘 식별자

    public String hashPasswordV3(String password) throws Exception {
        //SecureRandom rng = new SecureRandom();
        String prf = "PBKDF2WithHmacSHA256";
        int iterCount = 10000;
        int saltSize = 16;
        int numBytesRequested = 32;

        // salt 생성
        byte[] salt = new byte[saltSize];
        //System.out.println("랜덤한 값 : " + rng);
        //rng.nextBytes(salt);

        // 비밀번호 해싱
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterCount, numBytesRequested * 8);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(prf);
        byte[] subkey = factory.generateSecret(spec).getEncoded();

        // 해시 결과 저장을 위한 배열
        byte[] outputBytes = new byte[13 + salt.length + subkey.length];
        outputBytes[0] = 0x01; // 포맷 마커

        // 출력 바이트에 PRF, 반복 횟수, salt 크기 기록
        writeNetworkByteOrder(outputBytes, 1, PBKDF2_WITH_HMAC_SHA256); // PBKDF2 알고리즘 식별자
        writeNetworkByteOrder(outputBytes, 5, iterCount);
        writeNetworkByteOrder(outputBytes, 9, saltSize);

        // salt와 subkey를 outputBytes에 복사
        System.arraycopy(salt, 0, outputBytes, 13, salt.length);
        System.arraycopy(subkey, 0, outputBytes, 13 + salt.length, subkey.length);

        // Base64로 인코딩하여 출력
        return Base64.getEncoder().encodeToString(outputBytes);
    }

    private void writeNetworkByteOrder(byte[] buffer, int offset, int value) {
        buffer[offset] = (byte) ((value >> 24) & 0xFF);
        buffer[offset + 1] = (byte) ((value >> 16) & 0xFF);
        buffer[offset + 2] = (byte) ((value >> 8) & 0xFF);
        buffer[offset + 3] = (byte) (value & 0xFF);
    }



    public boolean verifyHashedPasswordV3(byte[] hashedPassword, String password, int[] iterCount, String prf) {
        // iterCount는 배열을 통해 값이 변경될 수 있도록 처리
        iterCount[0] = 0;
        try {
            // 헤더 정보 읽기
            int prfValue = readNetworkByteOrder(hashedPassword, 1);
            iterCount[0] = readNetworkByteOrder(hashedPassword, 5);
            int saltLength = readNetworkByteOrder(hashedPassword, 9);

            // salt 읽기: 최소 128비트 이상이어야 함
            if (saltLength < 128 / 8) {
                return false;
            }
            byte[] salt = new byte[saltLength];
            System.arraycopy(hashedPassword, 13, salt, 0, salt.length);

            // 나머지 페이로드에서 subkey 읽기: 최소 128비트 이상이어야 함
            int subkeyLength = hashedPassword.length - 13 - salt.length;
            if (subkeyLength < 128 / 8) {
                return false;
            }
            byte[] expectedSubkey = new byte[subkeyLength];
            System.arraycopy(hashedPassword, 13 + salt.length, expectedSubkey, 0, expectedSubkey.length);

            // 받은 비밀번호 해싱 후 비교
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterCount[0], subkeyLength * 8);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(prf);
            byte[] actualSubkey = factory.generateSecret(spec).getEncoded();

            // 두 배열 비교
            return Arrays.equals(actualSubkey, expectedSubkey);

        } catch (Exception e) {
            // 페이로드가 잘못된 경우 검증 실패
            return false;
        }
    }

    private static int readNetworkByteOrder(byte[] buffer, int offset) {
        return (buffer[offset] << 24) | ((buffer[offset + 1] & 0xFF) << 16) | ((buffer[offset + 2] & 0xFF) << 8) | (buffer[offset + 3] & 0xFF);
    }
}

