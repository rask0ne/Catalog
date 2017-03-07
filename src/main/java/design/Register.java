package design;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

/**
 * Created by rask on 01.03.2017.
 */

/**
 * Logic of 'Register' window.
 */
public class Register extends Application {

    private final Logger logger = Logger.getLogger(Register.class);

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));

        primaryStage.setTitle("Register");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

        logger.info("Window 'Register' created");
    }
}
