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
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(
                Collections.singletonList("friends"));
        FilterTagCommand command = new FilterTagCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        String expectedMessage = String.format("Listed %d persons with tag(s): %s.",
                expectedModel.getFilteredPersonList().size(), "friends");

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleTagKeywords_unionOfPeopleFound() {
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(
                Arrays.asList("friends", "owesMoney"));
        FilterTagCommand command = new FilterTagCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        String expectedMessage = String.format("Listed %d persons with tag(s): %s.",
                expectedModel.getFilteredPersonList().size(), "friends, owesMoney");

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_missingTag_only_missing_showsNoSuchTag_noFilterApplied() {
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
    public void execute_mixedValidAndMissing_filtersByValid_andWarnsAboutMissing() {
        // valid: "friends", missing: "unicorns"
        TagContainsKeywordsPredicate mixed =
                new TagContainsKeywordsPredicate(Arrays.asList("friends", "unicorns"));
        FilterTagCommand command = new FilterTagCommand(mixed);

        // Expected: filter only by the valid subset
        TagContainsKeywordsPredicate validOnly =
                new TagContainsKeywordsPredicate(Collections.singletonList("friends"));
        expectedModel.updateFilteredPersonList(validOnly);

        CommandResult result = command.execute(model);

        // Message should include normal success + note about the missing tag
        String msg = result.getFeedbackToUser();
        assertTrue(msg.contains("Listed"));
        assertTrue(msg.contains("friends"));
        assertTrue(msg.toLowerCase().contains("ignored") || msg.toLowerCase().contains("does not exist"));
        assertTrue(msg.contains("unicorns"));

        // Result list must match filtering by the valid tag only
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
