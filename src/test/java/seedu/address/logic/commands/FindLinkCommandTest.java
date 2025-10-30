package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Integration-style tests for {@link FindLinkCommand} using a purpose-built
 * Model stub.
 */
public class FindLinkCommandTest {

    private Student alice; // student
    private Parent mary; // parent
    private Parent tom; // parent
    private Student bob; // student (unlinked)
    private ModelStubWithPersons model;

    @BeforeEach
    public void setUp() {
        // Build persons using your test builder (tags only on students as per your refactor)
        alice = new PersonBuilder().withName("Alice Tan").withPhone("91234567")
                .withEmail("alice@example.com").withAddress("12 Clementi Rd")
                .withTags("sec2").buildStudent();

        bob = new PersonBuilder().withName("Bob Lee").withPhone("93334444")
                .withEmail("bob@example.com").withAddress("45 Pasir Ris Dr 3")
                .withTags("sec3").buildStudent();

        mary = new PersonBuilder().withName("Mary Tan").withPhone("90001111")
                .withEmail("mary@example.com").withAddress("8 Holland Ave").buildParent();

        tom = new PersonBuilder().withName("Tom Tan").withPhone("98887777")
                .withEmail("tom@example.com").withAddress("77 Ang Mo Kio Ave").buildParent();

        // Model with all four people added
        model = new ModelStubWithPersons(alice, bob, mary, tom);

        // Establish links: Alice <-> Mary, Alice <-> Tom (Bob remains unlinked)
        model.link(alice, mary);
        model.link(alice, tom);
    }

    // Positive behaviours

    @Test
    public void execute_studentHasTwoLinkedParents_filtersToTwo() throws Exception {
        FindLinkCommand cmd = new FindLinkCommand("Alice Tan");
        CommandResult result = cmd.execute(model);

        // Filtered list should be Mary's and Tom's cards (order not guaranteed)
        ObservableList<Person> filtered = model.getFilteredPersonList();
        assertEquals(2, filtered.size());
        assertTrue(filtered.contains(mary));
        assertTrue(filtered.contains(tom));

        // Message should start with count and include Alice's summary (Messages.format)
        assertTrue(result.getFeedbackToUser().startsWith("Showing 2 linked contact(s)"));
        assertTrue(result.getFeedbackToUser().contains(alice.getName().fullName));
    }

    @Test
    public void execute_parentShowsLinkedChildren_filtersToOne() throws Exception {
        FindLinkCommand cmd = new FindLinkCommand("Tom Tan");
        CommandResult result = cmd.execute(model);

        ObservableList<Person> filtered = model.getFilteredPersonList();
        assertEquals(1, filtered.size());
        assertTrue(filtered.contains(alice)); // Tom -> Alice

        String expectedPrefix = "Showing 1 linked contact(s)";
        assertTrue(result.getFeedbackToUser().startsWith(expectedPrefix));
        assertTrue(result.getFeedbackToUser().contains(tom.getName().fullName));
    }

    @Test
    public void execute_nameMatching_isCaseInsensitive() throws Exception {
        FindLinkCommand cmd = new FindLinkCommand("aLiCe tAn"); // weird casing
        cmd.execute(model);

        ObservableList<Person> filtered = model.getFilteredPersonList();
        assertEquals(2, filtered.size());
        assertTrue(filtered.contains(mary));
        assertTrue(filtered.contains(tom));
    }

    // Edge / negative behaviours

    @Test
    public void execute_targetFoundButHasNoLinks_showsZero() throws Exception {
        // Bob exists but is unlinked
        FindLinkCommand cmd = new FindLinkCommand("Bob Lee");
        CommandResult result = cmd.execute(model);

        ObservableList<Person> filtered = model.getFilteredPersonList();
        assertEquals(0, filtered.size());

        String expectedPrefix = "Showing 0 linked contact(s)";
        assertTrue(result.getFeedbackToUser().startsWith(expectedPrefix));
        assertTrue(result.getFeedbackToUser().contains(bob.getName().fullName));
    }

    @Test
    public void execute_targetNotFound_throwsCommandException() {
        FindLinkCommand cmd = new FindLinkCommand("nonexisTsEnt");

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals("No person found with the name: nonexisTsEnt", ex.getMessage());

        // Optional: ensure list not filtered on failure (remains 'show all' in our stub)
        assertEquals(4, model.getFilteredPersonList().size());
    }

    // Model stub

    /**
     * A concrete Model that stores persons and links in-memory, implementing just
     * the methods that FindLinkCommand relies on. All other methods are no-ops.
     */
    private static class ModelStubWithPersons implements Model {
        private final AddressBook addressBook = new AddressBook();
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final FilteredList<Person> filtered = new FilteredList<>(persons);
        private final Map<Person, Set<Person>> links = new HashMap<>();
        private final UserPrefs prefs = new UserPrefs();

        ModelStubWithPersons(Person... initial) {
            for (Person p : initial) {
                persons.add(p);
                addressBook.addPerson(p);
                links.putIfAbsent(p, new HashSet<>());
            }
            filtered.setPredicate(PREDICATE_SHOW_ALL_PERSONS);
        }

        // Linking
        @Override
        public boolean link(Person a, Person b) {
            links.putIfAbsent(a, new HashSet<>());
            links.putIfAbsent(b, new HashSet<>());
            boolean addedA = links.get(a).add(b);
            boolean addedB = links.get(b).add(a);
            return addedA || addedB;
        }

        @Override
        public boolean unlink(Person a, Person b) {
            boolean r1 = links.containsKey(a) && links.get(a).remove(b);
            boolean r2 = links.containsKey(b) && links.get(b).remove(a);
            return r1 || r2;
        }

        @Override
        public List<Person> getLinkedPersons(Person person) {
            return new ArrayList<>(links.getOrDefault(person, Collections.emptySet()));
        }

        // Filtering
        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return filtered;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            filtered.setPredicate(predicate);
        }

        // AddressBook access used by FindLinkCommand.findPersonByNameIgnoreCase
        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return addressBook;
        }

        // Minimal implementations (expanded to satisfy LeftCurly rule)
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            return prefs;
        }

        @Override
        public GuiSettings getGuiSettings() {
            return prefs.getGuiSettings();
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            prefs.setGuiSettings(guiSettings);
        }

        @Override
        public Path getAddressBookFilePath() {
            return prefs.getAddressBookFilePath();
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            prefs.setAddressBookFilePath(addressBookFilePath);
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
        }

        @Override
        public boolean hasPerson(Person person) {
            return persons.contains(person);
        }

        @Override
        public void deletePerson(Person target) {
            persons.remove(target);
        }

        @Override
        public void addPerson(Person person) {
            persons.add(person);
            addressBook.addPerson(person);
            links.putIfAbsent(person, new HashSet<>());
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
        }

        @Override
        public Set<Tag> getTags() {
            return new HashSet<>();
        }

        @Override
        public boolean personHasValidTags(Person p) {
            return true;
        }

        @Override
        public boolean hasTag(Tag tag) {
            return false;
        }

        @Override
        public void addTagTypes(Set<Tag> tags) {
        }

        @Override
        public void deleteTagTypes(Set<Tag> tagsDelete) {
        }
    }
}
