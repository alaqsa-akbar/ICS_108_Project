package com.example.ics108_project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * This class is the main class that runs the program and the application
 * This class sets the scene and the title along with the icon and other stage controls
 */
public class GameClass extends Application {
    static Stage stage;
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        Scene menuScene = new Scene(MainMenu.mainMenuPane());
        stage.setScene(menuScene);

        //Set the title as the name of the game application
        stage.setTitle("Ap-FALL-E");

        //Set the icon of the game
        Image iconImage = new Image("AppleLogoNoBG.PNG");
        stage.getIcons().add(iconImage);

        //Change dimensions
        stage.setMaximized(true);
        stage.setFullScreen(true);

        //Show the stage
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}