package org.brbrt.playlistmanagerj;

import java.io.File;

public class Media {

    private File file;
    private String title;

    public Media() {}

    public Media(Media other) {
        this.file = other.file;
        this.title = other.title;
    }

    public File getFile() {
        return file;
    }

    public Media setFile(File file) {
        this.file = file;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Media setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public String toString() {
        return "Media{" + "file=" + file + '}';
    }
}
