package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// remember to add the bouncy castle library file
import static java.security.Security.addProvider;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        // add bouncy castle as security provider
        addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        char[] pw = InputUtils.requireStrongPassword();
        char action = 's';

        while(action != 'f') {
            action = InputUtils.prompt("Select action e or d:")[0];
            // decrypt all
            if(action == 'd')
            for (String s : FileUtils.getAllFileNames("/home/simon/securitydemo/", "aes"))
                CryptoUtils.decrypt(s, pw);

            // encrypt all
            if(action == 'e')
            for (String s : FileUtils.getAllFileNamesWOExt("/home/simon/securitydemo/", "aes"))
                CryptoUtils.encrypt(s, pw);
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
