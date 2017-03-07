package design;


import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import hibernate.Util.FileDataAccessor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;

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

    private final Logger logger = Logger.getLogger(CatalogController.class);

    public void searchAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        String text = srchText.getText();

        updateTableView(text);

    }

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

    public void openButtonAction(ActionEvent actionEvent) throws IOException, SQLException {

        FileRepository file = (FileRepository) tableView.getSelectionModel().getSelectedItem();
        file.execution();

    }
}


