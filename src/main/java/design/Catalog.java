package design;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.UsersEntity;
import repositories.UserRepository;

/**
 * Created by rask on 01.03.2017.
 */
public class Catalog extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Catalog.fxml"));

        primaryStage.setTitle("Catalog");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    public Catalog(Stage stage) throws Exception {

        //lblMessage.setText(UserRepository.getInstance().getName());
        start(stage);

    }

}
