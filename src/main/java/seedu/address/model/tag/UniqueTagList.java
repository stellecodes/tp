package seedu.address.model.tag;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Person;
import seedu.address.model.person.Student;
import seedu.address.model.tag.exceptions.TagNotFoundException;

/**
 * A hashmap that maps each unique Tag to an ObservableList of Student objects.
 * Enforces uniqueness of tags and non-null constraints.
 */
public class UniqueTagList {
    private final Set<Tag> tagSet = new HashSet<>();

    /**
     * Returns true if the list contains an equivalent tag as the given argument.
     */
    public boolean contains(Tag toCheck) {
        assert toCheck != null : "Tag to check should not be null";
        return tagSet.contains(toCheck);
    }

    /**
     * Replaces the current map with another UniqueTagList's contents.
     */
    public void setTags(UniqueTagList replacement) {
        assert replacement != null : "Replacement should not be null";
        tagSet.clear();
        tagSet.addAll(replacement.getTags());
    }

    /**
     * Returns all tags currently in the map.
     */
    public Set<Tag> getTags() {
        return this.tagSet;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof UniqueTagList
                && tagSet.equals(((UniqueTagList) other).tagSet));
    }

    @Override
    public int hashCode() {
        return tagSet.hashCode();
    }

    @Override
    public String toString() {
        return tagSet.toString();
    }

    /**
     * Returns true if all tags of the given person exist in the tag list.
     * If the person is not a student, returns false.
     * @throws TagNotFoundException if any tag of the person does not exist in the tag list.
     */
    public boolean personHasValidTags(Person person) throws TagNotFoundException {
        assert person != null : "Person should not be null";
        if (!(person instanceof Student student)) {
            return false;
        }
        Set<Tag> tags = student.getTags();
        for (Tag tag : tags) {
            if (!contains(tag)) {
                throw new TagNotFoundException(tag);
            }
        }
        return true;
    }

    /**
     * Adds multiple tag types to the map.
     * Returns a set of tags that were not added because they already exist.
     */
    public Set<Tag> addTagTypes(Set<Tag> tagsToAdd) {
        assert tagsToAdd != null : "Tags should not be null";
        Set<Tag> alreadyPresent = new HashSet<>();
        for (Tag tag : tagsToAdd) {
            if (contains(tag)) {
                alreadyPresent.add(tag);
            } else {
                tagSet.add(tag);
            }
        }
        return alreadyPresent;
    }

    /**
     * Deletes multiple tag types from the map.
     */
    public void deleteTagTypes(Set<Tag> tagsToDelete) {
        assert tagsToDelete != null : "Tags should not be null";
        for (Tag toDelete : tagsToDelete) {
            tagSet.remove(toDelete);
        }
    }
}
