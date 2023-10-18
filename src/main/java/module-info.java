module com.btl {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.btl to javafx.fxml;
    exports com.btl;
}
