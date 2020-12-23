package org.brbrt.playlistmanagerj.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.brbrt.playlistmanagerj.FxmlView;
import org.brbrt.playlistmanagerj.FxmlViewLoader;
import org.brbrt.playlistmanagerj.Player;
import org.brbrt.playlistmanagerj.event.PlaybackStartedEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@Component
@Scope(SCOPE_SINGLETON)
public class AppController implements Initializable {

    private final FxmlViewLoader fxmlViewLoader;
    private final Player player;
    private final Logger logger;

    @FXML
    private TabPane playlists;
    private Map<Tab, PlaylistController> playlistControllers = new HashMap<>();

    @Autowired
    public AppController(FxmlViewLoader fxmlViewLoader,
                         Player player,
                         Logger logger) {
        this.fxmlViewLoader = fxmlViewLoader;
        this.player = player;
        this.logger = logger;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("initialize");
        initTabs();
        initKeyboardShortcuts();
    }

    @EventListener
    void onPlaybackStarted(PlaybackStartedEvent e) {
        Platform.runLater(() -> primaryStage().setTitle(e.getMedia().getTitle() + " [playlist-manager-j]"));
    }

    private void initTabs() {
        Tab defaultPlaylist = createPlaylistTab("Default");
        playlists.getTabs().add(defaultPlaylist);

        Tab addPlaylist = new Tab();
        addPlaylist.setText("+");
        playlists.getTabs().add(addPlaylist);

        playlists.getSelectionModel().selectedItemProperty().addListener((x, y, selectedTab) -> {
            if (selectedTab == addPlaylist) {
                int index = playlists.getSelectionModel().getSelectedIndex();
                Tab playlistTab = createPlaylistTab("New tab");

                playlists.getTabs().add(index, playlistTab);
                playlists.getSelectionModel().select(playlistTab);
            }
        });
    }

    private Tab createPlaylistTab(String name) {
        Tab t = new Tab();
        t.setText(name);

        FxmlView<PlaylistController> playlistView = fxmlViewLoader.load("Playlist.fxml");
        t.setContent(playlistView.getRootNode());

        playlistControllers.put(t, playlistView.getController());

        MenuItem close = new MenuItem("Close playlist");
        close.setOnAction(e -> {
            playlists.getTabs().remove(t);
            playlistControllers.remove(t);
        });

        MenuItem rename = new MenuItem("Rename playlist");
        rename.setOnAction(e -> {
            TextInputDialog input = new TextInputDialog(t.getText());
            input.setHeaderText("Rename playlist from '" + t.getText() + "' to:");
            input.setGraphic(null);
            input.setTitle("Rename");
            input.showAndWait().ifPresent(t::setText);
        });

        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(close, rename);
        t.setContextMenu(menu);

        return t;
    }

    private void initKeyboardShortcuts() {
        playlists.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case V:
                    player.stop();
                    break;
                case C:
                    player.pauseOrResume();
                    break;
            }
        });
    }

    private Stage primaryStage() {
        return (Stage)playlists.getScene().getWindow();
    }

}
