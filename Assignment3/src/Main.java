import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;
import java.util.List;

public class Main extends Application {
    HashMap<ArrayList<Integer>, String> blockAtCoordinate = new HashMap<>();
    ImageView[] currentDrillerImage = {new ImageView(new Image("assets/drill/drill_02.png"))};
    int[] drillerCoordinates = {1, 14};
    GridPane gameMapGrid = new GridPane();
    GridPane drillerGrid = new GridPane();
    long lastUpdateTime = 0;
    long elapsedTime = 0;
    long startTime = System.nanoTime();
    AnimationTimer timer;

    /*
     In this assignment we will be using layers of panes , underground pane which holds images of minerals and soil
     , driller pane which only holds drillers image and arrange it's location
     , Statistics pane which shows money, haul and fuel.
     We keep record of each blocks record with their coordinates in a hashmap for reaching them after in our code.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        setArrangements();
        StackPane gridHolderStack = new StackPane();
        drillerGrid.setPadding(new Insets(0, 2, 0, 0));
        drillerGrid.add(currentDrillerImage[0], drillerCoordinates[1], drillerCoordinates[0]);
        Label fuelLabel = new Label("Fuel:" + Driller.fuel) {{
            setFont(new Font(20));
        }};
        Label haulLabel = new Label("Haul:" + Driller.haul) {{
            setFont(new Font(20));
        }};
        Label moneyLabel = new Label("Money:" + Driller.getMoney()) {{
            setFont(new Font(20));
        }};


        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            if (i == 0 || i == 1) {
                for (int j = 0; j < 16; j++) {
                    ImageView imageView = new ImageView(new Image("assets/underground/empty_15.png")) {{
                        setFitWidth(50);
                        setFitHeight(50);
                    }};
                    gameMapGrid.add(imageView, j, i);
                    ArrayList<Integer> list = new ArrayList<>(Arrays.asList(i, j));
                    blockAtCoordinate.put(list, "empty");
                }
            } else if (i == 2) {
                for (int j = 0; j < 16; j++) {
                    ImageView imageView = new ImageView(Attributes.imageHashMap.get("top_soil"));
                    gameMapGrid.add(imageView, j, i);
                    ArrayList<Integer> list = new ArrayList<>(Arrays.asList(i, j));
                    blockAtCoordinate.put(list, "soil");
                }
            } else {
                for (int j = 0; j < 16; j++) {
                    ImageView imageView = new ImageView(Attributes.imageHashMap.get("obstacle"));
                    gameMapGrid.add(imageView, j, 15);
                    ArrayList<Integer> list = new ArrayList<>(Arrays.asList(i, j));
                    blockAtCoordinate.put(list, "obstacle");
                }
            }
        }
        for (int i = 3; i < 15; i++) {
            for (int j = 0; j < 16; j++) {
                if (j == 0 || j == 15) {
                    ImageView imageView = new ImageView(Attributes.imageHashMap.get("obstacle"));
                    gameMapGrid.add(imageView, j, i);
                    ArrayList<Integer> list = new ArrayList<>(Arrays.asList(i, j));
                    blockAtCoordinate.put(list, "obstacle");
                } else {
                    int randomInt = random.nextInt(9);
                    if (randomInt == 1) {
                        Image randomImage = Attributes.selectRandomMineralImage();
                        gameMapGrid.add(new ImageView(randomImage), j, i);
                        for (String s : Attributes.imageHashMap.keySet()) {
                            if (Attributes.imageHashMap.get(s) == randomImage) {
                                ArrayList<Integer> list = new ArrayList<>(Arrays.asList(i, j));
                                blockAtCoordinate.put(list, s);
                            }
                        }
                    } else if (randomInt == 2) {
                        gameMapGrid.add(new ImageView(Attributes.imageHashMap.get("lava")), j, i);
                        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(i, j));
                        blockAtCoordinate.put(list, "lava");
                    } else {
                        gameMapGrid.add(new ImageView(Attributes.imageHashMap.get("soil")), j, i);
                        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(i, j));
                        blockAtCoordinate.put(list, "soil");
                    }
                }
            }
        }
        //set drillerGridPane
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                ImageView imageView = new ImageView(new Image("assets/underground/empty_15.png")) {{
                    setFitWidth(50);
                    setFitHeight(50);
                }};
                drillerGrid.add(imageView, j, i);
            }
        }

        gridHolderStack.getChildren().addAll(statisticsPane(fuelLabel, haulLabel, moneyLabel, gridHolderStack), gameMapGrid, drillerGrid);
        Scene scene = new Scene(gridHolderStack, 800, 800);
        scene.setFill(Color.LIGHTSKYBLUE);
        primaryStage.setScene(scene);
        primaryStage.show();

        //movements

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                elapsedTime += (now - lastUpdateTime) / 1_000_000_000;
                if (elapsedTime >= 1) {
                    gravityFalls(gameMapGrid, drillerGrid, scene);
                    elapsedTime = 0;
                    lastUpdateTime = now;
                }
            }
        };

        scene.setOnKeyPressed(event -> {
            timer.stop();
            KeyCode keyCode = event.getCode();
            ArrayList<KeyCode> keyCodes = new ArrayList<>(Arrays.asList(KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT));
            if (keyCodes.contains(keyCode))
                moveDriller(keyCode, scene, gameMapGrid, drillerGrid);
            moneyLabel.setText("Money: " + Driller.getMoney());
            haulLabel.setText("Haul: " + Driller.haul);
            fuelLabel.setText("Fuel: " + Driller.fuel);
            if (!Driller.isAlive) {
                Label gamoverLabel = new Label("GAME OVER") {{
                    setFont(new Font(100));
                }};
                StackPane stackPane = new StackPane(gamoverLabel);
                stackPane.setStyle("-fx-background-color: red;");
                gridHolderStack.getChildren().clear();
                gridHolderStack.getChildren().add(stackPane);
            }

        });
        scene.setOnKeyReleased(event -> {
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    elapsedTime += (now - lastUpdateTime) / 1_000_000_000;
                    if (elapsedTime >= 1) {
                        gravityFalls(gameMapGrid, drillerGrid, scene);
                        elapsedTime = 0;
                        lastUpdateTime = now;
                    }
                }
            };
            timer.start();// gravity for timer starts when there is no operation is executed.
        });
    }

    /**
     * This method creates a GridPane that shows statistic like money, haul and fuel.
     *
     * @param fuelLabel fuel indicator text
     * @param haulLabel total weight indicator text
     * @param moneyLabel total money indicator text
     * @param gridHolderStack The stackpane that we hold all layers of our game
     * @return GridPane
     */
    GridPane statisticsPane(Label fuelLabel, Label haulLabel, Label moneyLabel, StackPane gridHolderStack) {
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: lightblue;");
        gridPane.add(fuelLabel, 0, 0);
        gridPane.add(haulLabel, 0, 1);
        gridPane.add(moneyLabel, 0, 2);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double elapsedTime = (now - startTime) / 1_000_000_000.0;
                if (elapsedTime >= 0.1) {
                    Driller.fuel -= 0.1f;
                    fuelLabel.setText("Fuel: " + Driller.fuel);
                    startTime = now;
                }
                if (Driller.fuel <= 0) {
                    Label label1 = new Label("GAME OVER") {{
                        setFont(new Font(100));
                    }};
                    Label label2 = new Label(String.format("Collected Money:%d", Driller.getMoney())) {{
                        setFont(new Font(50));
                    }};
                    VBox vBox = new VBox(label1, label2) {{
                        setAlignment(javafx.geometry.Pos.CENTER);
                    }};
                    StackPane stackPane = new StackPane(vBox) {{
                        setStyle("-fx-background-color: green;");
                    }};
                    gridHolderStack.getChildren().clear();
                    gridHolderStack.getChildren().add(stackPane);
                }
            }
        };
        timer.start();
        return gridPane;
    }

    /**
     * This method moves our driller according to keyCode parameter, then changes coordinates of drillerCoordinates.
     *
     * @param keyCode to know which arrow key is pressed
     * @param scene our main scene
     * @param gameMapGrid Gridpane that holds underground
     * @param drillerGrid Gridpane that arranges drillers location on screen
     */
    void moveDriller(KeyCode keyCode, Scene scene, GridPane gameMapGrid, GridPane drillerGrid) {
        if (keyCode == KeyCode.UP) {
            List<Integer> tempCoordinates = new ArrayList<Integer>(Arrays.asList(drillerCoordinates[0] - 1, drillerCoordinates[1]));
            if (blockAtCoordinate.containsKey(tempCoordinates) && blockAtCoordinate.get(tempCoordinates).equals("lava")) {
                Driller.isAlive = false;
                return;
            } else if (drillerCoordinates[0] > 0 && isMoveable(blockAtCoordinate, new ArrayList<Integer>(Arrays.asList(drillerCoordinates[0] - 1, drillerCoordinates[1])), keyCode)) {
                drillerGrid.getChildren().remove(currentDrillerImage[0]);
                drillerGrid.add(new ImageView(new Image("assets/underground/empty_15.png")) {{
                    setFitWidth(50);
                    setFitHeight(50);
                }}, drillerCoordinates[1], drillerCoordinates[0]);
                drillerCoordinates[0] -= 1;
                paintItBrown(gameMapGrid, drillerCoordinates);
                currentDrillerImage[0] = new ImageView(new Image("assets/drill/drill_24.png"));
                drillerGrid.add(currentDrillerImage[0], drillerCoordinates[1], drillerCoordinates[0]);

            }
        } else if (keyCode == KeyCode.RIGHT) {
            List<Integer> tempCoordinates = new ArrayList<Integer>(Arrays.asList(drillerCoordinates[0], drillerCoordinates[1] + 1));
            if (blockAtCoordinate.containsKey(tempCoordinates) && blockAtCoordinate.get(tempCoordinates).equals("lava")) {
                Driller.isAlive = false;
                return;
            } else if (drillerCoordinates[1] < 15 && isMoveable(blockAtCoordinate, new ArrayList<Integer>(Arrays.asList(drillerCoordinates[0], drillerCoordinates[1] + 1)), keyCode)) {
                drillerGrid.getChildren().remove(currentDrillerImage[0]);
                drillerGrid.add(new ImageView(new Image("assets/underground/empty_15.png")) {{
                                    setFitWidth(50);
                                    setFitHeight(50);
                                }}
                        , drillerCoordinates[1], drillerCoordinates[0]);
                drillerCoordinates[1] += 1;
                paintItBrown(gameMapGrid, drillerCoordinates);
                currentDrillerImage[0] = new ImageView(new Image("assets/drill/drill_right/01.png"));
                drillerGrid.add(currentDrillerImage[0], drillerCoordinates[1], drillerCoordinates[0]);
            }
        } else if (keyCode == KeyCode.LEFT) {
            List<Integer> tempCoordinates = new ArrayList<Integer>(Arrays.asList(drillerCoordinates[0], drillerCoordinates[1] - 1));
            if (blockAtCoordinate.containsKey(tempCoordinates) && blockAtCoordinate.get(tempCoordinates).equals("lava")) {
                Driller.isAlive = false;
                return;
            } else if (drillerCoordinates[1] > 0 && isMoveable(blockAtCoordinate, new ArrayList<Integer>(Arrays.asList(drillerCoordinates[0], drillerCoordinates[1] - 1)), keyCode)) {
                drillerGrid.getChildren().remove(currentDrillerImage[0]);
                drillerGrid.add(new ImageView(new Image("assets/underground/empty_15.png")) {{
                                    setFitWidth(50);
                                    setFitHeight(50);
                                }}
                        , drillerCoordinates[1], drillerCoordinates[0]);
                drillerCoordinates[1] -= 1;
                paintItBrown(gameMapGrid, drillerCoordinates);
                currentDrillerImage[0] = new ImageView(new Image("assets/drill/drill_02.png"));
                drillerGrid.add(currentDrillerImage[0], drillerCoordinates[1], drillerCoordinates[0]);
            }
        } else if (keyCode == KeyCode.DOWN) {
            List<Integer> tempCoordinates = new ArrayList<Integer>(Arrays.asList(drillerCoordinates[0] + 1, drillerCoordinates[1]));
            if (blockAtCoordinate.containsKey(tempCoordinates) && blockAtCoordinate.get(tempCoordinates).equals("lava")) {
                Driller.isAlive = false;
                return;
            } else if (drillerCoordinates[0] < 14 && isMoveable(blockAtCoordinate, new ArrayList<Integer>(Arrays.asList(drillerCoordinates[0] + 1, drillerCoordinates[1])), keyCode)) {
                drillerGrid.getChildren().remove(currentDrillerImage[0]);
                drillerGrid.add(new ImageView(new Image("assets/underground/empty_15.png")) {{
                    setFitWidth(50);
                    setFitHeight(50);
                }}, drillerCoordinates[1], drillerCoordinates[0]);
                drillerCoordinates[0] += 1;
                paintItBrown(gameMapGrid, drillerCoordinates);
                currentDrillerImage[0] = new ImageView(new Image("assets/drill/drill_43.png"));
                drillerGrid.add(currentDrillerImage[0], drillerCoordinates[1], drillerCoordinates[0]);
            }
        }
        String blockType = blockAtCoordinate.get(new ArrayList<Integer>(Arrays.asList(drillerCoordinates[0], drillerCoordinates[1])));
        if (Attributes.mineralHashMap.containsKey(blockType)) {
            Mineral mineral = Attributes.mineralHashMap.get(blockType);
            Driller.haul += mineral.weight;
            Driller.increaseMoney(mineral.value);
        }
        blockAtCoordinate.put(new ArrayList<Integer>(Arrays.asList(drillerCoordinates[0], drillerCoordinates[1])), "empty");
        Driller.fuel -= 100;
    }

    /**
     * This method is used for gravity and changes drillers location by increasing its row by one.
     *
     * @param scene our main scene
     * @param gameMapGrid Gridpane that holds underground
     * @param drillerGrid Gridpane that arranges drillers location on screen
     */
    void gravityFalls(GridPane gameMapGrid, GridPane drillerGrid, Scene scene) {
        List<Integer> location = new ArrayList<Integer>(Arrays.asList(drillerCoordinates[0] + 1, drillerCoordinates[1]));
        if (blockAtCoordinate.containsKey(location) && blockAtCoordinate.get(location).equals("empty")) {
            drillerGrid.getChildren().remove(currentDrillerImage[0]);
            drillerGrid.add(new ImageView(new Image("assets/underground/empty_15.png")) {{
                setFitWidth(50);
                setFitHeight(50);
            }}, drillerCoordinates[1], drillerCoordinates[0]);
            drillerCoordinates[0] += 1;
            currentDrillerImage[0] = new ImageView(new Image("assets/drill/drill_24.png"));
            drillerGrid.add(currentDrillerImage[0], drillerCoordinates[1], drillerCoordinates[0]);
        }

    }
    /**
     * This method shows if drillers next move is valid
     *
     * @param blockCoordinates hashmap that holds each blocks type record with their cooridnate
     * @param coordinate coordinates of drillers next move
     * @param keyCode direction of drillers next step
     * @return returns true if drillers next step is moveable,else, false
     */
    boolean isMoveable(Map blockCoordinates, ArrayList<Integer> coordinate, KeyCode keyCode) {
        if (blockCoordinates.get(coordinate).equals("obstacle"))
            return false;
        if (keyCode == KeyCode.UP && !blockCoordinates.get(coordinate).equals("empty"))
            return false;
        return true;
    }


    void paintItBrown(GridPane gridPane, int[] coordinate) {// paints brown at gridPane's given coordinates
        Rectangle rectangle = new Rectangle(50, 50, Color.BROWN);
        if (coordinate[0] > 1)
            gridPane.add(rectangle, coordinate[1], coordinate[0]);
    }

    private void setArrangements() {
        Attributes.setAttributes();
    }
}