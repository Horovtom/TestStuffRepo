package com.cvut.fel.horovtom.presentation;

/**
 * Created by Hermes235 on 25.4.2017.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.annotation.Nonnull;

public class Main extends Application {
    private static final String ICON_NAME = "icon.png";
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(@Nonnull Stage primaryStage) throws Exception {
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        Parent root = FXMLLoader.load(getClass().getResource("/gui-forms/main.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream(ICON_NAME)));
        primaryStage.setTitle("Fridge");
        primaryStage.show();
    }
}
