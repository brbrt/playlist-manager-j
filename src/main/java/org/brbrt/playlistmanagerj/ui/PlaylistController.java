package org.brbrt.playlistmanagerj.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.input.*;
import org.brbrt.playlistmanagerj.Media;
import org.brbrt.playlistmanagerj.Player;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class PlaylistController implements Initializable {

    private final Logger logger;
    private final Player player;
    private final ObservableList<Media> playlist = FXCollections.observableArrayList();

    @FXML
    private TableView<Media> playlistView;

    public PlaylistController(Logger logger, Player player) {
        this.logger = logger;
        this.player = player;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("initialize");

        playlistView.setOnDragDetected(e -> {
            Dragboard dragboard = playlistView.startDragAndDrop(TransferMode.COPY);

            ClipboardContent content = new ClipboardContent();
            dragboard.setContent(content);
        });

        playlistView.setOnDragOver(e -> e.acceptTransferModes(TransferMode.COPY));

        playlistView.setOnDragDropped(e -> {
            Dragboard dragboard = e.getDragboard();

            List<Media> tracks = dragboard.getFiles()
                    .stream()
                    .map(file -> new Media().setFile(file).setTitle(file.getName()))
                    .collect(Collectors.toList());

            playlist.addAll(tracks);

            e.setDropCompleted(true);
        });

        playlistView.setItems(playlist);
        playlistView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() > 1) {
                logger.info("double clicked");
                select(playlistView.getSelectionModel().getSelectedItem());
            }
        });
        playlistView.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                logger.info("enter pressed");
                select(playlistView.getSelectionModel().getSelectedItem());
            }
        });
    }

    void select(Media media) {
        player.open(media);
        player.play();
    }

}
