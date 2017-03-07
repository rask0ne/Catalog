package design;


import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import hibernate.Util.FileDataAccessor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.log4j.Logger;
import repositories.FileRepository;
import repositories.UserRepository;


import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

import java.sql.Date;
import java.sql.DriverManager;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;


/**
 * Created by rask on 01.03.2017.
 */

/**
 * Controller of 'Catalog' window.
 */
public class CatalogController {

    /**
     * Username from current session is shown here
     */
    @FXML
    private Label lblTextMessage;
    /**
     * Field to imput string for search
     */
    @FXML
    private TextField srchText;
    /**
     * Table where files are shown
     */
    @FXML
    private TableView<FileRepository> tableView;
    @FXML
    /**
     * Button to upload file
     */
    private Button uplButton;
    @FXML
    /**
     * Button to delete file
     */
    private Button dltButton;

    /**
     * Method to initialize table with all files first time and
     * change Label username info to current user
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {

        lblTextMessage.setText(UserRepository.getInstance().getName());

        updateTableView();

    }

    /**
     * Logger class initialize
     */
    private final Logger logger = Logger.getLogger(CatalogController.class);

    /**
     * Method to search files by entering string in Search TextField.
     * This string imputs in overloaded method updateTableView(String);
     *
     * @param actionEvent
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void searchAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        String text = srchText.getText();

        updateTableView(text);

    }

    /**
     * Algorithm of uploading file to database. Same time in history table in database
     * uploading info about data of upload, file size and who uploaded this file.
     * In this methoh establishing connection to the database, checking limit to
     * upload for current user and aploading file to files table
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void uploadActionButton(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {
        logger.info("Upload button pressed");
        if(!UserRepository.getInstance().getName().equals("guest")) {
           Connection con = (Connection) DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/catalogdb?autoReconnect=true&useSSL=false", "root", "root");

            logger.info("User tries to upload file");
            Statement stmt = (Statement) con.createStatement();
           String query = "SELECT username, downloadDate, fileSize FROM history;";
           stmt.executeQuery(query);
           ResultSet rs = stmt.getResultSet();
           int size = 0;
           while (rs.next()) {

               String dbUsername = rs.getString("username");
               Date dbDate = rs.getDate("downloadDate");
               Date date = new Date(Calendar.getInstance().getTime().getTime());

               if (dbUsername.equals(UserRepository.getInstance().getName()) )
                    if(dbDate.toString().equals(date.toString())){
                   size = size + rs.getInt("fileSize");
               }
           }
           if(size <= 10485760) {

               logger.info("User has some free space today");
               FileChooser fileChooser = new FileChooser();
               fileChooser.setTitle("Open Resource File");
               fileChooser.getExtensionFilters().addAll(
                       new FileChooser.ExtensionFilter("All Files", "*.*"),
                       new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.docx", "*.doc"),
                       new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                       new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                       new FileChooser.ExtensionFilter("Video Files", "*.mp4"));


               File selectedFile = fileChooser.showOpenDialog(/*root.getScene().getWindow()*/new Stage());
               int selectedFileSize = (int)selectedFile.length();
               if(selectedFileSize + size <= 10485760) {
                   FileInputStream inputStream = new FileInputStream(selectedFile);
                   byte[] array = new byte[(int) selectedFile.length()];
                   inputStream.read(array);

                   logger.info("File selected");

                   query = "insert into files (filename, file, username)" + " values (?, ?, ?)";
                   PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(query);
                   pstmt.setString(1, selectedFile.getName());
                   pstmt.setBytes(2, array);
                   pstmt.setString(3, UserRepository.getInstance().getName());

                   pstmt.execute();

                   logger.info("File uploaded into database");

                   query = "insert into history (username, downloadDate, fileSize)" + " values (?, ?, ?)";
                   pstmt = (PreparedStatement) con.prepareStatement(query);
                   pstmt.setString(1, UserRepository.getInstance().getName());
                   Date date = new Date(Calendar.getInstance().getTime().getTime());
                   pstmt.setDate(2, date);
                   pstmt.setInt(3, (int) selectedFile.length());

                   pstmt.execute();

                   logger.info("Saved info about this upload session");

                   con.close();

                   logger.info("Connection to DB closed");

                   updateTableView();

               }
           }
        }

    }

    /**
     * Algorithm of deleting file from database by its name and username, who uploaded this file.
     * Also there is a user role check, because admin has rights to delete all files, user
     * may delete only his files, guest has not such rights.
     * @param actionEvent
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void deleteActionButton(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        FileRepository file = (FileRepository) tableView.getSelectionModel().getSelectedItem();

        logger.info("Selected file to delete");

        if(file.getUsername().equals(UserRepository.getInstance().getName())
                || UserRepository.getInstance().getName().equals("admin")) {

            Connection con = (Connection) DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/catalogdb?autoReconnect=true&useSSL=false", "root", "root");

            logger.info("Connection to DB established");

            String query = "delete from files where filename = ? and username = ?";
            PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(query);
            pstmt.setString(1, file.getFileName());
            pstmt.setString(2, file.getUsername());
            pstmt.execute();
            pstmt = (PreparedStatement) con.prepareStatement("alter table files auto_increment = 1");
            pstmt.execute();

            logger.info("File deleted from DB");

            con.close();

            logger.info("Connection to DB closed");

            updateTableView();

        }

    }

    /**
     * Method to refresh data in table after some manipulations with files database. Works
     * after any changes such as uploading or deleting.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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


        tableView.getItems().addAll(dataAccessor.getFileList());

        logger.info("TableView updated");


    }

    /**
     * Overloaded method to refresh data after searching for some files. Gets string and then
     * forms list according to this inquiry.
     * @param name
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void updateTableView(String name) throws SQLException, ClassNotFoundException {


        FileDataAccessor dataAccessor = new FileDataAccessor("jdbc:mysql://localhost:3306/catalogdb?useSSL=false", "root", "root"); // provide driverName, dbURL, user, password...

        TableView<FileRepository> personTable = new TableView<>();
        TableColumn<FileRepository, String> firstNameCol = new TableColumn<>("File Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<FileRepository, String>("fileName"));
        TableColumn<FileRepository, String> usernameCol = new TableColumn<>("User");
        usernameCol.setCellValueFactory(new PropertyValueFactory<FileRepository, String>("username"));


        tableView.getItems().clear();
        if(tableView.getColumns().isEmpty())
            tableView.getColumns().addAll(firstNameCol, usernameCol);


        tableView.getItems().addAll(dataAccessor.getSearchFileList(name));

        logger.info("TableView with found files updated");
    }

    /**
     * Button to open file.
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void openButtonAction(ActionEvent actionEvent) throws IOException, SQLException {

        FileRepository file = (FileRepository) tableView.getSelectionModel().getSelectedItem();
        file.execution();

    }

    /**
     * Button to change current user. Creates window 'Login'.
     * @param actionEvent
     * @throws IOException
     */
    public void changeUserButton(ActionEvent actionEvent) throws IOException {

        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        Parent parent = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();

        logger.info("Created window 'Login' from changeUserButton");

    }
}


