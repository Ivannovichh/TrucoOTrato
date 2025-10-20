package Roulette;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HelloControllerRouleta {

    @FXML
    private StackPane rouletteHolder;

    @FXML
    private Button btnSpin;

    private static final int NUM_SLICES = 7;
    private static final double RADIUS = 160;

    private final Random random = new Random();
    private Group wheel;
    private List<String> layout;

    @FXML
    public void initialize() {
        wheel = buildWheel();
        Group pointer = buildPointer();

        StackPane.setAlignment(pointer, Pos.CENTER);
        rouletteHolder.getChildren().addAll(wheel, pointer);

        btnSpin.setOnAction(e -> {
            btnSpin.setDisable(true);
            spin(() -> btnSpin.setDisable(false));
        });
    }

    private Group buildWheel() {
        Group g = new Group();
        layout = new ArrayList<>(NUM_SLICES);
        layout.addAll(List.of("TRUCO", "TRUCO", "TRUCO", "TRATO", "TRATO", "TRATO", "OSKAR"));

        double anglePerSlice = 360.0 / NUM_SLICES;

        for (int i = 0; i < NUM_SLICES; i++) {
            String type = layout.get(i);
            Color fill = switch (type) {
                case "TRUCO" -> Color.RED;
                case "TRATO" -> Color.BLACK;
                default -> Color.GREEN;
            };
            Color textColor = type.equals("TRATO") ? Color.WHITE : Color.BLACK;

            double start = i * anglePerSlice;

            Arc arc = new Arc(0, 0, RADIUS, RADIUS, start, anglePerSlice);
            arc.setType(ArcType.ROUND);
            arc.setFill(fill);
            arc.setStroke(Color.web("#222"));
            arc.setStrokeWidth(2);

            Text label = new Text(type);
            label.setFont(Font.font("Consolas", 16));
            label.setFill(textColor);
            centerTextInSlice(label, start, anglePerSlice);

            g.getChildren().addAll(arc, label);
        }

        return g;
    }

    private Group buildPointer() {
        Group p = new Group();
        double s = 16;
        Polygon triangle = new Polygon(
                0.0, -(RADIUS + 26),
                -s, -(RADIUS + 6),
                s, -(RADIUS + 6)
        );
        triangle.setFill(Color.web("#FFD700"));
        triangle.setStroke(Color.web("#333"));
        triangle.setStrokeWidth(2);
        p.getChildren().add(triangle);
        return p;
    }

    private void centerTextInSlice(Text text, double startDeg, double sweepDeg) {
        double midDeg = startDeg + sweepDeg / 2.0;
        double r = RADIUS * 0.60;
        double rad = Math.toRadians(midDeg);
        double cx = r * Math.cos(rad);
        double cy = r * Math.sin(rad);

        Runnable place = () -> {
            var b = text.getLayoutBounds();
            text.setTranslateX(cx - b.getWidth() / 2.0);
            text.setTranslateY(cy + b.getHeight() / 4.0);
        };
        text.layoutBoundsProperty().addListener((obs, o, n) -> place.run());
        Platform.runLater(place);
    }

    private void spin(Runnable onFinish) {
        String chosen = weightedPick();
        List<Integer> candidates = new ArrayList<>();
        for (int i = 0; i < layout.size(); i++)
            if (layout.get(i).equals(chosen)) candidates.add(i);

        int targetIndex = candidates.get(random.nextInt(candidates.size()));

        double anglePerSlice = 360.0 / NUM_SLICES;
        double sectorCenter = targetIndex * anglePerSlice + anglePerSlice / 2.0;

        double baseEnd = 90 - sectorCenter;
        double end = baseEnd;
        while (end <= wheel.getRotate() + 3 * 360) end += 360;

        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(wheel.rotateProperty(), wheel.getRotate(), Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(3.2),
                        new KeyValue(wheel.rotateProperty(), end, Interpolator.EASE_OUT))
        );
        tl.setOnFinished(ev -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(null);
            a.setTitle("Resultado");
            a.setContentText("¡Ha salido: " + chosen + "!");
            a.showAndWait();
            if (onFinish != null) onFinish.run();
        });
        tl.play();
    }

    private String weightedPick() {
        double r = random.nextDouble();
        if (r < (3.0 / 7)) return "TRUCO";
        if (r < (6.0 / 7)) return "TRATO";
        return "OSKAR";
    }

    public void recibirDatos(String nombre, String apellidos, String curso) {
        System.out.println("Jugador: " + nombre + " " + apellidos + " - Curso: " + curso);
    }

}
