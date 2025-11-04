package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;
import seedu.address.model.tag.Tag;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "ClassConnect")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();

    private List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedLink> links = new ArrayList<>();
    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons) {
        this.persons.addAll(persons);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream()
                .map(this::identifyContactType)
                .collect(Collectors.toList()));

        tags.addAll(source.getTagList().getTags()
                .stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));


        if (source instanceof AddressBook) {
            AddressBook ab = (AddressBook) source;
            ab.getRelationshipGraph().getAllLinksAsPairs().forEach(
                    pair -> links.add(new JsonAdaptedLink(pair.getKey(), pair.getValue()))
            );
        }
    }

    /**
     * Helper function to be used with JsonSerializableAddressBook to use the appropriate JsonAdapterPerson class
     * @param p a Person object
     * @return an instance of the appropriate child class
     */
    private JsonAdaptedPerson identifyContactType(Person p) {
        if (p instanceof Student) {
            return new JsonAdaptedStudent((Student) p);
        } else {
            return new JsonAdaptedParent(p);
        }
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedPerson jsonPerson : persons) {
            Person p = jsonPerson.toModelType();
            if (addressBook.hasPerson(p)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(p);
        }
        Set<Tag> newTags = new HashSet<>();
        for (JsonAdaptedTag jsonTag : tags) {
            newTags.add(jsonTag.toModelType());
        }
        addressBook.addTagTypes(newTags);

        // REBUILD LINKS
        for (JsonAdaptedLink jsonLink : links) {
            Person a = addressBook.findPersonByName(jsonLink.getA());
            Person b = addressBook.findPersonByName(jsonLink.getB());
            if (a != null && b != null) {
                addressBook.linkPersons(a, b);
            }
        }

        return addressBook;
    }

}
