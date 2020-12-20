package org.brbrt.playlistmanagerj.event;

import org.brbrt.playlistmanagerj.Media;

public class PlaybackStartedEvent {

    private final Media media;

    public PlaybackStartedEvent(Media media) {
        this.media = media;
    }

    public Media getMedia() {
        return media;
    }

}
