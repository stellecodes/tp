package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Set;

import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

/**
 * Lists all tags in the address book to the user.
 */
public class ListTagsCommand extends Command {
    public static final String COMMAND_WORD = "list_tags";
    public static final String MESSAGE_SUCCESS = "Listed all tags: %1$s";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        Set<Tag> tags = model.getTags();
        String tagList = tags.isEmpty() ? "No tags found." : tags.toString();
        return new CommandResult(String.format(MESSAGE_SUCCESS, tagList));
    }
}
