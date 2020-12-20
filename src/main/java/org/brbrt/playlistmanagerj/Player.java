package org.brbrt.playlistmanagerj;

import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;
import org.brbrt.playlistmanagerj.event.PlaybackStartedEvent;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class Player {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final Logger logger;
    private final StreamPlayer streamPlayer;

    public Player(ApplicationEventPublisher applicationEventPublisher,
                  Logger logger,
                  StreamPlayer streamPlayer) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.logger = logger;
        this.streamPlayer = streamPlayer;
    }

    public void play(Media media) {
        streamPlayer.stop();

        try {
            streamPlayer.open(media.getFile());
            streamPlayer.play();

            applicationEventPublisher.publishEvent(new PlaybackStartedEvent(media));
        } catch (StreamPlayerException ex) {
            logger.warn("Error opening media: {}", media, ex);
        }
    }

    public void stop() {
        streamPlayer.stop();
    }

}
