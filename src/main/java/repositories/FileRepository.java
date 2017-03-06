package repositories;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by rask on 03.03.2017.
 */
public class FileRepository {

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

    public FileRepository() {}

    public FileRepository(String fileName) {
        setFileName(fileName);
        }

}
