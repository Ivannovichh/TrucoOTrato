package Roulette;

import javafx.animation.*;
import javafx.application.Platform;
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
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WheelView extends Pane {
    private static final int SECTORS = 6; // Number of sectors in the roulette wheel
    private final double RADIUS; // Radius of the wheel
    private final double WIDTH; // Width of the screen
    private final double HEIGHT; // Height of the screen
    private Canvas wheelCanvas; // Canvas for drawing the wheel
    private Button spinButton; // Button to spin the wheel
    private double currentAngle = 0; // Current rotation angle of the wheel
    private List<Sector> sectors = new ArrayList<>(); // List of wheel sectors
    private Random random = new Random(); // Random number generator
    private MediaPlayer spinPlayer; // Media player for spin sound
    private Image backgroundImg; // Background image
    private ImageView backgroundView; // ImageView for background
    private Polygon indicator; // Indicator triangle pointing to the wheel
    private Label welcomeLabel; // Label for welcome message
    private Label resultLabel; // Label for displaying spin result
    private MediaPlayer screamPlayer; // Media player for scream sound
    private int candyCount = 0; // Counter for candies collected
    private Label candyLabel; // Label displaying candy count

    // Constructor: Initializes the wheel view, sets up dimensions, and adds UI elements
    public WheelView() {
        // Get screen dimensions
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        WIDTH = screenBounds.getWidth(); // Set screen width
        HEIGHT = screenBounds.getHeight(); // Set screen height
        RADIUS = Math.min(WIDTH, HEIGHT) * 0.35; // Calculate wheel radius based on screen size

        // Load and set up background image
        backgroundImg = new Image(getClass().getResourceAsStream("/Inteface/background.jpg"));
        backgroundView = new ImageView(backgroundImg);
        backgroundView.setFitWidth(WIDTH); // Fit background to screen width
        backgroundView.setFitHeight(HEIGHT); // Fit background to screen height

        // Initialize canvas for drawing the wheel
        wheelCanvas = new Canvas(WIDTH, HEIGHT);

        // Initialize media resources
        initResources();
        // Initialize wheel sectors
        initSectors();
        // Draw the wheel on the canvas
        drawWheel();
        // Set up the indicator triangle
        setupIndicator();
        // Set up the spin button
        setupButtons();
        // Initialize candy count label
        candyLabel = new Label("Caramelos recogidos: 0");
        candyLabel.setStyle("-fx-font-family: 'Creepster'; -fx-font-size: " + (WIDTH * 0.02) +
                "; -fx-text-fill: red; -fx-background-color: black; -fx-padding: 10; -fx-border-color: white; -fx-border-width: 2;");
        candyLabel.setLayoutX(WIDTH * 0.78); // Position candy label to the right of the wheel
        candyLabel.setLayoutY(HEIGHT * 0.3); // Position candy label vertically

        // Add all UI elements to the pane
        getChildren().addAll(backgroundView, wheelCanvas, spinButton, indicator, candyLabel);
    }

    // Displays a welcome message with user details
    public void open(String nombre, String apellidos, String curso) {
        // Create and style welcome label
        welcomeLabel = new Label("¡Bienvenid@, " + nombre + " " + apellidos + " del curso " + curso + "!");
        welcomeLabel.setStyle("-fx-font-family: 'Creepster'; -fx-font-size: " + (WIDTH * 0.015) + "; -fx-text-fill: #FF0000; -fx-background-color: rgba(0,0,0,0.6); -fx-padding: 10; -fx-effect: dropshadow(gaussian, #FFFFFF, 10, 0.5, 0, 0);");
        welcomeLabel.setLayoutY(HEIGHT * 0.02); // Position label near the top
        // Add welcome label to the pane
        getChildren().add(welcomeLabel);

        // Center the label horizontally after rendering
        Platform.runLater(() -> {
            welcomeLabel.setLayoutX((WIDTH - welcomeLabel.getWidth()) / 2);
        });
    }

    // Initializes media resources (spin sound)
    private void initResources() {
        // Load and initialize spin sound
        spinPlayer = new MediaPlayer(new Media(getClass().getResource("/Inteface/spin.mp3").toString()));
    }

    // Initializes the wheel sectors with labels and colors
    private void initSectors() {
        // Define sector labels (TRUCO or TRATO)
        String[] labels = {"TRUCO", "TRATO", "TRUCO", "TRATO", "TRUCO", "TRATO"};
        // Define sector colors (black or red)
        Color[] colors = {Color.BLACK, Color.RED, Color.BLACK, Color.RED, Color.BLACK, Color.RED};
        // Create sectors and add to the list
        for (int i = 0; i < SECTORS; i++) {
            sectors.add(new Sector(labels[i], colors[i], i));
        }
    }

    // Draws the roulette wheel on the canvas
    private void drawWheel() {
        // Get graphics context of the canvas
        GraphicsContext gc = wheelCanvas.getGraphicsContext2D();
        // Clear the canvas
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        // Calculate center of the canvas
        double centerX = WIDTH / 2, centerY = HEIGHT / 2;
        // Calculate angle per sector
        double angleStep = 360.0 / SECTORS;

        // Save graphics context state
        gc.save();
        // Translate to center of the canvas
        gc.translate(centerX, centerY);
        // Rotate canvas based on current angle
        gc.rotate(currentAngle);

        // Draw each sector
        for (int i = 0; i < SECTORS; i++) {
            // Set fill color for the sector
            gc.setFill(sectors.get(i).color);
            // Draw sector arc
            gc.fillArc(-RADIUS, -RADIUS, 2 * RADIUS, 2 * RADIUS, i * angleStep, angleStep, javafx.scene.shape.ArcType.ROUND);

            // Set stroke color and width for sector borders
            gc.setStroke(Color.SILVER);
            gc.setLineWidth(2);
            // Draw sector border arc
            gc.strokeArc(-RADIUS, -RADIUS, 2 * RADIUS, 2 * RADIUS, i * angleStep, angleStep, javafx.scene.shape.ArcType.ROUND);
        }

        // Set text properties for sector labels
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Creepster", WIDTH * 0.015));
        gc.setEffect(new DropShadow(5, Color.BLACK));

        // Draw text labels for each sector
        for (int i = 0; i < SECTORS; i++) {
            // Calculate middle angle of the sector
            double midAngle = i * angleStep + angleStep;
            // Convert angle to radians
            double midAngleRad = Math.toRadians(midAngle);

            // Calculate text position closer to the center
            double textRadius = RADIUS * 0.35;
            double textX = Math.sin(midAngleRad) * textRadius;
            double textY = -Math.cos(midAngleRad) * textRadius;

            // Get sector label
            String label = sectors.get(i).label;
            // Create temporary text node to measure size
            Text textNode = new Text(label);
            textNode.setFont(gc.getFont());
            double textWidth = textNode.getLayoutBounds().getWidth();
            double textHeight = textNode.getLayoutBounds().getHeight();

            // Save context, translate, and draw centered text
            gc.save();
            gc.translate(textX, textY);
            gc.fillText(label, -textWidth / 2, textHeight / 4);
            gc.restore();
        }

        // Restore graphics context state
        gc.restore();
        // Clear any effects
        gc.setEffect(null);
    }

    // Sets up the indicator triangle pointing to the wheel
    private void setupIndicator() {
        // Create polygon for the indicator
        indicator = new Polygon();
        // Calculate center of the canvas
        double centerX = WIDTH / 2, centerY = HEIGHT / 2;
        // Define triangle points for the indicator
        indicator.getPoints().addAll(centerX - RADIUS - 20, centerY - 15, centerX - RADIUS + 10, centerY, centerX - RADIUS - 20, centerY + 15);
        // Set indicator fill and stroke
        indicator.setFill(Color.RED);
        indicator.setStroke(Color.BLACK);
        indicator.setStrokeWidth(3);
        // Add a decorative circle (blood effect) near the indicator
        Circle blood = new Circle(centerX - RADIUS + 5, centerY, 5, Color.DARKRED);
        // Add blood circle to the pane
        getChildren().add(blood);
    }

    // Sets up the spin button
    private void setupButtons() {
        // Create and style the spin button
        spinButton = new Button("¡GIRAR RULETA!");
        spinButton.setStyle("-fx-font-family: 'Creepster'; -fx-font-size: " + (WIDTH * 0.015) + "; -fx-background-color: #000000; -fx-text-fill: #FF0000; -fx-padding: 12 24; -fx-effect: dropshadow(gaussian, #FFFFFF, 10, 0.5, 0, 0);");
        // Position the button
        spinButton.setLayoutX(WIDTH * 0.45);
        spinButton.setLayoutY(HEIGHT * 0.92);
        // Set action to spin the wheel
        spinButton.setOnAction(e -> spinWheel());
    }

    // Animates the wheel spin and determines the result
    private void spinWheel() {
        // Disable the spin button during animation
        spinButton.setDisable(true);
        // Stop and replay spin sound
        spinPlayer.stop();
        spinPlayer.play();

        // Calculate total sectors and angle per sector
        int totalReto = SECTORS;
        double sliceAngle = 360.0 / totalReto;
        // Generate random stopping angle
        double randomAngle = Math.random() * 360;
        // Calculate number of full spins
        double vueltas = 5 + Math.random() * 2;
        // Calculate start and end angles for animation
        double startAngle = currentAngle;
        double endAngle = currentAngle + vueltas * 360 + randomAngle;

        // Set up animation timeline
        int frames = 120;
        Timeline timeline = new Timeline();

        // Create keyframes for smooth rotation
        for (int i = 0; i <= frames; i++) {
            double t = i / (double) frames;
            // Apply easing function for natural slowdown
            double easedT = 1 - Math.pow(1 - t, 3);
            double angle = startAngle + (endAngle - startAngle) * easedT;

            // Add keyframe to update wheel rotation
            KeyFrame kf = new KeyFrame(Duration.seconds(i * 0.025), e -> {
                currentAngle = angle;
                drawWheel();
            });
            timeline.getKeyFrames().add(kf);
        }

        // Handle animation completion
        timeline.setOnFinished(e -> {
            // Normalize final angle
            currentAngle = endAngle % 360;
            // Calculate winning sector
            int resultIndex = (int) ((360 - currentAngle) / sliceAngle) % totalReto;
            Sector winner = sectors.get(resultIndex);
            // Set result based on sector color
            winner.result = (winner.color == Color.RED) ? "TRATO" : "TRUCO";

            // Pause before showing result
            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            pause.setOnFinished(ev -> {
                // Execute visual and sound effects
                executeEffect(winner);
                // Display the result
                showResult(winner.result);
                // Re-enable the spin button
                spinButton.setDisable(false);
                // Stop spin sound
                spinPlayer.stop();
            });
            pause.play();
        });

        // Start the animation
        timeline.play();
    }

    // Executes effects based on the spin result (TRUCO or TRATO)
    private void executeEffect(Sector winner) {
        // Calculate center of the canvas
        double centerX = WIDTH / 2, centerY = HEIGHT / 2;

        // Handle TRUCO result
        if ("TRUCO".equals(winner.result)) {
            // Create confetti effect
            for (int i = 0; i < 20; i++) {
                // Create confetti circle with random position and color
                Circle confetti = new Circle(centerX + random.nextDouble() * WIDTH * 0.4 - WIDTH * 0.2,
                        -10, 5 + random.nextDouble() * 5,
                        Color.rgb((int) (random.nextDouble() * 255),
                                (int) (random.nextDouble() * 255),
                                (int) (random.nextDouble() * 255), 0.8));
                // Add confetti to the pane
                getChildren().add(confetti);

                // Animate confetti falling
                TranslateTransition fall = new TranslateTransition(Duration.seconds(1 + random.nextDouble()), confetti);
                fall.setToY(HEIGHT + 10);
                // Animate confetti rotation
                RotateTransition rotate = new RotateTransition(Duration.seconds(1 + random.nextDouble()), confetti);
                rotate.setByAngle(360 + random.nextDouble() * 360);
                // Animate confetti fading
                FadeTransition fade = new FadeTransition(Duration.seconds(1 + random.nextDouble()), confetti);
                fade.setToValue(0);

                // Combine animations
                ParallelTransition confettiTransition = new ParallelTransition(fall, rotate, fade);
                // Remove confetti after animation
                confettiTransition.setOnFinished(e -> getChildren().remove(confetti));
                confettiTransition.play();
            }

            // Load and display scream image
            Image screamImg = new Image(getClass().getResourceAsStream("/Inteface/scream.png"));
            ImageView screamView = new ImageView(screamImg);

            // Adjust size and position of scream image
            screamView.setFitHeight(HEIGHT);
            screamView.setPreserveRatio(true);
            screamView.setLayoutX(WIDTH / 4 - screamView.getFitWidth() / 2);
            screamView.setLayoutY(HEIGHT / 2 - screamView.getFitHeight() / 2);
            // Add scream image to the pane
            getChildren().add(screamView);

            // Play scream sound
            if (screamPlayer != null) screamPlayer.stop();
            screamPlayer = new MediaPlayer(new Media(getClass().getResource("/Inteface/scream.mp3").toString()));
            screamPlayer.play();

            // Animate scream image fading
            FadeTransition fadeScream = new FadeTransition(Duration.seconds(2.5), screamView);
            fadeScream.setFromValue(1.0);
            fadeScream.setToValue(0.0);
            // Remove scream image after fading
            fadeScream.setOnFinished(e -> getChildren().remove(screamView));
            fadeScream.play();

            // Handle TRATO result
        } else if ("TRATO".equals(winner.result)) {
            // Increment candy count
            candyCount += 1;
            // Update candy label
            candyLabel.setText("Caramelos recogidos: " + candyCount);

            // Create candy effect
            for (int i = 0; i < 10; i++) {
                // Create candy circle with random position and color
                Circle candy = new Circle(centerX + random.nextDouble() * WIDTH * 0.4 - WIDTH * 0.2,
                        -10, 10 + random.nextDouble() * 5,
                        Color.rgb((int) (random.nextDouble() * 128 + 127),
                                (int) (random.nextDouble() * 128 + 127),
                                (int) (random.nextDouble() * 128 + 127)));
                // Add candy to the pane
                getChildren().add(candy);

                // Animate candy falling
                TranslateTransition fall = new TranslateTransition(Duration.seconds(1 + random.nextDouble()), candy);
                fall.setToY(HEIGHT + 10);
                // Animate candy rotation
                RotateTransition rotate = new RotateTransition(Duration.seconds(1 + random.nextDouble()), candy);
                rotate.setByAngle(180 + random.nextDouble() * 180);
                // Animate candy fading
                FadeTransition fade = new FadeTransition(Duration.seconds(1 + random.nextDouble()), candy);
                fade.setToValue(0);
                // Combine animations
                ParallelTransition candyTransition = new ParallelTransition(fall, rotate, fade);
                // Remove candy after animation
                candyTransition.setOnFinished(e -> getChildren().remove(candy));
                candyTransition.play();
            }
        }
    }

    // Displays the spin result with a fading label
    private void showResult(String result) {
        // Check if result is valid
        if (result == null) return;
        // Remove previous result label if it exists
        if (resultLabel != null) getChildren().remove(resultLabel);
        // Create and style result label
        resultLabel = new Label("¡Resultado: " + result + "!");
        resultLabel.setStyle("-fx-font-family: 'Creepster'; -fx-font-size: " + (WIDTH * 0.02) + "; -fx-text-fill: " + (result.equals("TRUCO") ? "#FF0000" : "#FFFF00") + "; -fx-background-color: rgba(0,0,0,0.6); -fx-padding: 10;");
        // Position the result label
        resultLabel.setLayoutX(WIDTH * 0.4);
        resultLabel.setLayoutY(HEIGHT * 0.4);
        // Add result label to the pane
        getChildren().add(resultLabel);

        // Animate result label fading
        FadeTransition fade = new FadeTransition(Duration.seconds(2), resultLabel);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        // Remove result label after fading
        fade.setOnFinished(e -> getChildren().remove(resultLabel));
        fade.play();
    }

    // Inner class representing a wheel sector
    private class Sector {
        String label; // Sector label (TRUCO or TRATO)
        Color color; // Sector color
        int index; // Sector index
        String result; // Spin result for the sector

        // Constructor: Initializes a sector with label, color, and index
        Sector(String label, Color color, int index) {
            this.label = label;
            this.color = color;
            this.index = index;
        }
    }
}