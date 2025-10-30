package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

/**
 * Adds a person to the address book.
 */
public class AddTagsCommand extends Command {

    public static final String COMMAND_WORD = "addtag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + " Parameters: "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "Math "
            + PREFIX_TAG + "Science";

    public static final String MESSAGE_SUCCESS = "New tag(s) added: %1$s";
    public static final String MESSAGE_DUPLICATE_TAG = "The following tag(s) already exist and were not added: %1$s.";

    private final Set<Tag> toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddTagsCommand(Set<Tag> tags) {
        assert tags != null : "Tags should not be null";
        toAdd = tags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        assert model != null : "Model should not be null";

        Set<Tag> existingTags = new HashSet<>();
        Set<Tag> newTags = new HashSet<>();

        for (Tag tag : toAdd) {
            if (model.hasTag(tag)) {
                existingTags.add(tag);
            } else {
                newTags.add(tag);
            }
        }

        model.addTagTypes(newTags);

        if (!existingTags.isEmpty()) {
            if (newTags.isEmpty()) {
                return new CommandResult(String.format(MESSAGE_DUPLICATE_TAG, existingTags));
            }
            return new CommandResult(String.format(
                    MESSAGE_DUPLICATE_TAG + " All other tag(s) were successfully added: %2$s",
                    existingTags, newTags
            ));
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, newTags));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
