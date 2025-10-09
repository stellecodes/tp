package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
 *  Supported forms:
 *    - delete INDEX
 *    - delete [n/NAME] [e/EMAIL] [p/PHONE]
 *    - delete n/NAME e/EMAIL p/PHONE  (AND semantics)
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the specified person from the address book.\n"
            + "Legacy (index-based): delete INDEX (must be a positive integer)\n"
            + "Attribute-based: delete [n/NAME] [e/EMAIL] [p/PHONE]\n"
            + "Examples:\n"
            + "  delete 2\n"
            + "  delete e/alex@example.com\n"
            + "  delete p/91234567\n"
            + "  delete n/Ada Lovelace e/ada@example.com";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted: %1$s";
    public static final String MESSAGE_NO_MATCH = "No person matches the given detail(s).";
    public static final String MESSAGE_MULTIPLE_MATCHES =
            "Multiple persons match the given detail(s). Please refine using email/phone, "
                    + "or delete by index after using 'find'.";

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

        // Attribute-based behaviour: filter the currently shown list (consistent with AB3 workflow).
        List<Person> candidates = model.getFilteredPersonList();

        // Starts with a predicate that matches everyone.
        Predicate<Person> predicate = p -> true;

        // For each provided optional, we AND another test to narrow down the set
        // Usage of equalsIgnoreCase for name/ email, but exact match for phone
        if (name.isPresent()) {
            String target = name.get().fullName;
            predicate = predicate.and(p -> p.getName().fullName.equalsIgnoreCase(target));
        }
        if (email.isPresent()) {
            String target = email.get().value;
            predicate = predicate.and(p -> p.getEmail().value.equalsIgnoreCase(target));
        }
        if (phone.isPresent()) {
            String target = phone.get().value;
            predicate = predicate.and(p -> p.getPhone().value.equals(target));
        }

        List<Person> matches = candidates.stream().filter(predicate).collect(Collectors.toList());

        // matches.size = 0, ie no such person, throw exception
        if (matches.isEmpty()) {
            throw new CommandException(MESSAGE_NO_MATCH);
        }

        // matches > 1, ie ambiguous, ask user to be more specific
        if (matches.size() > 1) {
            throw new CommandException(MESSAGE_MULTIPLE_MATCHES);
        }

        Person personToDelete = matches.get(0);
        model.deletePerson(personToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete)));
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

    @Override
    public String toString() {
        // Match test expectation: <canonical class name>{targetIndex=<Index.toString()>} OR attributes
        StringBuilder sb = new StringBuilder(getClass().getCanonicalName()).append("{");
        if (targetIndex.isPresent()) {
            sb.append("targetIndex=").append(targetIndex.get()); // uses Index#toString()
        } else {
            sb.append("name=").append(name)
                    .append(", email=").append(email)
                    .append(", phone=").append(phone);
        }
        sb.append("}");
        return sb.toString();
    }
}
