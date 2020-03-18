package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bouncycastle.util.encoders.Hex;

// remember to add the bouncy castle library file
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static java.security.Security.addProvider;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        // add bouncy castle as security provider
        addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // setup variables and make filesafe directory if none exists
        String dir = System.getProperty("user.home") + "/filesafe/";
        String hashDir = dir + "/.hashes/";
        FileUtils.makeDir(dir);
        FileUtils.makeDir(hashDir);
        char action = 's';

        // instruct user to put files in filesafe directory and prompt for strong password
        System.out.println("Place the files you want to encrypt inside " + dir);
        char[] pw = InputUtils.requireStrongPassword();
        //String hash = Hex.toHexString(CryptoUtils.getHash(pw.toString().getBytes(StandardCharsets.UTF_8)));

        // generate secret key with PBKDF
        SecretKeySpec secretKey = CryptoUtils.generateSecretKey(pw);

        // listen for user action
        while(action != 'f') {
            action = InputUtils.prompt("Select action e or d:")[0];
            // decrypt all
            if(action == 'd')
            for (String s : FileUtils.getAllFileNames(dir, "aes"))
                CryptoUtils.decrypt(s, secretKey, hashDir);

            // encrypt all
            if(action == 'e')
            for (String s : FileUtils.getAllFileNamesWOExt(dir, "aes"))
                CryptoUtils.encrypt(s, secretKey, hashDir);
        }

        // standard dummy code
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
