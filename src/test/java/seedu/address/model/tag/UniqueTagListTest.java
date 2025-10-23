package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UniqueTagListTest {

    private UniqueTagList uniqueTagList;
    private Tag mathTag;
    private Tag scienceTag;

    @BeforeEach
    void setUp() {
        uniqueTagList = new UniqueTagList();
        mathTag = new Tag("Math");
        scienceTag = new Tag("Science");
    }

    @Test
    void contains_tagPresent_returnsTrue() {
        uniqueTagList.addTagTypes(Set.of(mathTag));
        assertTrue(uniqueTagList.contains(mathTag));
    }

    @Test
    void contains_tagAbsent_returnsFalse() {
        assertFalse(uniqueTagList.contains(mathTag));
    }

    @Test
    void setTags_replacesTagsSuccessfully() {
        UniqueTagList replacement = new UniqueTagList();
        replacement.addTagTypes(Set.of(mathTag));
        uniqueTagList.addTagTypes(Set.of(scienceTag));

        uniqueTagList.setTags(replacement);

        assertTrue(uniqueTagList.contains(mathTag));
        assertFalse(uniqueTagList.contains(scienceTag));
    }

    @Test
    void getTags_returnsUnmodifiableSet() {
        uniqueTagList.addTagTypes(Set.of(mathTag));
        Set<Tag> tags = uniqueTagList.getTags();
        assertTrue(tags.contains(mathTag));
    }

    @Test
    void equals_sameInstance_returnsTrue() {
        assertTrue(uniqueTagList.equals(uniqueTagList));
    }

    @Test
    void equals_sameContents_returnsTrue() {
        UniqueTagList other = new UniqueTagList();
        uniqueTagList.addTagTypes(Set.of(mathTag));
        other.addTagTypes(Set.of(mathTag));
        assertEquals(uniqueTagList, other);
    }

    @Test
    void equals_differentContents_returnsFalse() {
        UniqueTagList other = new UniqueTagList();
        uniqueTagList.addTagTypes(Set.of(mathTag));
        other.addTagTypes(Set.of(scienceTag));
        assertNotEquals(uniqueTagList, other);
    }

    @Test
    void hashCode_sameContents_sameHash() {
        UniqueTagList other = new UniqueTagList();
        uniqueTagList.addTagTypes(Set.of(mathTag));
        other.addTagTypes(Set.of(mathTag));
        assertEquals(uniqueTagList.hashCode(), other.hashCode());
    }

    @Test
    void toString_containsTags() {
        uniqueTagList.addTagTypes(Set.of(mathTag));
        String str = uniqueTagList.toString();
        assertTrue(str.contains("Math"));
    }

    @Test
    void addTagTypes_addsAndReturnsAlreadyPresent() {
        uniqueTagList.addTagTypes(Set.of(mathTag));
        Set<Tag> alreadyPresent = uniqueTagList.addTagTypes(Set.of(mathTag, scienceTag));

        assertTrue(uniqueTagList.contains(mathTag));
        assertTrue(uniqueTagList.contains(scienceTag));
        assertTrue(alreadyPresent.contains(mathTag));
        assertFalse(alreadyPresent.contains(scienceTag));
    }

    @Test
    void deleteTagTypes_removesTagsSuccessfully() {
        uniqueTagList.addTagTypes(Set.of(mathTag, scienceTag));
        uniqueTagList.deleteTagTypes(Set.of(mathTag));

        assertFalse(uniqueTagList.contains(mathTag));
        assertTrue(uniqueTagList.contains(scienceTag));
    }
}
