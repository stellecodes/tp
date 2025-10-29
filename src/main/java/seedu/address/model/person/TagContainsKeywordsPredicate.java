package seedu.address.model.person;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Student}'s tags matches any of the keywords given.
 */
public class TagContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    /**
     * Constructs a {@code TagContainsKeywordsPredicate} with the given keywords.
     *
     * @param keywords A list of tag keywords to match against a student's tags.
     */
    public TagContainsKeywordsPredicate(List<String> keywords) {
        // normalize list: avoid nulls and blanks
        this.keywords = (keywords == null)
                ? Collections.emptyList()
                : keywords.stream()
                .filter(s -> s != null && !s.isBlank())
                .map(String::trim)
                .collect(Collectors.toList());
    }

    @Override
    public boolean test(Person person) {
        // skip invalid or empty cases
        if (person == null || keywords.isEmpty()) {
            return false;
        }

        // only Students have tags — ignore others
        if (!(person instanceof Student)) {
            return false;
        }

        Student student = (Student) person;

        return student.getTags().stream()
                .map(Tag::getTagName)
                .anyMatch(tagName ->
                        keywords.stream()
                                .anyMatch(keyword ->
                                        tagName.equalsIgnoreCase(keyword)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagContainsKeywordsPredicate)) {
            return false;
        }

        TagContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (TagContainsKeywordsPredicate) other;
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }

    public List<String> getKeywords() {
        return this.keywords;
    }
}
