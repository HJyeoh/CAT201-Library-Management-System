module com.codewithmosh.librarytry2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.codewithmosh.librarytry2 to javafx.fxml;
    exports com.codewithmosh.librarytry2;
}