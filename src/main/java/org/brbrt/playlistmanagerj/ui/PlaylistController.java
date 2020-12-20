package org.brbrt.playlistmanagerj.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.input.*;
import org.brbrt.playlistmanagerj.Media;
import org.brbrt.playlistmanagerj.Player;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class PlaylistController implements Initializable {

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    private final Logger logger;
    private final Player player;

    @FXML
    private TableView<Media> playlistView;

    public PlaylistController(Logger logger, Player player) {
        this.logger = logger;
        this.player = player;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("initialize");

        playlistView.setOnDragDetected(e -> {
            logger.info("Dragging external files");
            playlistView.startDragAndDrop(TransferMode.COPY);
            e.consume();
        });

        playlistView.setRowFactory(tv -> {
            TableRow<Media> row = new TableRow<>();

            row.setOnDragDetected(e -> {
                logger.info("Dragging internal items: {}", e);
                dragInternalItems(row, e);
            });

            row.setOnDragOver(e -> {
                e.acceptTransferModes(TransferMode.MOVE, TransferMode.COPY);
                e.consume();
            });

            row.setOnDragDropped(e -> {
                logger.info("Row OnDragDropped: {}", e);
                dropExternalFiles(row, e);
                dropInternalItems(row, e);
            });

            return row;
        });

        disablePlaceholderTextInPlaylist();
        handleSelection();
    }

    private void dragInternalItems(TableRow<Media> row, MouseEvent e) {
        if (row.isEmpty()) {
            return;
        }

        List<Integer> selectedIndices = new ArrayList<>(playlistView.getSelectionModel().getSelectedIndices());

        Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
        db.setDragView(row.snapshot(null, null));

        ClipboardContent cc = new ClipboardContent();
        cc.put(SERIALIZED_MIME_TYPE, selectedIndices);
        db.setContent(cc);

        e.consume();
    }

    private void dropExternalFiles(TableRow<Media> row, DragEvent e) {
        if (e.getTransferMode() != TransferMode.COPY || !e.getDragboard().hasFiles()) {
            return;
        }

        List<Media> tracks = e.getDragboard().getFiles()
                .stream()
                .map(file -> new Media().setFile(file).setTitle(file.getName()))
                .collect(Collectors.toList());

        playlistView.getItems().addAll(getDropIndex(row), tracks);

        e.setDropCompleted(true);
    }

    private void dropInternalItems(TableRow<Media> row, DragEvent e) {
        if (e.getTransferMode() != TransferMode.MOVE || !e.getDragboard().hasContent(SERIALIZED_MIME_TYPE)) {
            return;
        }

        @SuppressWarnings("unchecked")
        List<Integer> droppedIndices = (List<Integer>)e.getDragboard().getContent(SERIALIZED_MIME_TYPE);
        List<Media> droppedItems = droppedIndices.stream()
                .map(i -> playlistView.getItems().get(i))
                .collect(Collectors.toList());

        List<Media> clonedItems = droppedItems.stream()
                .map(Media::new)
                .collect(Collectors.toList());

        playlistView.getItems().addAll(getDropIndex(row), clonedItems);
        playlistView.getSelectionModel().select(getDropIndex(row));

        playlistView.getItems().removeAll(droppedItems);

        e.setDropCompleted(true);
    }

    private int getDropIndex(TableRow<Media> row) {
        if (row.isEmpty()) {
            return playlistView.getItems().size();
        }

        return row.getIndex();
    }

    private void handleSelection() {
        playlistView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() > 1) {
                logger.info("double clicked");
                select(playlistView.getSelectionModel().getSelectedItem());
            }
        });
        playlistView.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                logger.info("enter pressed");
                select(playlistView.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void select(Media media) {
        player.open(media);
        player.play();
    }

    private void disablePlaceholderTextInPlaylist() {
        playlistView.setSkin(new TableViewSkin<>(playlistView) {
            @Override
            public int getItemCount() {
                int count = super.getItemCount();
                return count == 0 ? 1 : count;
            }
        });
    }

}
