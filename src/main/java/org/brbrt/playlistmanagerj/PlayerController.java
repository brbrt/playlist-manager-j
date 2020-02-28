package org.brbrt.playlistmanagerj;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PlayerController implements Initializable {

    @FXML
    private VBox box;
    @FXML
    private MediaView mediaView;
    @FXML
    private Label currentSongTitle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
    }

    void play(File fileName) {
        System.out.println("Playing " + fileName);
        URI uri = fileName.toURI();
        Media pick = new Media(uri.toString());
        MediaPlayer player = new MediaPlayer(pick);

        player.play();

        mediaView.setMediaPlayer(player);
        currentSongTitle.setText(fileName.getName());
    }

}
