package org.brbrt.playlistmanagerj;

import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PlayerController implements Initializable {

    @FXML
    private VBox box;
    @FXML
    private Label currentSongTitle;

    private StreamPlayer streamPlayer = new StreamPlayer();

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

    void play(File file) {
        streamPlayer.stop();

        try {
            streamPlayer.open(file);
            streamPlayer.play();
        } catch (StreamPlayerException e) {
            e.printStackTrace();
        }

        currentSongTitle.setText(file.getName());
    }

}
