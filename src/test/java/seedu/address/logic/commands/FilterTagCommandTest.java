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
    public void execute_zeroKeywords_noPersonFound() {
        // Build a predicate with NO keywords (filter blanks out)
        TagContainsKeywordsPredicate predicate = preparePredicate("");

        FilterTagCommand command = new FilterTagCommand(predicate);

        // Expect 0 shown
        expectedModel.updateFilteredPersonList(predicate);
        String expectedMessage = String.format("%d people found with the specified tag(s).",
                expectedModel.getFilteredPersonList().size());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_singleTagKeyword_somePeopleFound() {
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(
                Collections.singletonList("friends"));
        FilterTagCommand command = new FilterTagCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        String expectedMessage = String.format("%d people found with the specified tag(s).",
                expectedModel.getFilteredPersonList().size());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleTagKeywords_unionOfPeopleFound() {
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(
                Arrays.asList("friends", "owesMoney"));
        FilterTagCommand command = new FilterTagCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        String expectedMessage = String.format("%d people found with the specified tag(s).",
                expectedModel.getFilteredPersonList().size());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_tagNotFound_noPersonFound() {
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(
                Collections.singletonList("nonexistentTag"));
        FilterTagCommand command = new FilterTagCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        String expectedMessage = String.format("%d people found with the specified tag(s).",
                expectedModel.getFilteredPersonList().size());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertTrue(expectedModel.getFilteredPersonList().isEmpty());
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
