package org.brbrt.playlistmanagerj.ui;

import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;

@Component
public class FxmlViewLoader {

    private final ApplicationContext applicationContext;
    private final Logger logger;

    public FxmlViewLoader(ApplicationContext applicationContext, Logger logger) {
        this.applicationContext = applicationContext;
        this.logger = logger;
    }

    public <T> FxmlView<T> load(String fileName) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fileName));
        loader.setControllerFactory(applicationContext::getBean);

        try {
            return new FxmlView<>(
                    loader.load(),
                    loader.getController()
            );
        } catch (IOException ex) {
            logger.error("Loading fxml '{}' failed", fileName, ex);
            throw new UncheckedIOException(ex);
        }
    }

}
