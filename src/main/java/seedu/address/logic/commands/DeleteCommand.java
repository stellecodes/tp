package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Deletes a person identified by index (legacy) or by attributes (name/email/phone).
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the specified person from the address book.\n"
            + "Legacy (index-based): delete INDEX (must be a positive integer)\n"
            + "Attribute-based: delete [n/NAME] [e/EMAIL] [p/PHONE]";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    // Either index is present OR one/more of these fields, hence usage of Optional
    private final Optional<Index> targetIndex;
    private final Optional<Name> name;
    private final Optional<Email> email;
    private final Optional<Phone> phone;

    /** Constructor for index-based delete */
    public DeleteCommand(Index targetIndex) {
        this.targetIndex = Optional.of(targetIndex);
        this.name = Optional.empty();
        this.email = Optional.empty();
        this.phone = Optional.empty();
    }

    /** Constructor for attribute-based delete */
    public DeleteCommand(Optional<Name> name, Optional<Email> email, Optional<Phone> phone) {
        if (name.isEmpty() && email.isEmpty() && phone.isEmpty()) {
            throw new IllegalArgumentException("At least one of name/email/phone must be provided.");
        }
        this.targetIndex = Optional.empty();
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // if index is present, perform legacy behaviour for delete
        if (targetIndex.isPresent()) {
            var lastShownList = model.getFilteredPersonList();
            if (targetIndex.get().getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            Person personToDelete = lastShownList.get(targetIndex.get().getZeroBased());
            model.deletePerson(personToDelete);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS,
                    Messages.format(personToDelete)));
        }

        // Placeholder, actual attribute-based logic will be added in next commits
        throw new CommandException("Attribute-based delete not yet implemented.");
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeleteCommand)) {
            return false;
        }
        DeleteCommand o = (DeleteCommand) other;
        return targetIndex.equals(o.targetIndex)
                && name.equals(o.name)
                && email.equals(o.email)
                && phone.equals(o.phone);
    }
}
