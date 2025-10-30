package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

/**
 * Deletes existing tags from the address book.
 */
public class DeleteTagsCommand extends Command {

    public static final String COMMAND_WORD = "deletetag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + " Parameters: "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "Math "
            + PREFIX_TAG + "Science";

    public static final String MESSAGE_SUCCESS = "Tag(s) successfully deleted: %1$s";
    public static final String MESSAGE_NON_EXISTENT_TAG = "The following tag(s) do not exist "
            + "and were not deleted: %1$s.";

    private final Set<Tag> toDelete;

    /**
     * Creates a DeleteTagsCommand to delete the specified {@code Tag}s.
     */
    public DeleteTagsCommand(Set<Tag> tags) {
        requireNonNull(tags);
        toDelete = tags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Set<Tag> deletedTags = new HashSet<>();
        Set<Tag> nonExistentTags = new HashSet<>();

        // 1. Separate tags into those that exist and those that don't
        for (Tag tag : toDelete) {
            if (model.hasTag(tag)) {
                deletedTags.add(tag);
            } else {
                nonExistentTags.add(tag);
            }
        }

        // 2. Only perform deletion on tags that exist
        model.deleteTagTypes(deletedTags);

        // 3. Construct CommandResult based on deletion outcome (mimicking AddTagsCommand)
        if (!nonExistentTags.isEmpty()) {
            if (deletedTags.isEmpty()) {
                // Case 1: None of the specified tags existed.
                return new CommandResult(String.format(MESSAGE_NON_EXISTENT_TAG, nonExistentTags));
            }
            // Case 2: Partial success - some deleted, some didn't exist.
            return new CommandResult(String.format(
                    MESSAGE_NON_EXISTENT_TAG + " All other tag(s) were successfully deleted: %2$s",
                    nonExistentTags, deletedTags
            ));
        }

        // Case 3: Full success - all specified tags existed and were deleted.
        return new CommandResult(String.format(MESSAGE_SUCCESS, deletedTags));
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toDelete", toDelete)
                .toString();
    }
}
