package org.brbrt.playlistmanagerj;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.brbrt.playlistmanagerj.ui.FxmlViewLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class PlaylistManagerApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(PlaylistManagerApplication.class).run();
    }

    @Override
    public void start(Stage primaryStage) {
        FxmlViewLoader fxmlViewLoader = applicationContext.getBean(FxmlViewLoader.class);

        Scene scene = new Scene(fxmlViewLoader.load("App.fxml").getRootNode(), 1000, 500);
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

}