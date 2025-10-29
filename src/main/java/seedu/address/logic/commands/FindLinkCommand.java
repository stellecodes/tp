package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Shows all contacts linked to the given person name (Student ↔ Parent).
 * <p>
 * Usage: {@code findlink n/<NAME>}
 * Example: {@code findlink n/John Tan}
 */
public class FindLinkCommand extends Command {

    public static final String COMMAND_WORD = "findlink";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows contacts linked to the given person.\n"
            + "Parameters: n/NAME\n"
            + "Example: " + COMMAND_WORD + " n/John Tan";

    public static final String MESSAGE_NOT_FOUND = "No person found with the name: %s";
    public static final String MESSAGE_RESULT = "Showing %d linked contact(s) for %s";

    private final String targetName;

    /**
     * @param targetName Name of the person whose links should be shown (case-insensitive).
     */
    public FindLinkCommand(String targetName) {
        requireNonNull(targetName);
        this.targetName = targetName.trim();
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Resolves the target Person by name (case-insensitive)
        Person target = findPersonByNameIgnoreCase(model, targetName)
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_NOT_FOUND, targetName)));

        // Ask the Model for linked persons (Student<->Parent)
        List<Person> linked = model.getLinkedPersons(target);

        // Build a predicate that matches only the linked contacts and apply to filtered list
        Predicate<Person> showOnlyLinked = buildMembershipPredicate(linked);
        model.updateFilteredPersonList(showOnlyLinked);

        String header = String.format(MESSAGE_RESULT, linked.size(), target.getName().fullName);
        return new CommandResult(header);
    }

    /** Find a person by name (case-insensitive) among all persons in the address book. */
    private Optional<Person> findPersonByNameIgnoreCase(Model model, String name) {
        ObservableList<Person> all = model.getAddressBook().getPersonList();
        String needle = name.toLowerCase();
        return all.stream()
                .filter(p -> p.getName().fullName.toLowerCase().equals(needle))
                .findFirst();
    }

    /** Builds a predicate that returns true only for members of the given list. */
    private Predicate<Person> buildMembershipPredicate(List<Person> persons) {
        Set<Person> set = new HashSet<>(persons);
        return set::contains;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FindLinkCommand
                && targetName.equals(((FindLinkCommand) other).targetName));
    }

    @Override
    public String toString() {
        return "FindLinkCommand{targetName=" + targetName + "}";
    }
}
