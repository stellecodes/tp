package seedu.address.model;

import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    UniqueTagList getTagList();

    boolean personHasValidTags(Person person);

    boolean hasTag(Tag tag);

    void addTagTypes(Set<Tag> tags);

    void deleteTagTypes(Set<Tag> tags);

    Set<Tag> getTags();
}
