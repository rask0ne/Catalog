package design;


import com.mysql.jdbc.Connection;
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

    public void searchAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        String text = srchText.getText();

        updateTableView(text);

    }

    public void uploadActionButton(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {

       if(!UserRepository.getInstance().getName().equals("guest")) {
           Connection con = (Connection) DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/catalogdb?autoReconnect=true&useSSL=false", "root", "root");


            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                    new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));

            File selectedFile = fileChooser.showOpenDialog(/*root.getScene().getWindow()*/new Stage());
            FileInputStream inputStream = new FileInputStream(selectedFile);
            byte[] array = new byte[(int) selectedFile.length()];
            inputStream.read(array);


            String query = "insert into files (filename, file, username)" + " values (?, ?, ?)";
            PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(query);
            pstmt.setString(1, selectedFile.getName());
            pstmt.setBytes(2, array);
            pstmt.setString(3, UserRepository.getInstance().getName());

            pstmt.execute();

            con.close();

            updateTableView();
        }

    }

    public void deleteActionButton(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        FileRepository file = (FileRepository) tableView.getSelectionModel().getSelectedItem();



        if(file.getUsername().equals(UserRepository.getInstance().getName())
                || UserRepository.getInstance().getName().equals("admin")) {

            Connection con = (Connection) DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/catalogdb?autoReconnect=true&useSSL=false", "root", "root");

            String query = "delete from files where filename = ? and username = ?";
            PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(query);
            pstmt.setString(1, file.getFileName());
            pstmt.setString(2, file.getUsername());
            pstmt.execute();
            pstmt = (PreparedStatement) con.prepareStatement("alter table files auto_increment = 1");
            pstmt.execute();

            con.close();

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

    }

    public void openButtonAction(ActionEvent actionEvent) throws IOException, SQLException {

        FileRepository file = (FileRepository) tableView.getSelectionModel().getSelectedItem();
        file.execution();

    }
}


