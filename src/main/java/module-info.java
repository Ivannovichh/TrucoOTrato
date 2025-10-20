module Interface {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // <-- Permite a FXMLLoader instanciar controladores con @FXML
    opens Inteface to javafx.fxml;
    opens Roulette to javafx.fxml;

    // <-- Exporta si otras clases fuera del módulo los usan
    exports Inteface;
    exports Roulette;
}
