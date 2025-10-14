module Interface {
    requires javafx.controls;
    requires javafx.fxml;


    exports Inteface;
    opens Inteface to javafx.fxml;
}