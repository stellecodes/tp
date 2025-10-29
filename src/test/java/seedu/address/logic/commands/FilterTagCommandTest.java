package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.TagContainsKeywordsPredicate;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterTagCommand}.
 */
public class FilterTagCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        model.getAddressBook().getTagList().getTags().add(new Tag("friends")); // or via StudentBuilder
        model.getAddressBook().getTagList().getTags().add(new Tag("owesMoney"));
        expectedModel.getAddressBook().getTagList().getTags().add(new Tag("friends"));
        expectedModel.getAddressBook().getTagList().getTags().add(new Tag("owesMoney"));

    }

    @Test
    public void equals() {
        TagContainsKeywordsPredicate firstPredicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("Math"));
        TagContainsKeywordsPredicate secondPredicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("Chem"));

        FilterTagCommand filterFirstCommand = new FilterTagCommand(firstPredicate);
        FilterTagCommand filterSecondCommand = new FilterTagCommand(secondPredicate);

        // same object -> returns true
        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        // same values -> returns true
        FilterTagCommand filterFirstCommandCopy = new FilterTagCommand(firstPredicate);
        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        // different types -> returns false
        assertFalse(filterFirstCommand.equals(1));

        // null -> returns false
        assertFalse(filterFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(filterFirstCommand.equals(filterSecondCommand));
    }

    @Test
    public void execute_singleTagKeyword_somePeopleFound() {
        // Tag exists in registry (friends)
        TagContainsKeywordsPredicate predicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("friends"));
        FilterTagCommand command = new FilterTagCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);

        // The command produces: "Listed X persons with tag(s): friends."
        String expectedMessage = String.format("Listed %d persons with tag(s): %s.",
                expectedModel.getFilteredPersonList().size(), "friends");

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleTagKeywords_unionOfPeopleFound() {
        TagContainsKeywordsPredicate predicate =
                new TagContainsKeywordsPredicate(Arrays.asList("friends", "owesMoney"));
        FilterTagCommand command = new FilterTagCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);

        String expectedMessage = String.format("Listed %d persons with tag(s): %s.",
                expectedModel.getFilteredPersonList().size(), "friends, owesMoney");

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_mixedValidAndMissing_filtersByValidAndWarnsAboutMissing() {
        TagContainsKeywordsPredicate mixed =
                new TagContainsKeywordsPredicate(Arrays.asList("friends", "unicorns"));
        FilterTagCommand command = new FilterTagCommand(mixed);

        TagContainsKeywordsPredicate validOnly =
                new TagContainsKeywordsPredicate(Collections.singletonList("friends"));
        expectedModel.updateFilteredPersonList(validOnly);

        CommandResult result = command.execute(model);
        String actualMessage = result.getFeedbackToUser();

        // "Listed X persons with tag(s): friends. Note: tag 'unicorns' does not exist and was ignored."
        String expectedMessageStart = String.format("Listed %d persons with tag(s): %s.",
                expectedModel.getFilteredPersonList().size(), "friends");

        assertTrue(actualMessage.startsWith(expectedMessageStart),
                "Expected message to start with: " + expectedMessageStart);
        assertTrue(actualMessage.toLowerCase().contains("unicorns"),
                "Expected missing tag name to appear");
        assertTrue(actualMessage.toLowerCase().contains("ignored")
                        || actualMessage.toLowerCase().contains("does not exist"),
                "Expected note about ignored / does not exist");

        // Ensure filtered list matches the valid-only predicate
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_mixedTags_warnsAboutMissing() {
        TagContainsKeywordsPredicate predicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("nonexistentTag"));
        FilterTagCommand command = new FilterTagCommand(predicate);

        var before = model.getFilteredPersonList();
        CommandResult result = command.execute(model);

        String msg = result.getFeedbackToUser();
        assertTrue(msg.contains("does not exist"));

        // Ensure list did not change
        assertEquals(before, model.getFilteredPersonList());
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(
                Collections.singletonList("keyword"));
        FilterTagCommand command = new FilterTagCommand(predicate);
        String expected = FilterTagCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }

    /**
     * Parses {@code userInput} into a {@code TagContainsKeywordsPredicate}, removing blanks.
     */
    private TagContainsKeywordsPredicate preparePredicate(String userInput) {
        List<String> tokens = Arrays.stream(userInput.trim().split("\\s+"))
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());
        return new TagContainsKeywordsPredicate(tokens);
    }
}
