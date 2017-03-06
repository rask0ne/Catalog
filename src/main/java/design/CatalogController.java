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

        updateTableView();

    }

    public void searchAction(ActionEvent actionEvent) {

        String text = srchText.getText();


    }

    public void uploadActionButton(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

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


            String query = "insert into files (filename, file, username)" + " values (?, ?, ?)";
            PreparedStatement pstmt = (PreparedStatement)con.prepareStatement(query);
            pstmt.setString(1, selectedFile.getName());
            pstmt.setBytes(2, array);
            pstmt.setString(3, UserRepository.getInstance().getName());

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

//            con.commit();

        con.close();

        updateTableView();


    }

    public void deleteActionButton(ActionEvent actionEvent) {
    }

    public void updateTableView() throws SQLException, ClassNotFoundException {


        FileDataAccessor dataAccessor = new FileDataAccessor("jdbc:mysql://localhost:3306/catalogdb?useSSL=false", "root", "root"); // provide driverName, dbURL, user, password...

        TableView<FileRepository> personTable = new TableView<>();
        TableColumn<FileRepository, String> firstNameCol = new TableColumn<>("File Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<FileRepository, String>("fileName"));
        TableColumn<FileRepository, String> usernameCol = new TableColumn<>("User");
        usernameCol.setCellValueFactory(new PropertyValueFactory<FileRepository, String>("username"));


        tableView.getItems().clear();
        if(tableView.getColumns().isEmpty())
            tableView.getColumns().addAll(firstNameCol, usernameCol);


        tableView.getItems().addAll(dataAccessor.getPersonList());

    }
}
