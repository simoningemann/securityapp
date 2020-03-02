package sample;

import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class CryptoUtils {

    public static void encrypt(String filePath, char[] password){
        try {
            // setup crypto preferences
            SecureRandom secureRandom = SecureRandom.getInstance("DEFAULT", "BC");
            byte[] generatedIV = new byte[16];
            secureRandom.nextBytes(generatedIV);
            byte[] salt = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
            SecretKeyFactory factory =   SecretKeyFactory.getInstance("PBKDF2WITHHMACSHA256", "BC");
            SecretKey k = factory.generateSecret(new PBEKeySpec(password, salt, 1000, 128));
            byte[] keyBytes = k.getEncoded();
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            // read the file
            byte[] file = FileUtils.readAllBytes(filePath);

            // encrypt the file
            byte[] output = cipher.doFinal(file);

            // write new encrypted file
            String ivString = Hex.toHexString(cipher.getIV());
            String outFile = filePath + "." + ivString + "." + "aes";
            FileUtils.write(outFile, output);

            // delete the original file
            FileUtils.delete(filePath);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void decrypt(String filePath, char[] password) {
        try {
            // split up filePath: 0=originalpath, 1=originalextension, 2=iv, 3=aes
            String[] parts = filePath.split("[.]");
            // setup crypto preferences
            byte[] salt = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
            SecretKeyFactory factory =   SecretKeyFactory.getInstance("PBKDF2WITHHMACSHA256", "BC");
            SecretKey k = factory.generateSecret(new PBEKeySpec(password, salt, 1000, 128));
            byte[] keyBytes = k.getEncoded();
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            byte[] iv = Hex.decode(parts[2]);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

            // read encrypted file
            byte[] input = FileUtils.readAllBytes(filePath);

            // decrypt file
            byte[] output = cipher.doFinal(input);

            // write to new file
            String outFile = parts[0] + "." + parts[1];
            FileUtils.write(outFile, output);

            // delete the encrypted file
            FileUtils.delete(filePath);

        } catch (Exception e)  {
            System.out.println(e.toString());
        }
    }
}
