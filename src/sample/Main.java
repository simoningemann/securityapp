package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

// remember to add the bouncy castle library file
import javax.crypto.spec.SecretKeySpec;

import java.util.concurrent.atomic.AtomicReference;

import static java.security.Security.addProvider;

public class Main extends Application {

    SecretKeySpec secretKey = null;

    @Override
    public void start(Stage primaryStage) throws Exception{

        // add bouncy castle as security provider
        addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // setup variables and make filesafe directory if none exists
        String dir = System.getProperty("user.home") + "/filesafe/";
        String hashDir = dir + "/.hashes/";
        FileUtils.makeDir(dir);
        FileUtils.makeDir(hashDir);

        /* listen for user action
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
        }*/

        // standard dummy code
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("FileSafe");

        PasswordField passwordField = new PasswordField();
        Button pwButton = new Button("Check/Set Password");
        Label pwLabel = new Label("Password");
        Label isOkLabel = new Label("Not ok");
        Label checklabel = new Label("Password must be at least 10 symbols and must have numbers,\n lower case letters, uppercase letters and special characters.");
        GridPane gridPane = new GridPane();
        Button encryptButton = new Button("Encrypt");
        Button decryptButton = new Button("Decrypt");

        pwButton.setOnAction(action -> {
            char[] pw = passwordField.getText().toCharArray();
            if(InputUtils.isPasswordStrong(pw, 10)) {
                isOkLabel.setText("Ok");
                secretKey = CryptoUtils.generateSecretKey(pw);
                gridPane.add(encryptButton, 0, 5, 1, 1);
                gridPane.add(decryptButton, 0, 6, 1, 1);
            }
            else {
                if (isOkLabel.getText().equals("Not ok"))
                    isOkLabel.setText("Still not ok");
                else
                    isOkLabel.setText("Not ok");

                gridPane.getChildren().remove(encryptButton);
                gridPane.getChildren().remove(decryptButton);
            }
        });

        gridPane.add(pwLabel, 0, 0, 1, 1);
        gridPane.add(passwordField, 0, 1, 1,1);
        gridPane.add(pwButton, 0, 2, 1, 1);
        gridPane.add(isOkLabel, 0, 3, 1, 1);
        gridPane.add(checklabel, 0, 4, 1, 1);


        primaryStage.setScene(new Scene(gridPane, 600, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
