package repositories;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import design.Catalog;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.*;
import java.sql.Blob;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rask on 03.03.2017.
 */

/**
 * Class to store information about file into object to integrage in list of files
 * for table in 'Catalog' window.
 */
public class FileRepository {

    private final Logger logger = Logger.getLogger(FileRepository.class);

    private final StringProperty fileName = new SimpleStringProperty(this, "fileName");
    public StringProperty fileNameProperty() {
        return fileName ;
    }
    public final String getFileName() {
        return fileNameProperty().get();
    }
    public final void setFileName(String fileName) {
        fileNameProperty().set(fileName);
    }

    private final StringProperty username = new SimpleStringProperty(this, "username");
    public StringProperty usernameProperty() {
        return username ;
    }
    public final String getUsername() {
        return usernameProperty().get();
    }
    public final void setUsername(String username) {
        usernameProperty().set(username);
    }

    public FileRepository() {}

    public FileRepository(String fileName, String username) {
        setFileName(fileName);
        setUsername(username);
    }

    /**
     * Method to load file from database and then run it on computer with process.
     * @throws SQLException
     * @throws IOException
     */
    public void execution() throws SQLException, IOException {

        Connection conn = (Connection) DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/catalogdb?autoReconnect=true&useSSL=false", "root", "root");
        logger.info("Connection to DB established");
        String sql = "SELECT file FROM files WHERE filename =? AND username=?";
        PreparedStatement statement = (PreparedStatement) conn.prepareStatement(sql);
        statement.setString(1, getFileName());
        statement.setString(2, getUsername());

        ResultSet result = statement.executeQuery();
        if (result.next()) {
            Blob blob = result.getBlob("file");
            InputStream inputStream = blob.getBinaryStream();
            OutputStream outputStream = new FileOutputStream("E:/temp/" + getFileName());

            int bytesRead = -1;
            byte[] buffer = new byte[(int)blob.length()];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            File file = new File("E:/temp/" + getFileName());
            Desktop.getDesktop().open(file);
            logger.info("File opened  successfully");
        }
        conn.close();
        logger.info("Connection to DB closed");

    }


}
