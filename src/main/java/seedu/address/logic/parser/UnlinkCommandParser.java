package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.UnlinkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Parses input arguments and creates a new UnlinkCommand object.
 */
public class UnlinkCommandParser implements Parser<UnlinkCommand> {

    private static final Prefix PREFIX_STUDENT_NAME = new Prefix("sn/");
    private static final Prefix PREFIX_STUDENT_PHONE = new Prefix("sp/");
    private static final Prefix PREFIX_PARENT_NAME = new Prefix("pn/");
    private static final Prefix PREFIX_PARENT_PHONE = new Prefix("pp/");

    private final Model model;

    public UnlinkCommandParser(Model model) {
        this.model = model;
    }

    @Override
    public UnlinkCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_STUDENT_NAME, PREFIX_STUDENT_PHONE,
                        PREFIX_PARENT_NAME, PREFIX_PARENT_PHONE);

        if (!argMultimap.getValue(PREFIX_STUDENT_NAME).isPresent()
                || !argMultimap.getValue(PREFIX_PARENT_NAME).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnlinkCommand.MESSAGE_USAGE));
        }

        String studentName = argMultimap.getValue(PREFIX_STUDENT_NAME).get().trim();
        String studentPhone = argMultimap.getValue(PREFIX_STUDENT_PHONE).orElse("").trim();
        String parentName = argMultimap.getValue(PREFIX_PARENT_NAME).get().trim();
        String parentPhone = argMultimap.getValue(PREFIX_PARENT_PHONE).orElse("").trim();

        Person student = resolvePerson(studentName, studentPhone);
        Person parent = resolvePerson(parentName, parentPhone);

        if (student == null || parent == null) {
            throw new ParseException(UnlinkCommand.MESSAGE_NOT_FOUND);
        }

        return new UnlinkCommand(student, parent);
    }

    /** Finds a unique Person by name and (optionally) phone number. */
    private Person resolvePerson(String name, String phone) {
        var allPersons = model.getFilteredPersonList();
        var nameMatches = allPersons.stream()
                .filter(p -> p.getName().fullName.equalsIgnoreCase(name))
                .toList();

        if (nameMatches.isEmpty()) {
            return null;
        }
        if (nameMatches.size() == 1) {
            return nameMatches.get(0);
        }
        if (!phone.isEmpty()) {
            return nameMatches.stream()
                    .filter(p -> p.getPhone().value.equals(phone))
                    .findFirst().orElse(null);
        }
        return null;
    }
}
