package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

// remember to add the bouncy castle library file
import javax.crypto.spec.SecretKeySpec;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.security.Security.addProvider;

public class Main extends Application {

    SecretKeySpec secretKey = null;
    String userHome = System.getProperty("user.home");
    String dir = userHome + "/.filesafe/";
    String hashDir = dir + "/.hashes/";

    @Override
    public void start(Stage primaryStage) throws Exception{

        // add bouncy castle as security provider
        addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // setup variables and make filesafe directory if none exists
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
        Label encryptLabel = new Label("Choose which files to encrypt.");
        Button encryptButton = new Button("Encrypt");
        Label encryptAllLabel = new Label("Choose which folder to encrypt.");
        Button encryptAllButton = new Button("Encrypt All");
        Label decryptLabel = new Label("Choose which folder to decrypt.");
        Button decryptButton = new Button("Decrypt All");
        Label hiddenLabel = new Label("Note that encrypted files are hidden, which is why they may \n seem to disappear. They are still stored in the same folder.");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(userHome));
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(userHome));

        pwButton.setOnAction(action -> {
            char[] pw = passwordField.getText().toCharArray();
            if(InputUtils.isPasswordStrong(pw, 10)) {
                isOkLabel.setText("Ok");
                secretKey = CryptoUtils.generateSecretKey(pw);
                gridPane.add(encryptLabel, 0, 5, 1, 1);
                gridPane.add(encryptButton, 0, 6, 1, 1);
                gridPane.add(encryptAllLabel, 0, 7, 1, 1);
                gridPane.add(encryptAllButton, 0, 8, 1, 1);
                gridPane.add(decryptLabel, 0, 9, 1, 1);
                gridPane.add(decryptButton, 0, 10, 1, 1);
                gridPane.add(hiddenLabel, 0, 11, 1, 1);
            }
            else {
                if (isOkLabel.getText().equals("Not ok"))
                    isOkLabel.setText("Still not ok");
                else
                    isOkLabel.setText("Not ok");

                gridPane.getChildren().remove(encryptLabel);
                gridPane.getChildren().remove(encryptButton);
                gridPane.getChildren().remove(encryptAllLabel);
                gridPane.getChildren().remove(encryptAllButton);
                gridPane.getChildren().remove(decryptLabel);
                gridPane.getChildren().remove(decryptButton);
                gridPane.getChildren().remove(hiddenLabel);
            }
        });

        encryptButton.setOnAction(action -> {
            List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);
            for (File file : files)
                CryptoUtils.encrypt(file.getAbsolutePath(), secretKey, hashDir);
        });

        encryptAllButton.setOnAction(action -> {
            for (String file : FileUtils.getAllFileNamesWOExtRecursive(directoryChooser.showDialog(primaryStage).getAbsolutePath() + "/", "aes"))
                CryptoUtils.encrypt(file, secretKey, hashDir);
        });

        decryptButton.setOnAction(action -> {
            for (String file : FileUtils.getAllFileNamesRecursive(directoryChooser.showDialog(primaryStage).getAbsolutePath() + "/", "aes"))
                CryptoUtils.decrypt(file, secretKey, hashDir);
        });

        gridPane.add(pwLabel, 0, 0, 1, 1);
        gridPane.add(passwordField, 0, 1, 1,1);
        gridPane.add(pwButton, 0, 2, 1, 1);
        gridPane.add(isOkLabel, 0, 3, 1, 1);
        gridPane.add(checklabel, 0, 4, 1, 1);


        primaryStage.setScene(new Scene(gridPane, 600, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
