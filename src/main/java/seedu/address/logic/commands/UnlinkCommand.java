package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Removes a link between two contacts (e.g. a student and a parent).
 */
public class UnlinkCommand extends Command {

    public static final String COMMAND_WORD = "unlink";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes the link between two contacts.\n"
            + "Parameters: sn/STUDENT_NAME pn/PARENT_NAME\n"
            + "Example: " + COMMAND_WORD + " sn/John Tan pn/Mrs Tan";

    public static final String MESSAGE_UNLINK_SUCCESS = "Unlinked %1$s ↔ %2$s";
    public static final String MESSAGE_NOT_LINKED = "These contacts are not currently linked.";
    public static final String MESSAGE_SAME_PERSON = "Cannot unlink a contact from itself.";
    public static final String MESSAGE_NOT_FOUND = "One or both contacts could not be found.";

    private final Person student;
    private final Person parent;

    /**
     * Create an UnlinkCommand with both contacts resolved.
     */
    public UnlinkCommand(Person student, Person parent) {
        requireNonNull(student);
        requireNonNull(parent);
        this.student = student;
        this.parent = parent;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (student.equals(parent)) {
            throw new CommandException(MESSAGE_SAME_PERSON);
        }

        boolean success = model.unlink(student, parent);
        if (!success) {
            throw new CommandException(MESSAGE_NOT_LINKED);
        }

        return new CommandResult(String.format(
                MESSAGE_UNLINK_SUCCESS,
                student.getName().fullName,
                parent.getName().fullName));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof UnlinkCommand
                && student.equals(((UnlinkCommand) other).student)
                && parent.equals(((UnlinkCommand) other).parent));
    }
}

