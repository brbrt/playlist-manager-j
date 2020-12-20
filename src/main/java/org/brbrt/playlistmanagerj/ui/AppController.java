package org.brbrt.playlistmanagerj.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.brbrt.playlistmanagerj.FxmlLoader;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class AppController implements Initializable {

    private final FxmlLoader fxmlLoader;
    private final Logger logger;

    @FXML
    private TabPane playlists;

    @Autowired
    public AppController(FxmlLoader fxmlLoader, Logger logger) {
        this.fxmlLoader = fxmlLoader;
        this.logger = logger;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("initialize");

        Tab defaultPlaylist = createPlaylistTab("Default");
        playlists.getTabs().add(defaultPlaylist);

        Tab addPlaylist = new Tab();
        addPlaylist.setText("+");
        playlists.getTabs().add(addPlaylist);

        playlists.getSelectionModel().selectedItemProperty().addListener((x, y, selectedTab) -> {
            if (selectedTab == addPlaylist) {
                int index = playlists.getSelectionModel().getSelectedIndex();
                Tab playlistTab = createPlaylistTab(String.valueOf(index));

                playlists.getTabs().add(index, playlistTab);
                playlists.getSelectionModel().select(playlistTab);
            }
        });
    }

    private Tab createPlaylistTab(String name) {
        Tab t = new Tab();
        t.setText(name);
        t.setContent(fxmlLoader.load("Playlist.fxml"));
        return t;
    }

}
