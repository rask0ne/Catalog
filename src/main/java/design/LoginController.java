package design;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import crypt.md5Crypt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.UsersEntity;
import org.apache.log4j.Logger;
import repositories.UserRepository;

import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * Controller of 'Login' window.
 */
public class LoginController {


    @FXML
    private Label lblMessage;
    /**
     * TextField to insert username string.
     */
    @FXML
    private TextField txtUsername;
    @FXML
    /**
     * TextField to insert password string
     */
    private PasswordField txtPassword;

    private final Logger logger = Logger.getLogger(LoginController.class);

    /**
     * loginButton action method. Gets data from username and password TextFields.
     * There is a check in method for concurrences with users database. If everything
     * is OK, creating singleton object of user with all main info about user.
     * Then creating a 'Catalog' window.
     * @param actionEvent
     * @throws Exception
     */
    public void loginButtonAction(ActionEvent actionEvent) throws Exception {

        logger.info("Login button pressed");

        boolean check = true;
        String query;
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        password = new md5Crypt().md5Apache(password);

        Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/catalogdb?useSSL=false", "root", "root");
        Statement stmt = (Statement) con.createStatement();
        logger.info("Connection to DB established");
        query = "SELECT Id, Username, Password, Role FROM users;";
        stmt.executeQuery(query);
        ResultSet rs = stmt.getResultSet();
        while (rs.next()) {

            String dbUsername = rs.getString("username");
            String dbPassword = rs.getString("password");

            if (username.equals(dbUsername) && password.equals(dbPassword)) {

                logger.info("User entered successfully");
                int id = rs.getInt(("id"));

                UserRepository user = new UserRepository().getInstance();

                user.setId(id);
                user.setUsername(username);
                int role = rs.getInt(("role"));
                user.setRole(role);

                logger.info("User singleton created");

                con.close();
                logger.info("Connection to DB closed");

                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

                Parent parent = FXMLLoader.load(getClass().getResource("Catalog.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(parent);
                stage.setScene(scene);
                stage.setTitle("Catalog");
                stage.show();

                logger.info("Window 'Catalog' created");

                break;
            }
        }
        lblMessage.setStyle("-fx-color: red");
        lblMessage.setText("Wrong username or password!");

    }

    /**
     * Entering catalog without authorization. Creating a singleton object of user class
     * to keep some info. Creating window 'Catalog'.
     * @param actionEvent
     * @throws Exception
     */
    public void guestAction(ActionEvent actionEvent) throws Exception{

        UserRepository user = new UserRepository().getInstance();

        user.setId(2);
        user.setUsername("guest");
        user.setRole(3);

        logger.info("Guest entering DB, created singleton 'guest'");

        Catalog catalog = new Catalog(new Stage());
        logger.info("Window 'Catalog' created");
    }

    /**
     * Button, which is responsible for creating registration form.
     * @param actionEvent
     * @throws Exception
     */
    public void registerButtonAction(ActionEvent actionEvent) throws Exception {

        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        Parent parent = FXMLLoader.load(getClass().getResource("Register.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Registration Form");
        stage.show();

        logger.info("Register button pressed, created window 'Register'");

    }
};
