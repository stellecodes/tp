package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

class DeleteTagsCommandTest {

    private ModelStub modelStub;
    private Tag tagFriend;
    private Tag tagOwesMoney;

    @BeforeEach
    void setUp() {
        modelStub = new ModelStub();
        tagFriend = new Tag("friends");
        tagOwesMoney = new Tag("owesMoney");
        modelStub.addedTags.add(tagFriend);
        modelStub.addedTags.add(tagOwesMoney);
    }

    @Test
    void execute_deleteExistingTags_success() throws Exception {
        Set<Tag> tagsToDelete = Set.of(tagFriend);
        DeleteTagsCommand command = new DeleteTagsCommand(tagsToDelete);

        CommandResult result = command.execute(modelStub);

        assertEquals("Tags deleted successfully!", result.getFeedbackToUser());
        assertTrue(!modelStub.addedTags.contains(tagFriend));
        assertTrue(modelStub.addedTags.contains(tagOwesMoney)); // untouched
    }

    @Test
    void execute_deleteNonexistentTag_throwsCommandException() {
        Tag nonExistentTag = new Tag("nonexistent");
        Set<Tag> tagsToDelete = Set.of(nonExistentTag);
        DeleteTagsCommand command = new DeleteTagsCommand(tagsToDelete);

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(modelStub));
        assertEquals("Tag " + nonExistentTag + " does not exist", exception.getMessage());
    }

    @Test
    void toString_containsTagNames() {
        Set<Tag> tags = Set.of(tagFriend);
        DeleteTagsCommand command = new DeleteTagsCommand(tags);
        String result = command.toString();

        assertTrue(result.contains("toDelete"));
        assertTrue(result.contains("friends"));
    }

    /**
     * Minimal stub for Model to simulate tag storage.
     */
    private static class ModelStub implements Model {
        final Set<Tag> addedTags = new HashSet<>();

        @Override
        public boolean hasTag(Tag tag) {
            return addedTags.contains(tag);
        }

        @Override
        public void deleteTagTypes(Set<Tag> tags) {
            addedTags.removeAll(tags);
        }

        // --- unused methods throw AssertionError to catch unintended calls ---
        @Override
        public void addTagTypes(Set<Tag> tags) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUserPrefs(seedu.address.model.ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public seedu.address.model.ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(seedu.address.commons.core.GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            return null;
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {

        }

        @Override
        public seedu.address.commons.core.GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(seedu.address.model.ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public seedu.address.model.ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            return false;
        }

        @Override
        public void deletePerson(Person target) {

        }

        @Override
        public void addPerson(Person person) {

        }

        @Override
        public void setPerson(Person target, Person editedPerson) {

        }

        @Override
        public Set<Tag> getTags() {
            return Set.of();
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {

        }

        @Override
        public boolean link(Person a, Person b) {
            return false;
        }

        @Override
        public boolean unlink(Person a, Person b) {
            return false;
        }

        @Override
        public List<Person> getLinkedPersons(Person person) {
            return List.of();
        }

        @Override
        public boolean personHasValidTags(Person p) {
            return false;
        }
    }
}
