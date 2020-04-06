package sample;

import org.bouncycastle.util.encoders.Hex;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.Arrays;

public class CryptoUtils {

    // encrypts a file given a filepath+name and a password, then deletes the plaintext
    public static boolean encrypt(String filePath, SecretKeySpec secretKey, String hashDir){
        try {
            // setup crypto preferences
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // read the file
            byte[] file = FileUtils.readAllBytes(filePath);

            // encrypt the file
            byte[] output = cipher.doFinal(file);

            // write new encrypted file
            String ivString = Hex.toHexString(cipher.getIV());

            // hash the IV with the key
            byte[] hash = getHash(concatenate(secretKey.getEncoded(), ivString.getBytes()));
            FileUtils.write(hashDir + Hex.toHexString(hash), hash);

            String[] parts = filePath.split("/");
            String newPath = "";
            String name = Hex.toHexString(cipher.doFinal(parts[parts.length-1].getBytes(StandardCharsets.UTF_8)));

            for (int i = 0; i < parts.length-1; i++)
                newPath += parts[i] + "/";

            newPath += "." + name;

            String outFile = newPath + "." + ivString + ".aes";

            if (FileUtils.isWriteSuccessful(outFile, output)) {
                FileUtils.delete(filePath);
                return true;
            }

            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // decrypts a file given a filepath+name and a secretKey, then deletes the encrypted file
    public static boolean decrypt(String filePath, SecretKeySpec secretKey, String hashDir) {
        try {
            // split up filePath: 0=originalpath, 1=originalname+ext, 2=iv, 3=aes
            String[] parts = filePath.split("[.]");
            // setup crypto preferences
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            byte[] iv = Hex.decode(parts[2]);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

            // read encrypted file
            byte[] file = FileUtils.readAllBytes(filePath);

            // check if key + iv hash exists
            String hash = Hex.toHexString(getHash(concatenate(secretKey.getEncoded(), parts[2].getBytes())));
            if(!FileUtils.dirContainsFileName(hashDir, hash)) {
                System.out.println("password incorrect for file:" + filePath);
                return false;
            }

            // decrypt file
            byte[] output = cipher.doFinal(file);

            String name = new String(cipher.doFinal(Hex.decode(parts[1])), StandardCharsets.UTF_8);

            // write to new file
            String outFile = parts[0] + name;
            FileUtils.write(outFile, output);

            // delete the encrypted file
            FileUtils.delete(filePath);
            FileUtils.delete(hashDir + hash);

            return true;

        } catch (Exception e)  {
            e.printStackTrace();
            return false;
        }
    }

    // encrypts a secret AES key with a public RSA key
    public static byte[] wrap(SecretKey secretKey, PublicKey publicKey) {
        byte[] b = {};
        try {
            Cipher cipher =
                    Cipher.getInstance("RSA/NONE/OAEPwithSHA256andMGF1Padding", "BC");
            cipher.init(Cipher.WRAP_MODE, publicKey);
            b = cipher.wrap(secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    // decrypts a secret AES key which has been encrypted with a public RSA key
    public static SecretKey unwrap(byte[] b, PrivateKey privateKey) {
        SecretKey secretKey = null;
        try {
            Cipher cipher =
                    Cipher.getInstance("RSA/NONE/OAEPwithSHA256andMGF1Padding", "BC");
            cipher.init(Cipher.UNWRAP_MODE, privateKey);
            secretKey = (SecretKey) cipher.unwrap(b, "AES", Cipher.SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secretKey;
    }

    // generate an RSA key pair
    public static KeyPair generateKeyPair () {
        KeyPair keyPair = new KeyPair(new PublicKey() {
            @Override
            public String getAlgorithm() {
                return null;
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public byte[] getEncoded() {
                return new byte[0];
            }
        }, new PrivateKey() {
            @Override
            public String getAlgorithm() {
                return null;
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public byte[] getEncoded() {
                return new byte[0];
            }
        });
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
            generator.initialize(2048);
            keyPair = generator.generateKeyPair();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return keyPair;
    }

    // generates an AES key based on password
    public static SecretKeySpec generateSecretKey (char[] password) {
        SecretKeySpec secretKey = new SecretKeySpec(new byte[] {1, 2}, "AES");
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("DEFAULT", "BC");
            byte[] generatedIV = new byte[16];
            secureRandom.nextBytes(generatedIV);
            byte[] salt = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
            SecretKeyFactory factory =   SecretKeyFactory.getInstance("PBKDF2WITHHMACSHA256", "BC");
            SecretKey k = factory.generateSecret(new PBEKeySpec(password, salt, 1000, 128));
            byte[] keyBytes = k.getEncoded();
            secretKey = new SecretKeySpec(keyBytes, "AES");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return secretKey;
    }

    // sign some data with a an RSA private key
    public static byte[] createSignature (byte[] data, PrivateKey privateKey) {
        byte[] signature = {};
        try {
            Signature signer = Signature.getInstance("SHA256withRSA", "BC");
            signer.initSign(privateKey);
            signer.update(data);
            signature = signer.sign();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return signature;
    }

    // verifies that the data was signed by the owner of the RSA private key
    public static boolean verifySignature (byte[] data, byte[] signature, PublicKey publicKey) {
        try {
            Signature verifier = Signature.getInstance("SHA256withRSA", "BC");
            verifier.initVerify(publicKey);
            verifier.update(data);
            return verifier.verify(signature);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // get the SHA256 hash value of the input data
    public static byte[] getHash (byte[] data) {
        byte[] hash = {};
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA256", "BC");
            digest.update(data);
            hash = digest.digest();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

    public static boolean verifyHash(byte[] data, byte[] hashToVerify) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA256", "BC");
            digest.update(data);
            byte[] hash = digest.digest();

            return Arrays.equals(hash, hashToVerify);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // helper functions
    public static  byte[] concatenate(byte[] a, byte[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        byte[] c = (byte[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }
}
