package seedu.address.logic.commands;

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
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

class AddTagsCommandTest {

    private ModelStub modelStub;
    private Tag tagMath;
    private Tag tagScience;

    @BeforeEach
    void setUp() {
        modelStub = new ModelStub();
        tagMath = new Tag("Math");
        tagScience = new Tag("Science");
    }

    @Test
    void execute_addTags_success() throws Exception {
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(tagMath);
        tagsToAdd.add(tagScience);

        AddTagsCommand command = new AddTagsCommand(tagsToAdd);
        command.execute(modelStub);

        assertEquals("Tags added successfully!", result.getFeedbackToUser());
        assertTrue(modelStub.addedTags.contains(tagMath));
        assertTrue(modelStub.addedTags.contains(tagScience));
    }

    @Test
    void execute_nullModel_throwsNullPointerException() {
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(tagMath);

        AddTagsCommand command = new AddTagsCommand(tagsToAdd);
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    void constructor_nullTags_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddTagsCommand(null));
    }

    @Test
    void toString_correctFormat() {
        Set<Tag> tags = Set.of(tagMath, tagScience);
        String expected = new AddTagsCommand(tags).toString();

        assertTrue(expected.contains("toAdd"));
        assertTrue(expected.contains("Science"));
        assertTrue(expected.contains("Math"));
    }

    /**
     * Simple stub for Model that tracks added tags.
     */
    private static class ModelStub implements Model {
        final Set<Tag> addedTags = new HashSet<>();

        @Override
        public void addTagTypes(Set<Tag> tags) {
            addedTags.addAll(tags);
        }

        @Override
        public void deleteTagTypes(Set<Tag> tagsDelete) {

        }

        // --- unused methods throw AssertionError to catch unexpected calls ---
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

        @Override
        public boolean hasTag(Tag tag) {
            return false;
        }

        // You can safely omit the rest of the Model methods if not needed for this test.
    }
}
