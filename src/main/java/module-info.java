module com.example.yahtzeeextreme {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.yahtzeeextreme to javafx.fxml;
    exports com.example.yahtzeeextreme;
}