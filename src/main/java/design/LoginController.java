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
import repositories.UserRepository;

import java.sql.DriverManager;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private Label lblMessage;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;

    public void passwordTextButtonAction(ActionEvent actionEvent) {


    }

    public void loginButtonAction(ActionEvent actionEvent) throws Exception {

        boolean check = true;
        String query;
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        password = new md5Crypt().md5Apache(password);

        Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/catalogdb?useSSL=false", "root", "root");
        Statement stmt = (Statement) con.createStatement();
        query = "SELECT Id, Username, Password, Role FROM users;";
        stmt.executeQuery(query);
        ResultSet rs = stmt.getResultSet();
        while (rs.next()) {

            String dbUsername = rs.getString("username");
            String dbPassword = rs.getString("password");

            if (username.equals(dbUsername) && password.equals(dbPassword)) {

                //UsersEntity user = new UsersEntity(username, password, 2);
                int id = rs.getInt(("id"));

               // UserRepository user = new UserRepository();


                UserRepository user = new UserRepository().getInstance();

                user.setId(id);
                user.setUsername(username);
                int role = rs.getInt(("role"));
                user.setRole(role);

                /*query = "UPDATE `catalogdb`.`users` SET `connected`='1' WHERE `Id`= ?";
                PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
                preparedStmt.setInt(1, user.getId());
                preparedStmt.executeUpdate();*/

                con.close();

                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

                Parent parent = FXMLLoader.load(getClass().getResource("Catalog.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(parent);
                stage.setScene(scene);
                stage.setTitle("Catalog");
                stage.show();
                break;
            }
        }
        lblMessage.setStyle("-fx-color: red");
        lblMessage.setText("Wrong username or password!");

    }

    public void usernameTextButtonAction(ActionEvent actionEvent) {
    }

    public void guestAction(ActionEvent actionEvent) throws Exception{

        UserRepository user = new UserRepository().getInstance();

        user.setId(2);
        user.setUsername("guest");
        user.setRole(3);
        //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        Catalog catalog = new Catalog(new Stage());
       /* Parent parent = FXMLLoader.load(getClass().getResource("Catalog.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Catalog");
        stage.show();*/

    }

    public void registerButtonAction(ActionEvent actionEvent) throws Exception {

        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        Parent parent = FXMLLoader.load(getClass().getResource("Register.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Registration Form");
        stage.show();

    }
};
