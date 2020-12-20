package org.brbrt.playlistmanagerj;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.io.UncheckedIOException;

@SpringBootApplication
public class PlaylistManagerApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(PlaylistManagerApplication.class).run();
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(loadFxml("Playlist.fxml"), 900, 500);
        primaryStage.setTitle("playlist-manager-j");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
        System.exit(0);
    }

    public static void main(String... args) {
        Application.launch(PlaylistManagerApplication.class, args);
    }

    private Parent loadFxml(String fileName) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fileName));
        loader.setControllerFactory(applicationContext::getBean);

        try {
            return loader.load();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}