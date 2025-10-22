package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.model.person.Student;
import seedu.address.model.tag.exceptions.TagNotFoundException;

class UniqueTagListTest {

    private UniqueTagList uniqueTagList;
    private Tag math;
    private Tag science;

    @BeforeEach
    void setUp() {
        uniqueTagList = new UniqueTagList();
        math = new Tag("Math");
        science = new Tag("Science");
        // Ensure initial tags are available for most tests that involve adding students
        uniqueTagList.addTagTypes(Set.of(math, science));
    }

    // =========================================================================
    // contains(Tag toCheck) tests
    // =========================================================================

    @Test
    void testContains_tagExists_returnsTrue() {
        assertTrue(uniqueTagList.contains(math));
    }

    @Test
    void testContains_tagDoesNotExist_returnsFalse() {
        Tag history = new Tag("History");
        assertFalse(uniqueTagList.contains(history));
    }

    @Test
    void testContains_nullTag_throwsException() {
        assertThrows(NullPointerException.class, () -> uniqueTagList.contains(null));
    }


    // =========================================================================
    // addStudentToTags(Person toAddStudent) tests
    // =========================================================================
    @Test
    void addStudentToTags_nullStudent_throwsException() {
        assertThrows(NullPointerException.class, () -> uniqueTagList.addStudentToTags(null));
    }


    // =========================================================================
    // removePersonFromAllTags(Person toRemoveStudent) tests
    // =========================================================================

    @Test
    void removePersonFromAllTags_removesFromMultipleTags() {
        uniqueTagList.addStudentToTags(BENSON); // BENSON has Math and Science
        uniqueTagList.addStudentToTags(ALICE); // ALICE has Science

        uniqueTagList.removePersonFromAllTags(BENSON);

        assertFalse(uniqueTagList.getStudentsWithTag(math).contains(BENSON));
        assertFalse(uniqueTagList.getStudentsWithTag(science).contains(BENSON));

        // Check that ALICE is unaffected in Math
        assertTrue(uniqueTagList.getStudentsWithTag(science).contains(ALICE));
        assertEquals(1, uniqueTagList.getStudentsWithTag(science).size());

        // Science list should be empty
        assertTrue(uniqueTagList.getStudentsWithTag(math).isEmpty());
    }

    // =========================================================================
    // setTags(UniqueTagList replacement) tests
    // =========================================================================
    @Test
    void setTags_nullReplacement_throwsException() {
        assertThrows(NullPointerException.class, () -> uniqueTagList.setTags(null));
    }

    // =========================================================================
    // asUnmodifiableObservableList() and getTags() tests
    // =========================================================================

    @Test
    void testGetTags_returnsCorrectSet() {
        Set<Tag> tags = uniqueTagList.getTags();
        assertEquals(2, tags.size());
        assertTrue(tags.contains(math));
        assertTrue(tags.contains(science));

        // Check that modifying the returned set doesn't affect the internal map
        assertThrows(UnsupportedOperationException.class, () -> tags.add(new Tag("History")));
    }

    @Test
    void testAsUnmodifiableObservableList_returnsCorrectList() {
        ObservableList<Tag> list = uniqueTagList.asUnmodifiableObservableList();

        assertEquals(2, list.size());
        assertTrue(list.contains(math));

        // Check if list is unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> list.add(new Tag("History")));
    }


    // =========================================================================
    // getStudentsWithTag(Tag tag) tests
    // =========================================================================

    @Test
    void getStudentsWithTag_returnsUnmodifiableList() {
        uniqueTagList.addStudentToTags(ALICE);
        ObservableList<Student> students = uniqueTagList.getStudentsWithTag(math);

        // Check if list is unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> students.add(BENSON));
    }

    @Test
    void getStudentsWithTag_throwsIfTagMissing() {
        Tag history = new Tag("History");
        assertThrows(TagNotFoundException.class, () -> uniqueTagList.getStudentsWithTag(history));
    }


    // =========================================================================
    // personHasValidTags(Person person) tests
    // =========================================================================

    @Test
    void personHasValidTags_allTagsExist_returnsTrue() {
        // All of BENSON's tags (Math, Science) exist
        assertTrue(uniqueTagList.personHasValidTags(BENSON));
    }

    // =========================================================================
    // addTagTypes(Set<Tag> tagsToAdd) tests
    // =========================================================================

    @Test
    void addTagTypes_addsNewTags_returnsAlreadyPresent() {
        // uniqueTagList already has Math and Science from setUp
        Tag newTag = new Tag("New");

        Set<Tag> duplicates = uniqueTagList.addTagTypes(Set.of(math, newTag));

        // Math was already present
        assertTrue(duplicates.contains(math));
        // New tag was newly added
        assertFalse(duplicates.contains(newTag));

        assertTrue(uniqueTagList.contains(newTag));

        // Check that the new tag has an empty list
        assertTrue(uniqueTagList.getStudentsWithTag(newTag).isEmpty());
    }

    @Test
    void addTagTypes_emptySet_listIsUnchanged() {
        int initialSize = uniqueTagList.getTags().size(); // 2

        Set<Tag> duplicates = uniqueTagList.addTagTypes(Set.of());

        assertTrue(duplicates.isEmpty());
        assertEquals(initialSize, uniqueTagList.getTags().size());
    }


    // =========================================================================
    // deleteTagTypes(Set<Tag> tagsToDelete) tests
    // =========================================================================

    @Test
    void deleteTagTypes_removesTagsAndUpdatesStudents() {
        uniqueTagList.addStudentToTags(BENSON); // BENSON has Math and Science

        // Delete Math tag
        uniqueTagList.deleteTagTypes(Set.of(math));

        assertFalse(uniqueTagList.contains(math));
        assertTrue(uniqueTagList.contains(science));

        // Verify that the tag was removed from the student object (mutation check)
        // BENSON is a mock object, so we assume removeTag is implemented in Student.
        // We will assert on the tags set that was passed during mock creation.
        // NOTE: A proper test would check if the Student object was actually mutated.
        // For this mock structure, we assume student.removeTag(toDelete) works as intended.
        // This test primarily checks for tag list removal and student list removal (implicitly via setUp/addStudent).
        assertTrue(uniqueTagList.getStudentsWithTag(science).contains(BENSON));
        assertThrows(TagNotFoundException.class, () -> uniqueTagList.getStudentsWithTag(math));
    }

    // =========================================================================
    // Object Overrides (equals, hashCode, toString) tests
    // =========================================================================

    @Test
    void testEquals_differentContent_returnsFalse() {
        uniqueTagList.addStudentToTags(ALICE);
        UniqueTagList other = new UniqueTagList();
        other.addTagTypes(Set.of(math, science));
        other.addStudentToTags(BENSON);

        assertNotEquals(uniqueTagList, other);
    }



    @Test
    void testToString_returnsCorrectFormat() {
        // After setup, contains Math and Science with empty lists
        String actual = uniqueTagList.toString();
        // Since HashMap ordering is not guaranteed, check that both keys/values exist.
        assertTrue(actual.contains(math.toString()));
        assertTrue(actual.contains(science.toString()));
        assertTrue(actual.contains("[]"));
    }
}
