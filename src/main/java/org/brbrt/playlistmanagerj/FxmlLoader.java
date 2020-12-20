package org.brbrt.playlistmanagerj;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;

@Component
public class FxmlLoader {

    private final ApplicationContext applicationContext;
    private final Logger logger;

    @Autowired
    public FxmlLoader(ApplicationContext applicationContext, Logger logger) {
        this.applicationContext = applicationContext;
        this.logger = logger;
    }

    public Parent load(String fileName) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fileName));
        loader.setControllerFactory(applicationContext::getBean);

        try {
            return loader.load();
        } catch (IOException ex) {
            logger.error("Loading fxml '{}' failed", fileName, ex);
            throw new UncheckedIOException(ex);
        }
    }

}
