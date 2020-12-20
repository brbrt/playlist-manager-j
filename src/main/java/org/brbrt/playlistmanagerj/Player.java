package org.brbrt.playlistmanagerj;

import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Player {

    private final Logger logger;
    private final StreamPlayer streamPlayer;

    public Player(Logger logger, StreamPlayer streamPlayer) {
        this.logger = logger;
        this.streamPlayer = streamPlayer;
    }

    public  void play(Media media) {
        streamPlayer.stop();

        try {
            streamPlayer.open(media.getFile());
            streamPlayer.play();
        } catch (StreamPlayerException ex) {
            logger.warn("Error opening media: {}", media, ex);
        }

        // TODO: display currently played song
    }

    public void stop() {
        streamPlayer.stop();
    }

}
