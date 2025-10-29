package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.Model;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Filters and lists all students whose tags contain any of the specified keywords.
 */
public class FilterTagCommand extends Command {

    public static final String COMMAND_WORD = "filter";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters students by tag(s).\n"
            + "Parameters: TAG [MORE_TAGS]...\n"
            + "Example: " + COMMAND_WORD + " math j2";

    private final TagContainsKeywordsPredicate predicate;

    public FilterTagCommand(TagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        // Use the master registry instead of scanning current Students
        Set<String> knownTags = model.getAddressBook().getTagList().getTags()
                .stream()
                .map(t -> t.tagName.trim().toLowerCase())
                .collect(Collectors.toSet());

        List<String> requested = predicate.getKeywords();

        List<String> valid = requested.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .filter(s -> knownTags.contains(s.toLowerCase()))
                .collect(Collectors.toList());

        List<String> missing = requested.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .filter(s -> !knownTags.contains(s.toLowerCase()))
                .collect(Collectors.toList());

        if (valid.isEmpty()) {
            // All requested tags are unknown → early message
            String msg = (missing.size() == 1)
                    ? String.format("The tag '%s' does not exist. Refer to tag list for valid tags or add new tags.",
                    missing.get(0))
                    : String.format("These tags do not exist: %s. Refer to tag list for valid tags or add new tags.",
                    String.join(", ", missing));
            return new CommandResult(msg);
        }

        // Filter by only the valid tags
        TagContainsKeywordsPredicate validPredicate = new TagContainsKeywordsPredicate(valid);
        model.updateFilteredPersonList(validPredicate);

        String base = String.format("Listed %d persons with tag(s): %s.",
                model.getFilteredPersonList().size(), String.join(", ", valid));

        if (!missing.isEmpty()) {
            String note = (missing.size() == 1)
                    ? String.format(" Note: tag '%s' does not exist and was ignored.", missing.get(0))
                    : String.format(" Note: these tags do not exist and were ignored: %s.",
                    String.join(", ", missing));
            return new CommandResult(base + note);
        }

        return new CommandResult(base);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterTagCommand)) {
            return false;
        }

        FilterTagCommand otherFilterTagCommand = (FilterTagCommand) other;
        return predicate.equals(otherFilterTagCommand.predicate);
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + "{predicate=" + predicate + "}";
    }
}
