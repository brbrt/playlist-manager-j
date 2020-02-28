package org.brbrt.playlistmanagerj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(loadFxml("Player.fxml"),900,500);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setTitle("playlist-manager-j");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String... args) {
        launch(args);
    }

    private Parent loadFxml(String fileName) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fileName));

        try {
            return loader.load();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}