package hibernate.Util;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import design.Login;
import org.apache.log4j.Logger;
import repositories.FileRepository;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rask on 06.03.2017.
 */

/**
 * Class to connect database with table in 'Catalog'.
 */
public class FileDataAccessor {

    private Connection connection ;

    private final Logger logger = Logger.getLogger(FileDataAccessor.class);

    /**
     * Method to establish connection to the database.
     * @param dbURL URL to database.
     * @param user  username
     * @param password password
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public FileDataAccessor(String dbURL, String user, String password) throws SQLException, ClassNotFoundException {
        //Class.forName(driverClassName);
        connection = (Connection) DriverManager.getConnection(dbURL, user, password);
        logger.info("Connection to DB establihed");
    }

    /**
     * Method to close connection to the database.
     * @throws SQLException
     */
    public void shutdown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Creating list of all files to import it into table in 'Catalog' window.
     *
     * @return list of files to integrate.
     * @throws SQLException
     */
    public List<FileRepository> getFileList() throws SQLException {

        Statement stmnt = connection.createStatement();
        ResultSet rs = stmnt.executeQuery("select filename, username from files");
        {
            List<FileRepository> filesList = new ArrayList<>();
            while (rs.next()) {
                String fileName = rs.getString("Filename");
                String username = rs.getString("Username");
                FileRepository file = new FileRepository(fileName, username);
                filesList.add(file);
            }
            logger.info("Created full list of file for tableView");
            return filesList ;
        }
    }

    /**
     * Creating list of files which comparable with search string.
     * @param name string to compare
     * @return list of files to integrate.
     * @throws SQLException
     */
    public List<FileRepository> getSearchFileList(String name) throws SQLException {

        String task = "select filename, username from files where filename like ? or username like ?";
        PreparedStatement stmnt = (PreparedStatement) connection.prepareStatement(task);
        stmnt.setString(1, "%" + name + "%");
        stmnt.setString(2, "%" + name + "%");
        ResultSet rs = stmnt.executeQuery();
        {
            List<FileRepository> filesList = new ArrayList<>();
            while (rs.next()){
                String fileName = rs.getString("Filename");
                String username = rs.getString("Username");
                FileRepository file = new FileRepository(fileName, username);
                filesList.add(file);
            }
            logger.info("Created list of files for tableView which were searched");
            return filesList ;
        }
    }

}
