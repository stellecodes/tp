package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;


/**
 * Panel containing the list of unique tags.
 */
public class TagListPanel extends UiPart<Region> {
    private static final String FXML = "TagListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TagListPanel.class);

    @FXML
    private ListView<Tag> tagListView;

    /**
     * Creates a {@code TagListPanel} with the given {@code UniqueTagList}.
     */
    public TagListPanel(UniqueTagList uniqueTagList) {
        super(FXML);
        Set<Tag> tagSet = uniqueTagList.getTags();
        List<Tag> sortedTags = new ArrayList<>(tagSet);

        // Wrap as observable and show
        ObservableList<Tag> observableTags = FXCollections.observableArrayList(sortedTags);
        tagListView.setItems(observableTags);

        tagListView.setCellFactory(listView -> new javafx.scene.control.ListCell<>() {
            private final Label label = new Label();

            {
                // Render only the label, not the cell's internal text node
                setContentDisplay(javafx.scene.control.ContentDisplay.GRAPHIC_ONLY);

                // Let the cell itself grow to the list's width
                prefWidthProperty().bind(listView.widthProperty().subtract(12));
                setMaxWidth(javafx.scene.control.Control.USE_PREF_SIZE);

                // Make the label wrap
                label.setWrapText(true);
                label.setMaxWidth(Double.MAX_VALUE);
                label.maxWidthProperty().bind(listView.widthProperty().subtract(24)); // try 20–28 if needed
            }

            @Override
            protected void updateItem(Tag tag, boolean empty) {
                super.updateItem(tag, empty);
                if (empty || tag == null) {
                    setGraphic(null);
                } else {
                    label.setText(tag.tagName);  // display tag name only
                    setGraphic(label);
                }
            }
        });
    }

    /**
     * Updates tagListView with changes to uniqueTagList if any
     */
    public void updateTags(UniqueTagList uniqueTagList) {
        tagListView.setItems(
                FXCollections.observableArrayList(uniqueTagList.getTags())
        );
    }
}
