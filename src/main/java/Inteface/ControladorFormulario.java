package Inteface; // Define el paquete donde se encuentra esta clase

import Roulette.WheelView; // Importa la clase WheelView para mostrar la ruleta
import javafx.animation.PauseTransition; // Importa la clase para animaciones con pausas
import javafx.fxml.FXML; // Importa la anotación para vincular elementos FXML
import javafx.scene.Node; // Importa la clase base para todos los nodos de la escena
import javafx.scene.Scene; // Importa la clase que representa una escena de JavaFX
import javafx.scene.control.*; // Importa los controles de interfaz (Label, Button, etc.)
import javafx.stage.Stage; // Importa la clase Stage para manejar ventanas
import javafx.util.Duration; // Importa la clase para definir duraciones de animación
import java.util.Random; // Importa la clase para generar números aleatorios

public class ControladorFormulario { // Clase controladora del formulario inicial

    @FXML private Label titulo; // Etiqueta del título principal
    @FXML private TextField nombreField; // Campo de texto para el nombre
    @FXML private TextField apellidosField; // Campo de texto para los apellidos
    @FXML private ComboBox<String> cursoComboBox; // Menú desplegable para elegir curso
    @FXML private Button continuarBtn; // Botón para continuar al siguiente paso

    private final Random random = new Random(); // Generador de números aleatorios para animaciones

    @FXML
    public void initialize() { // Método que se ejecuta automáticamente al cargar la interfaz
        if (!titulo.getStyleClass().contains("titulo-neon-bright")) { // Verifica si el título tiene la clase CSS
            titulo.getStyleClass().add("titulo-neon-bright"); // Añade el efecto brillante al título
        }
        iniciarParpadeo(titulo, 220, 620); // Inicia el efecto de parpadeo entre estilos del título
    }

    @FXML
    private void onContinuar() { // Método que se ejecuta al pulsar el botón "Continuar"
        String nombre = nombreField.getText() == null ? "" : nombreField.getText().trim(); // Obtiene el nombre y elimina espacios
        String apellidos = apellidosField.getText() == null ? "" : apellidosField.getText().trim(); // Obtiene los apellidos
        String curso = cursoComboBox.getValue(); // Obtiene el curso seleccionado

        // Comprueba que todos los campos estén completos
        if (nombre.isEmpty() || apellidos.isEmpty() || curso == null || curso.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING); // Muestra una alerta de advertencia
            alert.setTitle("Campos incompletos"); // Título de la alerta
            alert.setHeaderText(null); // Sin encabezado
            alert.setContentText("Rellena Nombre, Apellidos y selecciona un Curso antes de continuar."); // Mensaje de aviso
            alert.showAndWait(); // Espera a que el usuario cierre la alerta
            return; // Sale del método si faltan datos
        }

        // Crea la vista de la ruleta y pasa los datos del formulario
        WheelView wheelView = new WheelView(); // Instancia la ruleta
        wheelView.open(nombre, apellidos, curso); // Muestra el mensaje de bienvenida

        // Crea una nueva escena con la ruleta
        Scene wheelScene = new Scene(wheelView, 800, 600); // Define tamaño base (se ajustará a pantalla completa)
        Stage stage = new Stage(); // Crea una nueva ventana (Stage)
        stage.setScene(wheelScene); // Asocia la escena a la ventana
        stage.setFullScreen(true); // Muestra la ruleta en pantalla completa
        stage.setFullScreenExitHint(""); // Quita el mensaje de salida de pantalla completa
        stage.setFullScreenExitKeyCombination(null); // Desactiva combinación de teclas para salir
        stage.show(); // Muestra la nueva ventana

        // Cierra la ventana actual del formulario
        Stage currentStage = (Stage) continuarBtn.getScene().getWindow(); // Obtiene la ventana actual
        currentStage.close(); // La cierra
    }

    private void iniciarParpadeo(Node nodo, int minMillis, int maxMillis) { // Crea un efecto de parpadeo entre dos estilos CSS
        int intervalo = minMillis + random.nextInt(Math.max(1, maxMillis - minMillis)); // Calcula un intervalo aleatorio
        PauseTransition pausa = new PauseTransition(Duration.millis(intervalo)); // Crea una pausa con esa duración
        pausa.setOnFinished(event -> { // Cuando termina la pausa...
            alternarClase(nodo); // Alterna la clase CSS (brillante ↔ tenue)
            iniciarParpadeo(nodo, minMillis, maxMillis); // Reinicia el parpadeo recursivamente
        });
        pausa.play(); // Inicia la animación
    }

    private void alternarClase(Node nodo) { // Alterna entre las clases CSS "bright" y "dim"
        if (nodo.getStyleClass().contains("titulo-neon-bright")) { // Si el nodo tiene el estilo brillante
            nodo.getStyleClass().remove("titulo-neon-bright"); // Lo elimina
            if (!nodo.getStyleClass().contains("titulo-neon-dim")) { // Si no tiene el estilo tenue
                nodo.getStyleClass().add("titulo-neon-dim"); // Lo añade
            }
        } else { // Si el nodo no tiene el estilo brillante
            nodo.getStyleClass().remove("titulo-neon-dim"); // Quita el estilo tenue
            if (!nodo.getStyleClass().contains("titulo-neon-bright")) { // Si no tiene el estilo brillante
                nodo.getStyleClass().add("titulo-neon-bright"); // Lo añade
            }
        }
    }
}
