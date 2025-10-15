package seedu.address.storage;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Student;
import seedu.address.model.tag.Tag;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonAdaptedStudent extends JsonAdaptedPerson {

    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedStudent} with the given student details.
     */
    @JsonCreator
    public JsonAdaptedStudent(@JsonProperty("name") String name,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email,
                             @JsonProperty("address") String address,
                             @JsonProperty("remark") String remark,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        super(name, phone, email, address, remark);
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Student} into this class for Jackson use.
     */
    public JsonAdaptedStudent(Student source) {
        super(source.getName().fullName, source.getPhone().value, source.getEmail().value,
                source.getAddress().value, source.getRemark().remarks);
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    @Override
    public Person toModelType() throws IllegalValueException {

        Dictionary<String, Object> fieldSet = setCommonFields();

        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }
        final Set<Tag> modelTags = new HashSet<>(personTags);
        return new Student((Name) fieldSet.get("modelName"),
                (Phone) fieldSet.get("modelPhone"),
                (Email) fieldSet.get("modelEmail"),
                (Address) fieldSet.get("modelAddress"),
                (Remark) fieldSet.get("modelRemark"), modelTags);
    }
}

