module Interface {
    requires javafx.controls;
    requires javafx.fxml;


    exports Inteface;
    opens Inteface to javafx.fxml;
    exports Roulette;
    opens Roulette to  javafx.fxml;
}