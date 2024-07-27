module com.shop {
    requires javafx.controls;
    requires javafx.fxml; 
    requires java.sql;
    requires com.opencsv;
    requires javafx.base;
    requires javafx.graphics; // Required for SQL functionality 
    opens com.shop to javafx.fxml;
    exports com.shop;
}
