package hibernate.Util;

import com.mysql.jdbc.Connection;
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
public class FileDataAccessor {

    private Connection connection ;

    public FileDataAccessor(String dbURL, String user, String password) throws SQLException, ClassNotFoundException {
        //Class.forName(driverClassName);
        connection = (Connection) DriverManager.getConnection(dbURL, user, password);
    }

    public void shutdown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public List<FileRepository> getPersonList() throws SQLException {

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
            return filesList ;
        }
    }

}
