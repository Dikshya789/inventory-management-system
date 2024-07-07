module com.dikshya.inventorymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires servlet.api;
    requires org.json;
    requires java.sql;
    requires javafx.base;

    opens com.dikshya.inventorymanagementsystem to javafx.fxml;
    exports com.dikshya.inventorymanagementsystem;

    opens com.dikshya.inventorymanagementsystem.classes to javafx.base;
    exports com.dikshya.inventorymanagementsystem.classes;
}