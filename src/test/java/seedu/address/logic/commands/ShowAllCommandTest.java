package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code ShowAllCommand}.
 */
public class ShowAllCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_showAll_success() {
        model.updateFilteredPersonList(person -> person.getName().fullName.contains("Alice"));

        ShowAllCommand command = new ShowAllCommand();

        String expectedMessage = ShowAllCommand.MESSAGE_SUCCESS;

        expectedModel.updateFilteredPersonList(person -> true);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getFilteredPersonList().size(),
                model.getFilteredPersonList().size(),
                "All persons should be visible after showall command.");
    }

    @Test
    public void equals_sameInstance_returnsTrue() {
        ShowAllCommand command = new ShowAllCommand();
        assertTrue(command.equals(command));
    }

    @Test
    public void equals_differentInstances_returnsTrue() {
        assertTrue(new ShowAllCommand().equals(new ShowAllCommand()));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        assertFalse(new ShowAllCommand().equals(1));
    }

    @Test
    public void equals_null_returnsFalse() {
        assertFalse(new ShowAllCommand().equals(null));
    }

    @Test
    public void toStringMethod() {
        ShowAllCommand command = new ShowAllCommand();
        String expected = ShowAllCommand.class.getCanonicalName() + "{}";
        assertEquals(expected, command.toString());
    }
}
