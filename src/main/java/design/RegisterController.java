package design;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import crypt.md5Crypt;
import hibernate.Util.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.UsersEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ResourceBundle;

/**
 * Created by rask on 01.03.2017.
 */



public class RegisterController implements Initializable {

    @FXML
    private Label lblMessage;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;

    public void passwordTextButtonAction(ActionEvent actionEvent) {
    }

    public void usernameTextButtonAction(ActionEvent actionEvent) {
    }

    public void registerButtonAction(ActionEvent actionEvent) throws Exception{

        boolean check = true;
        String query;
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        password = new md5Crypt().md5Apache(password);

        Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/catalogdb?autoReconnect=true&useSSL=false", "root", "root");
        Statement stmt = (Statement) con.createStatement();
        query = "SELECT Username FROM users;";
        stmt.executeQuery(query);
        ResultSet rs = stmt.getResultSet();

        while(rs.next()){
           String DBusername = rs.getString("Username");
            if(username.equals(DBusername))    {

                check = false;
                lblMessage.setText("This username already exists!");
                break;

            }
        }

        if(check == true){

            query = "insert into users (username, password, role)"
                    + " values (?, ?, ?)";

            UsersEntity user = new UsersEntity(username, password, 2);
            PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
            preparedStmt.setString (1, username);
            preparedStmt.setString (2, password);
            preparedStmt.setInt(3, 2);
            preparedStmt.execute();

            con.close();
            /*SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            user.setUsername(username);
            user.setPassword(password);
            user.setRole(2);

            //Save the employee in database
            session.save(user);

            //Commit the transaction
            session.getTransaction().commit();
            session.close();*/

            user = null;

            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

            Parent parent = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();

        }

    }

    public void initialize(URL location, ResourceBundle resources) {

    }
}
