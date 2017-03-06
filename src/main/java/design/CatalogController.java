package design;


import com.mysql.jdbc.Connection;
import hibernate.Util.FileDataAccessor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import repositories.FileRepository;
import repositories.UserRepository;


import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

import java.sql.DriverManager;
import com.mysql.jdbc.PreparedStatement;

import java.sql.SQLException;

/*import com.mysql.jdbc.Connection;
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
import java.util.ResourceBundle;*/

/**
 * Created by rask on 01.03.2017.
 */
public class CatalogController {

    @FXML
    private Label lblTextMessage;
    @FXML
    private TextField srchText;
    @FXML
    private TableView<FileRepository> tableView;
    @FXML
    private Button uplButton;
    @FXML
    private Button dltButton;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {

        lblTextMessage.setText(UserRepository.getInstance().getName());
        FileDataAccessor dataAccessor = new FileDataAccessor("jdbc:mysql://localhost:3306/catalogdb?useSSL=false", "root", "root"); // provide driverName, dbURL, user, password...

        TableView<FileRepository> personTable = new TableView<>();
        TableColumn<FileRepository, String> firstNameCol = new TableColumn<>("File Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<FileRepository, String>("fileName"));


        tableView.getColumns().addAll(firstNameCol);

        tableView.getItems().addAll(dataAccessor.getPersonList());

    }

    public void searchAction(ActionEvent actionEvent) {
    }

    public void uploadActionButton(ActionEvent actionEvent) throws SQLException, IOException {

        //String updateSQL = "UPDATE files " + "SET file = ? " + "WHERE userid=?";


        //try  {
        Connection con = (Connection) DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/catalogdb?autoReconnect=true&useSSL=false", "root", "root");
       // con.setAutoCommit(false);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                    new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));

            Group root = new Group();
            final Stage mainStage = null;
            //mainStage.setScene(new Scene(root, 500, 400));
           // mainStage.show();
            File selectedFile = fileChooser.showOpenDialog(/*root.getScene().getWindow()*/new Stage());
            FileInputStream inputStream = new FileInputStream(selectedFile);
            byte[] array = new byte[(int)selectedFile.length()];
            inputStream.read(array);


            String query = "insert into files (filename, file, userId)" + " values (?, ?, ?)";
            PreparedStatement pstmt = (PreparedStatement)con.prepareStatement(query);
            pstmt.setString(1, selectedFile.getName());
            pstmt.setBytes(2, array);
            pstmt.setInt(3, UserRepository.getInstance().getId());

            /*Statement stmt = (Statement) conn.createStatement();
            String query = "SELECT Id, Connected FROM users;";
            stmt.executeQuery(query);
            ResultSet rs = stmt.getResultSet();

            int id = 0;
            while (rs.next()) {
                int count = rs.getInt("connected");
                if (count == 1) {
                    id = rs.getInt("id");
                    break;
                }
            }*/

       /* FilesEntity file_ = new FilesEntity();

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        file_.setFile(array);
        file_.setFilename(selectedFile.getName());
        file_.setUserId(UserRepository.getInstance().user.getId());

        //Save the employee in database
        session.save(file_);

        //Commit the transaction
        session.getTransaction().commit();
        session.close();*/


        pstmt.execute();

            con.commit();

            con.close();

            //Creating new column in files table

            /*SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();


            Blob blob = (Blob) Hibernate.getLobCreator(session)
                    .createBlob(inputStream, selectedFile.length());

           // FilesEntity file = new FilesEntity(selectedFile.getName(), id, blob);

            session.beginTransaction();
            //session.save(file);
            session.getTransaction().commit();
            session.close();*/

            // read the file
            //File file = new File(filename);


            // set parameters
            /*pstmt.setBinaryStream(1, inputStream);
            pstmt.setInt(2, id);

            // store the resume file in database
            System.out.println("Reading file " + selectedFile.getAbsolutePath());
            System.out.println("Store file in the database.");
            pstmt.executeUpdate();*/

        /*} catch (SQLException | FileNotFoundException e) {
            System.out.println(e.getMessage());
        }*/
        //}catch (SQLException e) {

    }

    public void deleteActionButton(ActionEvent actionEvent) {
    }
}
