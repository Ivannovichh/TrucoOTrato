package Inteface;

import Roulette.WheelView;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Duration;
import java.util.Random;

public class ControladorFormulario {

    @FXML private Label titulo;
    @FXML private TextField nombreField;
    @FXML private TextField apellidosField;
    @FXML private ComboBox<String> cursoComboBox;
    @FXML private Button continuarBtn;

    private final Random random = new Random();

    @FXML
    public void initialize() {
        if (!titulo.getStyleClass().contains("titulo-neon-bright")) {
            titulo.getStyleClass().add("titulo-neon-bright");
        }
        iniciarParpadeo(titulo, 220, 620);
    }

    @FXML
    private void onContinuar() {
        String nombre = nombreField.getText() == null ? "" : nombreField.getText().trim();
        String apellidos = apellidosField.getText() == null ? "" : apellidosField.getText().trim();
        String curso = cursoComboBox.getValue();

        // Validación de campos vacíos
        if (nombre.isEmpty() || apellidos.isEmpty() || curso == null || curso.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Rellena Nombre, Apellidos y selecciona un Curso antes de continuar.");
            alert.showAndWait();
            return;
        }

        // Llamar al Launcher de la ruleta
        try {
            WheelView wheelView = new WheelView();
            wheelView.open(nombre, apellidos, curso);
        } catch (Exception e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error al abrir la ruleta");
            error.setHeaderText(null);
            error.setContentText("Ha ocurrido un error al abrir la ruleta. Revisa la consola para más detalles.");
            error.showAndWait();
        }
    }

    private void iniciarParpadeo(Node nodo, int minMillis, int maxMillis) {
        int intervalo = minMillis + random.nextInt(Math.max(1, maxMillis - minMillis));
        PauseTransition pausa = new PauseTransition(Duration.millis(intervalo));
        pausa.setOnFinished(event -> {
            alternarClase(nodo);
            iniciarParpadeo(nodo, minMillis, maxMillis);
        });
        pausa.play();
    }

    private void alternarClase(Node nodo) {
        if (nodo.getStyleClass().contains("titulo-neon-bright")) {
            nodo.getStyleClass().remove("titulo-neon-bright");
            if (!nodo.getStyleClass().contains("titulo-neon-dim")) {
                nodo.getStyleClass().add("titulo-neon-dim");
            }
        } else {
            nodo.getStyleClass().remove("titulo-neon-dim");
            if (!nodo.getStyleClass().contains("titulo-neon-bright")) {
                nodo.getStyleClass().add("titulo-neon-bright");
            }
        }
    }
}
