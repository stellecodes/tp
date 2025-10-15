package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.model.person.Person;
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
            @Override
            protected void updateItem(Tag tag, boolean empty) {
                super.updateItem(tag, empty);
                if (empty || tag == null) {
                    setText(null);
                } else {
                    setText(tag.tagName);   // Removes the square brackets from display
                }
            }
        });
    }
}
