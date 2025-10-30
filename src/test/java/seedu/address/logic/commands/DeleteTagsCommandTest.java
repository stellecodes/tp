package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.DeleteTagsCommand.MESSAGE_NON_EXISTENT_TAG;

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

/**
 * Contains integration tests (execution) and unit tests (equality) for {@code DeleteTagsCommand}
 * using a custom Model Stub.
 */
class DeleteTagsCommandTest {

    private ModelStub modelStub;
    private Tag tagMath;
    private Tag tagScience;
    private Tag tagHistory;

    // Helper Sets
    private Set<Tag> allThreeTags;
    private Set<Tag> tagMathAndScience;
    private Set<Tag> onlyTagHistory;

    @BeforeEach
    void setUp() {
        modelStub = new ModelStub();
        tagMath = new Tag("Math");
        tagScience = new Tag("Science");
        tagHistory = new Tag("History"); // Third tag for mixed testing

        // Initial state of the AddressBook for testing: only Math and Science tags exist
        modelStub.addedTags.add(tagMath);
        modelStub.addedTags.add(tagScience);

        // Define helper sets
        allThreeTags = Set.of(tagMath, tagScience, tagHistory);
        tagMathAndScience = Set.of(tagMath, tagScience);
        onlyTagHistory = Set.of(tagHistory);
    }

    // =================================== Constructor Tests ===================================

    @Test
    void constructor_nullTags_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DeleteTagsCommand(null));
    }

    // =================================== Positive Execution Tests (Success Paths) ===================================

    @Test
    void execute_deleteExistingTags_fullSuccess() throws Exception {
        Set<Tag> tagsToDelete = tagMathAndScience;
        DeleteTagsCommand command = new DeleteTagsCommand(tagsToDelete);
        command.execute(modelStub);

        // Verification of side effect on ModelStub: All initial tags are gone
        assertFalse(modelStub.addedTags.contains(tagMath));
        assertFalse(modelStub.addedTags.contains(tagScience));
        assertTrue(modelStub.addedTags.isEmpty());
    }

    @Test
    void execute_mixedTags_partialSuccess() throws Exception {
        // Deleting all three tags: Math (exists), Science (exists), History (non-existent)
        Set<Tag> tagsToDelete = allThreeTags;

        DeleteTagsCommand command = new DeleteTagsCommand(tagsToDelete);

        command.execute(modelStub);

        // Verification of side effect on ModelStub: Math and Science should be gone
        assertFalse(modelStub.addedTags.contains(tagMath));
        assertFalse(modelStub.addedTags.contains(tagScience));
        assertTrue(modelStub.addedTags.isEmpty());
    }

    @Test
    void execute_deleteOneExistingOneNonExistent_partialSuccess() throws Exception {
        // Deleting Math (exists) and History (non-existent)
        Set<Tag> tagsToDelete = Set.of(tagMath, tagHistory);
        Set<Tag> deletedTags = Set.of(tagMath);
        Set<Tag> nonExistentTags = onlyTagHistory;

        DeleteTagsCommand command = new DeleteTagsCommand(tagsToDelete);

        CommandResult result = command.execute(modelStub);

        // Verification of result message
        String expectedMessage = String.format(
                MESSAGE_NON_EXISTENT_TAG + " All other tag(s) were successfully deleted: %2$s",
                nonExistentTags, deletedTags
        );
        assertEquals(expectedMessage, result.getFeedbackToUser());

        assertFalse(modelStub.addedTags.contains(tagMath));
        assertTrue(modelStub.addedTags.contains(tagScience));
    }

    @Test
    void execute_deleteNonExistingTags_fullFailure() throws Exception {
        // Deleting only History tag, which does not exist in the stub
        Set<Tag> tagsToDelete = onlyTagHistory;
        DeleteTagsCommand command = new DeleteTagsCommand(tagsToDelete);

        CommandResult result = command.execute(modelStub);

        // Verification of result message (full failure)
        String expectedMessage = String.format(MESSAGE_NON_EXISTENT_TAG, tagsToDelete);
        assertEquals(expectedMessage, result.getFeedbackToUser());

        assertTrue(modelStub.addedTags.contains(tagMath));
        assertTrue(modelStub.addedTags.contains(tagScience));
    }

    @Test
    void execute_nullModel_throwsNullPointerException() {
        Set<Tag> tagsToDelete = onlyTagHistory;
        DeleteTagsCommand command = new DeleteTagsCommand(tagsToDelete);
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    void toString_containsTagNames() {
        Set<Tag> tags = Set.of(tagMath, tagScience);
        String result = new DeleteTagsCommand(tags).toString();

        assertTrue(result.contains("toDelete"));
        assertTrue(result.contains("Math"));
        assertTrue(result.contains("Science"));
    }

    /**
     * Minimal stub for Model to simulate tag storage.
     * All unused methods throw AssertionError to catch unintended calls.
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

        // --- START: Unused methods from original stub (throwing AssertionError) ---

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
            // Can be left empty or throw assertion
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
            // Can be left empty or throw assertion
        }

        @Override
        public void addPerson(Person person) {
            // Can be left empty or throw assertion
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            // Can be left empty or throw assertion
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
            // Can be left empty or throw assertion
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

        // --- END: Unused methods from original stub ---
    }
}
