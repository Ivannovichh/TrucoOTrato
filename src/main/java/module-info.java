module Interface {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    exports Inteface;
    opens Inteface to javafx.fxml;
    exports Roulette;
    opens Roulette to  javafx.fxml;
}