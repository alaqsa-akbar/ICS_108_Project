package com.example.ics108_project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * The main class to handle gameplay elements.
 */
public class GameApp {
    public static ArrayList<FallingEntity> apples = new ArrayList<>();
    public static Pane pane = new Pane();
    public static Timeline timeline;
    private static final double width = Screen.getPrimary().getBounds().getWidth();
    private static final double height = Screen.getPrimary().getBounds().getHeight();
    private static final long initialGenerationSpeed = 2;
    private static final double initialFallSpeed = 10;
    private static final int pointsPerApple = 5;
    private static final double fallAcceleration = 1.005;
    private static final double generationAcceleration = 1.03;
    private static double fallSpeed = initialFallSpeed;
    private static Rectangle floor;
    private static Rectangle opacityRectangle;
    private static Label scoreLabel;
    private static Button resetButton;

    /**
     * Method to get the game's main scene. The scene contains a pane with a floor.
     * Apples are added to the scene as the game progresses. The gameplay is not
     * initiated until {@code initiate()} is called.
     * @return the main {@code Scene} for the game
     */
    public static Scene gameScene() {
        floor = new Rectangle();
        floor.setWidth(width);
        floor.setHeight(100);
        floor.setY(height);
        floor.setX(0);
        floor.setFill(Color.TRANSPARENT);

        pane.getChildren().add(floor);

        Image image = new Image("Background.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(1.0,1.0,true,true,false,false);
        BackgroundImage backgroundImage = new BackgroundImage(
                image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        pane.setBackground(background);

        ImageView backGroundMusic = MainMenu.backGroundMusic();
        backGroundMusic.setLayoutX(width);
        backGroundMusic.setLayoutY(height);
        pane.getChildren().add(backGroundMusic);

        return new Scene(pane);
    }

    /**
     * Method to initiate the game. A {@code Timeline} is created for dropping
     * apples.
     */
    public static void initiate() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(initialGenerationSpeed), event -> {
            addApple();
            timeline.setRate(timeline.getRate() * generationAcceleration);
            fallSpeed *= fallAcceleration;
        }));
        addApple();

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * A method that stops all instances of apples using
     * {@code FallingEntity.stopProcesses()}
     */
    public static void stopAll() {
        for (FallingEntity apple: apples) {
            apple.stopProcesses();
        }
    }

    /**
     * Method that renders the game over screen. It provides a label of the final
     * score and an option to reset and play again.
     */
    public static void gameOver() {
        opacityRectangle = new Rectangle();
        opacityRectangle.setFill(Color.BLACK);
        opacityRectangle.setOpacity(0.5);
        opacityRectangle.setHeight(height);
        opacityRectangle.setWidth(width);
        opacityRectangle.setX(0);
        opacityRectangle.setY(0);
        pane.getChildren().add(opacityRectangle);

        scoreLabel = new Label("Score: " + Player.getScore());
        scoreLabel.setFont(Font.font(40));
        scoreLabel.layoutXProperty().bind(pane.widthProperty().subtract(scoreLabel.widthProperty()).divide(2));
        scoreLabel.layoutYProperty().bind(pane.heightProperty().subtract(scoreLabel.heightProperty()).divide(2));
        pane.getChildren().add(scoreLabel);

        resetButton = new Button("Reset");
        resetButton.setOnAction(e -> {
            pane.getChildren().remove(opacityRectangle);
            opacityRectangle = null;
            pane.getChildren().remove(scoreLabel);
            scoreLabel = null;
            pane.getChildren().remove(resetButton);
            resetButton = null;
            clear();
            initiate();
        });
        pane.getChildren().add(resetButton);
    }

    /**
     * Method to clear all elements on screen. Main purpose is to call when player
     * chooses to play again at the game over screen.
     */
    private static void clear() {
        while (apples.size() != 0) {
            apples.get(0).kill();
        }
        fallSpeed = initialFallSpeed;
    }

    /**
     * A private method to add an apple to the {@code ArrayList} of apples
     * and drop it using {@code FallingEntity.fall()}
     */
    private static void addApple() {
        apples.add(new FallingEntity(pointsPerApple, fallSpeed));
        apples.get(apples.size() - 1).setPosition((int)(Math.random() * width), -250);
        //System.out.println(apples.get(apples.size() - 1).getX());
        pane.getChildren().add(apples.get(apples.size() - 1));
        apples.get(apples.size() - 1).fall(floor);
    }
}