package sample;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class Tests {

    public static void Encrypt (String testDir, String hashDir) {

        // before
        String testString = "This is just some random test data";
        byte[] testData = testString.getBytes(StandardCharsets.UTF_8);
        String testFile = testDir + "filesafetestfile";
        FileUtils.write(testFile, testData);
        char[] password = {'p', 'i', 'z', 'z', 'a'};
        SecretKeySpec key = CryptoUtils.generateSecretKey(password);

        CryptoUtils.encrypt(testFile, key, hashDir);

        // after
        String encryptedFile = FileUtils.getAllFileNames(testDir, "aes")[0];
        byte[] encryptedData = FileUtils.readAllBytes(encryptedFile);
        String encryptedString = new String(encryptedData, StandardCharsets.UTF_8);

        // print results
        System.out.println("Encrypt test results:");
        System.out.println("Original file name: " + testFile);
        System.out.println("Encrypted file name: " + encryptedFile);
        System.out.println("Original string: " + testString);
        System.out.println("Encrypted string: " + encryptedString);

        // cleanup
        CryptoUtils.decrypt(encryptedFile, key, hashDir);
        FileUtils.delete(testFile);
    }

    public static void Decrypt (String testDir, String hashDir) {

        // before
        String testString = "This is just some random test data";
        byte[] testData = testString.getBytes(StandardCharsets.UTF_8);
        String testFile = testDir + "testfile";
        FileUtils.write(testFile, testData);
        char[] password = {'p', 'i', 'z', 'z', 'a'};
        SecretKeySpec key = CryptoUtils.generateSecretKey(password);

        CryptoUtils.encrypt(testFile, key, hashDir);

        String encryptedFile = FileUtils.getAllFileNames(testDir, "aes")[0];
        byte[] encryptedData = FileUtils.readAllBytes(encryptedFile);
        String encryptedString = new String(encryptedData, StandardCharsets.UTF_8);

        CryptoUtils.decrypt(encryptedFile, key, hashDir);

        // after
        String decryptedFile = FileUtils.getAllFileNamesWOExt(testDir, "aes")[0];
        byte[] decryptedData = FileUtils.readAllBytes(decryptedFile);
        String decryptedString = new String(decryptedData, StandardCharsets.UTF_8);

        // print results
        System.out.println("Decrypt test results:");
        System.out.println("Encrypted file name: " + encryptedFile);
        System.out.println("Decrypted file name: " + decryptedFile);
        System.out.println("Encrypted string: " + encryptedString);
        System.out.println("Decrypted string: " + decryptedString);

        // cleanup
        FileUtils.delete(testFile);
    }
}
