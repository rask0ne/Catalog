package design;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

/**
 * Logic of 'Login' window.
 */
public class Login extends Application {

    private final Logger logger = Logger.getLogger(Login.class);

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

        logger.info("Window 'Login' created");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
