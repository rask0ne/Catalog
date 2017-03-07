package models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;

/**
 * Created by rask on 07.03.2017.
 */
@Entity
@Table(name = "history", schema = "catalogdb", catalog = "")
public class HistoryEntity {
    private int id;
    private String username;
    private Date date;
    private int fileSize;

    public HistoryEntity() {
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
    @Column(name = "username", nullable = false, length = 255)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "downloaddate", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Basic
    @Column(name = "fileSize", nullable = false)
    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoryEntity that = (HistoryEntity) o;

        if (id != that.id) return false;
        //if (userId != that.userId) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (fileSize != that.fileSize) return false;

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
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + date.hashCode();
        result = 31 * result + fileSize;
        // result = 31 * result + userId;
        return result;
    }
}
