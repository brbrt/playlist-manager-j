package org.brbrt.playlistmanagerj;

import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
public class PlayerController implements Initializable, DisposableBean {

    private final Logger logger;
    private final StreamPlayer streamPlayer;
    private final ObservableList<Media> playlist = FXCollections.observableArrayList();

    @FXML
    private VBox box;
    @FXML
    private Label currentSongTitle;
    @FXML
    private TableView<Media> playlistView;

    public PlayerController(Logger logger, StreamPlayer streamPlayer) {
        this.logger = logger;
        this.streamPlayer = streamPlayer;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("initialize");

        box.setOnDragDetected(e -> {
            Dragboard dragboard = box.startDragAndDrop(TransferMode.COPY);

            ClipboardContent content = new ClipboardContent();
            dragboard.setContent(content);
        });

        box.setOnDragOver(e -> e.acceptTransferModes(TransferMode.COPY));

        box.setOnDragDropped(e -> {
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
                play(playlistView.getSelectionModel().getSelectedItem());
            }
        });
        playlistView.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                logger.info("enter pressed");
                play(playlistView.getSelectionModel().getSelectedItem());
            }
        });
    }

    void play(Media media) {
        streamPlayer.stop();

        try {
            streamPlayer.open(media.getFile());
            streamPlayer.play();
        } catch (StreamPlayerException ex) {
            logger.warn("Error opening media: {}", media, ex);
        }

        currentSongTitle.setText(media.getTitle());
    }

    @Override
    public void destroy() {
        logger.info("Destroying PlayerController");
        streamPlayer.stop();
    }

}
