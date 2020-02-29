package org.brbrt.playlistmanagerj;

import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
            List<File> files = dragboard.getFiles();
            play(files.get(0));
            e.setDropCompleted(true);
        });

        playlistView.setItems(playlist);
    }

    void play(File file) {
        streamPlayer.stop();

        try {
            streamPlayer.open(file);
            streamPlayer.play();
        } catch (StreamPlayerException e) {
            e.printStackTrace();
        }

        Media media = new Media().setFile(file).setTitle(file.getName());

        playlist.add(media);
        currentSongTitle.setText(media.getTitle());
    }

    @Override
    public void destroy() {
        logger.info("Destroy");
        streamPlayer.stop();
    }

}
