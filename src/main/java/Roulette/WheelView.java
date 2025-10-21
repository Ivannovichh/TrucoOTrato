package Roulette;

import javafx.animation.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WheelView extends Pane {
    private static final int SECTORS = 7; // 7 casillas
    private final double RADIUS; // Radio escalado según pantalla
    private final double WIDTH; // Ancho de pantalla
    private final double HEIGHT; // Alto de pantalla
    private Canvas wheelCanvas; // Canvas para la ruleta
    private Button spinButton;
    private RotateTransition spinAnimation;
    private double currentAngle = 0;
    private List<Sector> sectors = new ArrayList<>();
    private Random random = new Random();
    private MediaPlayer spinPlayer; // Sonido de giro
    private Image backgroundImg;
    private String nombre, apellidos, curso; // Datos del usuario
    private Label welcomeLabel, resultLabel; // Etiquetas para mensajes
    private ImageView backgroundView; // Fondo estático
    private Polygon indicator; // Triángulo indicador estático

    public WheelView() {
        // Obtener dimensiones de la pantalla
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        WIDTH = screenBounds.getWidth();
        HEIGHT = screenBounds.getHeight();
        RADIUS = Math.min(WIDTH, HEIGHT) * 0.35; // 35% del menor lado

        // Inicializar fondo estático
        try {
            backgroundImg = new Image(getClass().getResourceAsStream("/background.jpg"));
        } catch (Exception e) {
            backgroundImg = new Image(getClass().getResourceAsStream("/Inteface/background.jpg"));
        }
        backgroundView = new ImageView(backgroundImg);
        backgroundView.setFitWidth(WIDTH);
        backgroundView.setFitHeight(HEIGHT);

        // Inicializar canvas para la ruleta
        wheelCanvas = new Canvas(WIDTH, HEIGHT);
        initResources();
        initSectors();
        drawWheel();
        setupIndicator();
        setupButtons();
        setupAnimations();

        // Agregar elementos al Pane
        getChildren().addAll(backgroundView, wheelCanvas, indicator, spinButton);
    }

    // Método open para inicializar con datos del usuario
    public void open(String nombre, String apellidos, String curso) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.curso = curso;

        // Etiqueta de bienvenida
        welcomeLabel = new Label("¡Bienvenid@, " + nombre + " " + apellidos + " del curso " + curso + "!");
        welcomeLabel.setStyle("-fx-font-family: 'Creepster'; -fx-font-size: " + (WIDTH * 0.015) + "; -fx-text-fill: #FF0000; -fx-background-color: rgba(0,0,0,0.8); -fx-padding: 12; -fx-effect: dropshadow(gaussian, #FFFFFF, 10, 0.5, 0, 0);");
        welcomeLabel.setLayoutX(WIDTH * 0.3);
        welcomeLabel.setLayoutY(HEIGHT * 0.05);
        getChildren().add(welcomeLabel);

        // Etiqueta para resultados
        resultLabel = new Label("");
        resultLabel.setStyle("-fx-font-family: 'Creepster'; -fx-font-size: " + (WIDTH * 0.02) + "; -fx-text-fill: #FF0000; -fx-background-color: rgba(0,0,0,0.8); -fx-padding: 15; -fx-effect: dropshadow(gaussian, #FFFFFF, 10, 0.5, 0, 0);");
        resultLabel.setLayoutX(WIDTH * 0.35);
        resultLabel.setLayoutY(HEIGHT * 0.75);
        getChildren().add(resultLabel);
    }

    private void initResources() {
        // Cargar sonido de giro
        try {
            spinPlayer = new MediaPlayer(new Media(getClass().getResource("/spin.mp3").toString()));
        } catch (Exception e) {
            spinPlayer = new MediaPlayer(new Media(getClass().getResource("/Inteface/spin.mp3").toString()));
        }
    }

    private void initSectors() {
        // Distribución: 3 TRUCO, 3 TRATO, 1 OSKAR (OSKAR en naranja)
        String[] labels = {"TRUCO", "TRATO", "OSKAR", "TRUCO", "TRATO", "TRUCO", "TRATO"};
        Color[] colors = {Color.RED, Color.BLACK, Color.ORANGE, Color.BLACK, Color.RED, Color.RED, Color.BLACK};
        for (int i = 0; i < SECTORS; i++) {
            sectors.add(new Sector(labels[i], colors[i]));
        }
    }

    private void drawWheel() {
        GraphicsContext gc = wheelCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        double centerX = WIDTH / 2, centerY = HEIGHT / 2;
        double angleStep = 360.0 / SECTORS;

        // Fondo de la ruleta
        gc.setFill(Color.BLACK);
        gc.fillOval(centerX - RADIUS - 10, centerY - RADIUS - 10, 2 * RADIUS + 20, 2 * RADIUS + 20);

        // Dibujar casillas
        for (int i = 0; i < SECTORS; i++) {
            gc.setFill(sectors.get(i).color);
            gc.fillArc(centerX - RADIUS, centerY - RADIUS, 2 * RADIUS, 2 * RADIUS,
                    currentAngle + i * angleStep, angleStep, javafx.scene.shape.ArcType.ROUND);
            // Borde plateado
            gc.setStroke(Color.SILVER);
            gc.setLineWidth(2);
            gc.strokeArc(centerX - RADIUS, centerY - RADIUS, 2 * RADIUS, 2 * RADIUS,
                    currentAngle + i * angleStep, angleStep, javafx.scene.shape.ArcType.ROUND);
            // Texto
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Creepster", WIDTH * 0.015));
            gc.setEffect(new DropShadow(5, Color.BLACK));
            double textX = centerX + Math.cos(Math.toRadians(currentAngle + (i + 0.5) * angleStep)) * RADIUS * 0.7;
            double textY = centerY + Math.sin(Math.toRadians(currentAngle + (i + 0.5) * angleStep)) * RADIUS * 0.7;
            gc.fillText(sectors.get(i).label, textX, textY);
        }
        gc.setEffect(null);
    }

    private void setupIndicator() {
        // Triángulo indicador estático, girado 180 grados sin mover su posición
        indicator = new Polygon();
        double centerX = WIDTH / 2;
        double centerY = HEIGHT / 2;
        indicator.getPoints().addAll(
                centerX - RADIUS - 20, centerY - 15, // Superior
                centerX - RADIUS + 10, centerY,      // Punta derecha (hacia afuera)
                centerX - RADIUS - 20, centerY + 15  // Inferior
        );
        indicator.setFill(Color.RED);
        indicator.setStroke(Color.BLACK);
        indicator.setStrokeWidth(3);
        // Detalle de "sangre"
        Circle blood = new Circle(centerX - RADIUS + 5, centerY, 5, Color.DARKRED);
        getChildren().add(blood);
    }

    private void setupButtons() {
        spinButton = new Button("¡GIRAR RULETA!");
        spinButton.setStyle("-fx-font-family: 'Creepster'; -fx-font-size: " + (WIDTH * 0.015) + "; -fx-background-color: #000000; -fx-text-fill: #FF0000; -fx-padding: 12 24; -fx-effect: dropshadow(gaussian, #FFFFFF, 10, 0.5, 0, 0);");
        spinButton.setLayoutX(WIDTH * 0.45);
        spinButton.setLayoutY(HEIGHT * 0.92); // Separado de la ruleta
        spinButton.setOnAction(e -> spinWheel());
    }

    private void setupAnimations() {
        spinAnimation = new RotateTransition(Duration.seconds(random.nextDouble() * 2 + 3), wheelCanvas);
        spinAnimation.setFromAngle(0);
        spinAnimation.setToAngle(360 * 5 + random.nextDouble() * 360); // 5 vueltas + random
        spinAnimation.setInterpolator(Interpolator.EASE_OUT);
        spinAnimation.setOnFinished(e -> {
            currentAngle = (currentAngle + spinAnimation.getToAngle()) % 360;
            drawWheel();
            Sector winner = getWinnerSector();
            executeEffect(winner);
            spinPlayer.stop();
            // Limpiar efectos
            getChildren().removeIf(node -> node instanceof Circle);
            resultLabel.setText("");
            spinButton.setDisable(false);
        });
    }

    private void spinWheel() {
        spinButton.setDisable(true);
        spinPlayer.seek(Duration.ZERO);
        spinPlayer.play();
        spinAnimation.playFromStart();
    }

    private Sector getWinnerSector() {
        double angle = (360 - (currentAngle % 360)) % 360;
        int sectorIndex = (int) (angle / (360.0 / SECTORS));
        return sectors.get(sectorIndex);
    }

    private void executeEffect(Sector winner) {
        switch (winner.label) {
            case "TRUCO":
                vibrateScreen();
                resultLabel.setText(nombre + ", ¡TRUCO escalofriante!");
                break;
            case "TRATO":
                confettiEffect();
                resultLabel.setText(nombre + ", ¡TRATO lleno de dulces!");
                break;
            case "OSKAR":
                fireworks();
                resultLabel.setText(nombre + ", ¡OSKAR, rey del terror!");
                break;
        }
        // Animar el mensaje de resultado
        FadeTransition fade = new FadeTransition(Duration.seconds(3), resultLabel);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setDelay(Duration.seconds(2));
        fade.play();
    }

    private void vibrateScreen() {
        TranslateTransition vib = new TranslateTransition(Duration.millis(2000), wheelCanvas);
        vib.setFromX(0);
        vib.setToX(10);
        vib.setAutoReverse(true);
        vib.setCycleCount(10);
        vib.play();
    }

    private void confettiEffect() {
        Random rand = new Random();
        for (int i = 0; i < 60; i++) {
            Circle particle = new Circle(5, rand.nextBoolean() ? Color.RED : Color.BLACK);
            particle.setLayoutX(rand.nextDouble() * WIDTH);
            particle.setLayoutY(0);
            getChildren().add(particle);
            TranslateTransition fall = new TranslateTransition(Duration.seconds(2 + rand.nextDouble()), particle);
            fall.setToY(HEIGHT);
            fall.setOnFinished(e -> getChildren().remove(particle));
            fall.play();
        }
    }

    private void fireworks() {
        Random rand = new Random();
        for (int i = 0; i < 15; i++) {
            Circle spark = new Circle(8, rand.nextBoolean() ? Color.RED : Color.BLACK);
            spark.setLayoutX(WIDTH / 2);
            spark.setLayoutY(HEIGHT / 2);
            getChildren().add(spark);
            double angle = rand.nextDouble() * 360;
            TranslateTransition explode = new TranslateTransition(Duration.seconds(1.5), spark);
            explode.setToX(WIDTH / 2 + Math.cos(Math.toRadians(angle)) * RADIUS);
            explode.setToY(HEIGHT / 2 + Math.sin(Math.toRadians(angle)) * RADIUS);
            explode.setOnFinished(e -> getChildren().remove(spark));
            explode.play();
        }
    }

    static class Sector {
        String label;
        Color color;

        Sector(String label, Color color) {
            this.label = label;
            this.color = color;
        }
    }
}