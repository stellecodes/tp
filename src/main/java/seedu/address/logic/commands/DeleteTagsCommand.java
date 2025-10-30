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
public class DeleteTagsCommand extends Command {

    public static final String COMMAND_WORD = "deletetag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + " Parameters: "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "Math "
            + PREFIX_TAG + "H2chemistry";

    public static final String MESSAGE_SUCCESS = "All tags deleted: %1$s";

    private final Set<Tag> toDelete;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public DeleteTagsCommand(Set<Tag> tags) {
        requireNonNull(tags);
        toDelete = tags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        for (Tag tag : toDelete) {
            if (!model.hasTag(tag)) {
                throw new CommandException(String.format("Tag %s does not exist", tag));
            }
        }
        model.deleteTagTypes(toDelete);
        return new CommandResult("Tags deleted successfully!");
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toDelete", toDelete)
                .toString();
    }
}
