package seedu.address.storage;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;

import java.util.Dictionary;

public class JsonAdaptedParent extends JsonAdaptedPerson {

    public JsonAdaptedParent(String name, String phone, String email, String address, String remark) {
        super(name, phone, email, address, remark);
    }

    public JsonAdaptedParent(Person source) {
        super(source);
    }

    @Override
    public Person toModelType() throws IllegalValueException {

        Dictionary<String, Object> fieldSet = setCommonFields();

        return new Person((Name) fieldSet.get("modelName"),
                (Phone) fieldSet.get("modelPhone"),
                (Email) fieldSet.get("modelEmail"),
                (Address) fieldSet.get("modelAddress"),
                (Remark) fieldSet.get("modelRemark"));
    }
}
