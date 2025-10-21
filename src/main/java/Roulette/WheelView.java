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
    private static final int SECTORS = 6; // 6 casillas
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
    private ImageView backgroundView; // Fondo estático
    private Polygon indicator; // Triángulo indicador estático
    private Label welcomeLabel; // Etiqueta de bienvenida
    private Label resultLabel; // Etiqueta para mostrar el resultado

    public WheelView() {
        // Obtener dimensiones de la pantalla
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        WIDTH = screenBounds.getWidth();
        HEIGHT = screenBounds.getHeight();
        RADIUS = Math.min(WIDTH, HEIGHT) * 0.35; // 35% del menor lado

        // Inicializar fondo estático
        backgroundImg = new Image(getClass().getResourceAsStream("/Inteface/background.jpg"));
        backgroundView = new ImageView(backgroundImg);
        backgroundView.setFitWidth(WIDTH);
        backgroundView.setFitHeight(HEIGHT);

        // Inicializar canvas para la ruleta
        wheelCanvas = new Canvas(WIDTH, HEIGHT);
        initResources();
        initSectors();
        drawWheel(); // Dibujar la ruleta y texto fijo al inicio
        setupIndicator();
        setupButtons();
        setupAnimations();

        // Agregar elementos al Pane: fondo, canvas, botón, triángulo (al frente)
        getChildren().addAll(backgroundView, wheelCanvas, spinButton, indicator);
    }

    public void open(String nombre, String apellidos, String curso) {
        // Etiqueta de bienvenida centrada arriba
        welcomeLabel = new Label("¡Bienvenid@, " + nombre + " " + apellidos + " del curso " + curso + "!");
        welcomeLabel.setStyle("-fx-font-family: 'Creepster'; -fx-font-size: " + (WIDTH * 0.015) + "; -fx-text-fill: #FF0000; -fx-background-color: rgba(0,0,0,0.6); -fx-padding: 10; -fx-effect: dropshadow(gaussian, #FFFFFF, 10, 0.5, 0, 0);");
        welcomeLabel.setLayoutX(WIDTH * 0.35);
        welcomeLabel.setLayoutY(HEIGHT * 0.02);
        getChildren().add(welcomeLabel);
    }

    private void initResources() {
        spinPlayer = new MediaPlayer(new Media(getClass().getResource("/Inteface/spin.mp3").toString()));
    }

    private void initSectors() {
        // Distribución: 3 TRUCO (negro), 3 TRATO (rojo) para visualización
        String[] labels = {"TRUCO", "TRATO", "TRUCO", "TRATO", "TRUCO", "TRATO"};
        Color[] colors = {Color.BLACK, Color.RED, Color.BLACK, Color.RED, Color.BLACK, Color.RED};
        for (int i = 0; i < SECTORS; i++) {
            sectors.add(new Sector(labels[i], colors[i], i));
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
                    i * angleStep + currentAngle, angleStep, javafx.scene.shape.ArcType.ROUND);
            // Borde plateado
            gc.setStroke(Color.SILVER);
            gc.setLineWidth(2);
            gc.strokeArc(centerX - RADIUS, centerY - RADIUS, 2 * RADIUS, 2 * RADIUS,
                    i * angleStep + currentAngle, angleStep, javafx.scene.shape.ArcType.ROUND);
        }

        // Dibujar texto en cada sector con rotación dinámica
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Creepster", WIDTH * 0.015));
        gc.setEffect(new DropShadow(5, Color.BLACK));
        for (int i = 0; i < SECTORS; i++) {
            double textAngle = Math.toRadians(i * angleStep + angleStep / 2 + currentAngle); // Ajuste preciso
            double textX = centerX + Math.cos(textAngle) * RADIUS * 0.7;
            double textY = centerY + Math.sin(textAngle) * RADIUS * 0.7;
            gc.save();
            gc.translate(centerX, centerY);
            gc.rotate(i * angleStep + currentAngle); // Rotación exacta
            gc.fillText(sectors.get(i).label, -gc.getFont().getSize() / 2, -RADIUS * 0.7);
            gc.restore();
        }
        gc.setEffect(null);
    }

    private void setupIndicator() {
        // Triángulo indicador estático, apuntando a la izquierda (270°)
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
        spinAnimation = new RotateTransition(Duration.seconds(random.nextDouble() * 2 + 3), wheelCanvas); // Duración variable
        spinAnimation.setFromAngle(currentAngle); // Partir desde el ángulo actual
        int minTurns = 2, maxTurns = 5;
        double totalRotation = currentAngle + 360 * (minTurns + random.nextInt(maxTurns - minTurns + 1)) + random.nextDouble() * 360; // 2 a 5 vueltas + variación
        spinAnimation.setToAngle(totalRotation);
        spinAnimation.setInterpolator(Interpolator.EASE_OUT); // Deceleración natural
        spinAnimation.setOnFinished(e -> {
            currentAngle = spinAnimation.getToAngle(); // Usar el ángulo exacto de detención sin módulo
            drawWheel(); // Redibujar con el ángulo final exacto
            Sector winner = getWinnerSector(); // Determinar resultado basado en la posición final
            executeEffect(winner);
            spinPlayer.stop();
            showResult(winner.result); // Mostrar resultado basado en la casilla
            spinButton.setDisable(false); // Habilitar botón para otro giro
        });
    }

    private void spinWheel() {
        if (!spinButton.isDisabled()) { // Evitar múltiples clics simultáneos
            spinButton.setDisable(true);
            spinPlayer.play();
            spinAnimation.play();
        }
    }

    private Sector getWinnerSector() {
        // Determinar resultado basado en la posición final exacta bajo el indicador
        double angleStep = 360.0 / SECTORS;
        double finalAngle = spinAnimation.getToAngle() % 360; // Usar el ángulo exacto de detención
        int sectorIndex = (int) ((finalAngle / angleStep) % SECTORS); // Calcular sector sin ajuste
        Sector sector = sectors.get(sectorIndex);
        String result = (sector.color == Color.RED) ? "TRATO" : "TRUCO"; // Resultado según el color
        return new Sector(sector.label, sector.color, sectorIndex) {
            { this.result = result; }
        };
    }

    private void executeEffect(Sector winner) {
        double centerX = WIDTH / 2;
        double centerY = HEIGHT / 2;

        if ("TRUCO".equals(winner.result)) {
            // Animación de confeti para TRUCO
            for (int i = 0; i < 20; i++) {
                Circle confetti = new Circle(
                        centerX + random.nextDouble() * WIDTH * 0.4 - WIDTH * 0.2,
                        -10, // Comienzan fuera de la pantalla arriba
                        5 + random.nextDouble() * 5,
                        Color.rgb(
                                (int) (random.nextDouble() * 255),
                                (int) (random.nextDouble() * 255),
                                (int) (random.nextDouble() * 255),
                                0.8 // Opacidad
                        )
                );
                getChildren().add(confetti);
                TranslateTransition fall = new TranslateTransition(Duration.seconds(1 + random.nextDouble()), confetti);
                fall.setToY(HEIGHT + 10);
                RotateTransition rotate = new RotateTransition(Duration.seconds(1 + random.nextDouble()), confetti);
                rotate.setByAngle(360 + random.nextDouble() * 360);
                FadeTransition fade = new FadeTransition(Duration.seconds(1 + random.nextDouble()), confetti);
                fade.setToValue(0);
                ParallelTransition confettiTransition = new ParallelTransition(fall, rotate, fade);
                confettiTransition.setOnFinished(e -> getChildren().remove(confetti));
                confettiTransition.play();
            }
        } else if ("TRATO".equals(winner.result)) {
            // Animación de caramelos para TRATO
            for (int i = 0; i < 10; i++) {
                Circle candy = new Circle(
                        centerX + random.nextDouble() * WIDTH * 0.4 - WIDTH * 0.2,
                        -10, // Comienzan fuera de la pantalla arriba
                        10 + random.nextDouble() * 5,
                        Color.rgb(
                                (int) (random.nextDouble() * 128 + 127), // Colores pastel
                                (int) (random.nextDouble() * 128 + 127),
                                (int) (random.nextDouble() * 128 + 127)
                        )
                );
                getChildren().add(candy);
                TranslateTransition fall = new TranslateTransition(Duration.seconds(1 + random.nextDouble()), candy);
                fall.setToY(HEIGHT + 10);
                RotateTransition rotate = new RotateTransition(Duration.seconds(1 + random.nextDouble()), candy);
                rotate.setByAngle(180 + random.nextDouble() * 180);
                FadeTransition fade = new FadeTransition(Duration.seconds(1 + random.nextDouble()), candy);
                fade.setToValue(0);
                ParallelTransition candyTransition = new ParallelTransition(fall, rotate, fade);
                candyTransition.setOnFinished(e -> getChildren().remove(candy));
                candyTransition.play();
            }
        }
    }

    private void showResult(String result) {
        if (resultLabel != null) getChildren().remove(resultLabel);
        resultLabel = new Label("¡Resultado: " + result + "!");
        resultLabel.setStyle("-fx-font-family: 'Creepster'; -fx-font-size: " + (WIDTH * 0.02) + "; -fx-text-fill: " + (result.equals("TRUCO") ? "#FF0000" : "#FFFF00") + "; -fx-background-color: rgba(0,0,0,0.6); -fx-padding: 10;");
        resultLabel.setLayoutX(WIDTH * 0.4);
        resultLabel.setLayoutY(HEIGHT * 0.4);
        getChildren().add(resultLabel);
        FadeTransition fade = new FadeTransition(Duration.seconds(2), resultLabel);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> getChildren().remove(resultLabel));
        fade.play();
    }

    private class Sector {
        String label;
        Color color;
        int index;
        String result;

        Sector(String label, Color color, int index) {
            this.label = label;
            this.color = color;
            this.index = index;
        }
    }
}