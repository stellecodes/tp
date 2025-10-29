package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.Model;
import seedu.address.model.person.Student;
import seedu.address.model.person.TagContainsKeywordsPredicate;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

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

        Set<String> existingTagNames = model.getAddressBook().getPersonList().stream()
                .filter(p -> p instanceof Student)
                .map(p -> (Student) p)
                .flatMap(s -> s.getTags().stream())
                .map(tag -> tag instanceof Tag
                        ? ((Tag) tag).tagName
                        : tag.toString())
                .map(name -> name.trim().toLowerCase())
                .collect(Collectors.toSet());

        // Split request into valid vs missing
        List<String> requested = predicate.getKeywords(); // existing predicate input
        List<String> valid = requested.stream()
                .map(s -> s.trim())
                .filter(s -> !s.isEmpty())
                .filter(s -> existingTagNames.contains(s.toLowerCase()))
                .collect(Collectors.toList());

        List<String> missing = requested.stream()
                .map(s -> s.trim())
                .filter(s -> !s.isEmpty())
                .filter(s -> !existingTagNames.contains(s.toLowerCase()))
                .collect(Collectors.toList());

        // If nothing valid, send missing message
        if (valid.isEmpty()) {
            String msg = (missing.size() == 1)
                    ? String.format("The tag '%s' does not exist. Refer to tag list for valid tags or add a new tag.", missing.get(0))
                    : String.format("These tags do not exist: %s. Refer to tag list for valid tags or add new tags.", String.join(", ", missing));
            return new CommandResult(msg);
        }

        // Filter using only valid tags
        TagContainsKeywordsPredicate validPredicate = new TagContainsKeywordsPredicate(valid);
        model.updateFilteredPersonList(validPredicate);

        // Prepare UX message
        String validShown = String.join(", ", valid);

        if (model.getFilteredPersonList().isEmpty()) {
            // Tags exist but students don't have them
            String base = String.format("No users found with tag(s): %s.", validShown);
            if (!missing.isEmpty()) {
                String note = (missing.size() == 1)
                        ? String.format(" Note: tag '%s' does not exist.", missing.get(0))
                        : String.format(" Note: these tags do not exist: %s.", String.join(", ", missing));
                return new CommandResult(base + note);
            }
            return new CommandResult(base);
        }

        String base = String.format("Listed %d persons with tag(s): %s.",
                model.getFilteredPersonList().size(), validShown);

        if (!missing.isEmpty()) {
            String note = (missing.size() == 1)
                    ? String.format(" Note: tag '%s' does not exist and was ignored.", missing.get(0))
                    : String.format(" Note: these tags do not exist and were ignored: %s.", String.join(", ", missing));
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
