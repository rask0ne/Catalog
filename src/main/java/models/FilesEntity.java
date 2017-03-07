package models;

import com.mysql.jdbc.Blob;

import javax.persistence.*;
import java.util.Arrays;

/**
 * Created by rask on 03.03.2017.
 */

/**
 * Entity for files table in the database.
 */
@Entity
@Table(name = "files", schema = "catalogdb", catalog = "")
public class FilesEntity {
    private int id;
    private String filename;
    private byte[] file;
    private String username;

    public FilesEntity() {
    }

    @Id
    @Column(name = "Id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Filename", nullable = false, length = 255)
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Basic
    @Column(name = "File", nullable = false)
    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    @Basic
    @Column(name = "Username", nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilesEntity that = (FilesEntity) o;

        if (id != that.id) return false;
        //if (userId != that.userId) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (filename != null ? !filename.equals(that.filename) : that.filename != null) return false;
        if (!Arrays.equals(file, that.file)) return false;

        return true;
    }

   /* public FilesEntity(String filename, int userId, byte[] file){

        this.filename = filename;
        this.userId = userId;
        this.file = file;
    }*/

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (filename != null ? filename.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(file);
        result = 31 * result + (username != null ? username.hashCode() : 0);
       // result = 31 * result + userId;
        return result;
    }
}
