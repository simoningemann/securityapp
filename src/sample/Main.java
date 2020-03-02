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
        addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());//

        for (String s : FileUtils.getAllFileNames("/home/simon/","js"))
            System.out.println(s);

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
