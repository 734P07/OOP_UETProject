module com.btl {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.btl to javafx.fxml;
    exports com.btl;
}
