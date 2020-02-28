package org.brbrt.playlistmanagerj;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayerController implements Initializable {

    @FXML
    private MediaView mediaView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("initialize PlayerController");

        play("song.mp3");
    }

    void play(String fileName) {
        URI uri = new File(fileName).toURI();
        Media pick = new Media(uri.toString());
        MediaPlayer player = new MediaPlayer(pick);

        player.play();

        mediaView.setMediaPlayer(player);
    }

}
