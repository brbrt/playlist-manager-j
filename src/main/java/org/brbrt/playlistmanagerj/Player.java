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

    private Media currentMedia;

    public Player(ApplicationEventPublisher applicationEventPublisher,
                  Logger logger,
                  StreamPlayer streamPlayer) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.logger = logger;
        this.streamPlayer = streamPlayer;
    }

    public void open(Media media) {
        streamPlayer.stop();

        try {
            streamPlayer.open(media.getFile());
            currentMedia = media;
        } catch (StreamPlayerException ex) {
            logger.warn("Error while opening media: {}", media, ex);
        }
    }

    public void play() {
        try {
            streamPlayer.play();
            applicationEventPublisher.publishEvent(new PlaybackStartedEvent(currentMedia));
        } catch (StreamPlayerException ex) {
            logger.warn("Error while playing", ex);
        }
    }

    public void stop() {
        streamPlayer.stop();
    }

    public void pauseOrResume() {
        if (streamPlayer.isPaused()) {
            streamPlayer.resume();
            return;
        }
        if (streamPlayer.isPlaying()) {
            streamPlayer.pause();
        }
    }

}
