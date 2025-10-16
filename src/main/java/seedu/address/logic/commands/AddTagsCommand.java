package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

/**
 * Adds a person to the address book.
 */
public class AddTagsCommand extends Command {

    public static final String COMMAND_WORD = "add_tags";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + " Parameters: "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New tags added: %1$s";
    public static final String MESSAGE_DUPLICATE_TAG = "Some tags already exist and were not added: %1$s";

    private final Set<Tag> toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddTagsCommand(Set<Tag> tags) {
        requireNonNull(tags);
        toAdd = tags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.addTagTypes(toAdd);
        return new CommandResult("Tags added successfully!");
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
