package org.brbrt.playlistmanagerj;

import javafx.scene.Parent;

public class FxmlView<T> {

    private final Parent rootNode;
    private final T controller;

    public FxmlView(Parent rootNode, T controller) {
        this.rootNode = rootNode;
        this.controller = controller;
    }

    public Parent getRootNode() {
        return rootNode;
    }

    public T getController() {
        return controller;
    }

}
