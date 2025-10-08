package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Links two contacts together (e.g. a student and a parent).
 */
public class LinkCommand extends Command {

    public static final String COMMAND_WORD = "link";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Links two contacts together by name and phone.\n"
            + "Parameters: sn/STUDENT_NAME [sp/STUDENT_PHONE] pn/PARENT_NAME [pp/PARENT_PHONE]\n"
            + "Example: " + COMMAND_WORD + " sn/John Tan sp/91234567 pn/Mrs Tan pp/92345678";

    public static final String MESSAGE_LINK_SUCCESS = "Linked %1$s ↔ %2$s";
    public static final String MESSAGE_DUPLICATE_LINK = "These contacts are already linked.";
    public static final String MESSAGE_SAME_PERSON = "Cannot link a contact to itself.";
    public static final String MESSAGE_NOT_FOUND = "One or both contacts could not be found.";

    private final Person student;
    private final Person parent;

    /**
     * Creates a LinkCommand with both contacts resolved.
     */
    public LinkCommand(Person student, Person parent) {
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

        boolean success = model.link(student, parent);
        if (!success) {
            throw new CommandException(MESSAGE_DUPLICATE_LINK);
        }

        return new CommandResult(String.format(MESSAGE_LINK_SUCCESS,
                Messages.format(student), Messages.format(parent)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LinkCommand)) {
            return false;
        }
        LinkCommand otherCommand = (LinkCommand) other;
        return student.equals(otherCommand.student)
                && parent.equals(otherCommand.parent);
    }
}

