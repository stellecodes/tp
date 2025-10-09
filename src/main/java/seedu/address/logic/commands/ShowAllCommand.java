package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Displays all persons in the address book.
 * This command resets any applied filters and shows the complete person list.
 */
public class ShowAllCommand extends Command {

    public static final String COMMAND_WORD = "showall";
    public static final String MESSAGE_SUCCESS = "Showing all persons.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        // Reset filter to show everyone
        model.updateFilteredPersonList(person -> true);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ShowAllCommand; // ShowAllCommand should be the same for all
    }
}
