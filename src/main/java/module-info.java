module playlist.manager.j {
    requires spring.core;
    requires spring.context;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.starter;
    requires spring.boot.autoconfigure;

    requires javafx.controls;
    requires javafx.fxml;

    requires org.slf4j;
    requires com.goxr3plus.streamplayer;

    exports org.brbrt.playlistmanagerj;
    opens org.brbrt.playlistmanagerj;
}