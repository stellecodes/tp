package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.LinkCommand.MESSAGE_NOT_PARENT;
import static seedu.address.logic.commands.LinkCommand.MESSAGE_NOT_STUDENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_NAME;

import seedu.address.logic.commands.UnlinkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * Parses input arguments and creates a new {@code UnlinkCommand} object.
 * <p>
 * Ensures that sn/ refers to a {@code Student} contact
 * and pn/ refers to a {@code Parent} contact.
 */
public class UnlinkCommandParser implements Parser<UnlinkCommand> {

    private final Model model;

    /**
     * Constructs an {@code UnlinkCommandParser} with access to the current model.
     *
     * @param model The model used to resolve {@code Person} objects by name and phone.
     */
    public UnlinkCommandParser(Model model) {
        this.model = model;
    }

    @Override
    public UnlinkCommand parse(String args) throws ParseException {
        var argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_STUDENT_NAME, PREFIX_PARENT_NAME);

        if (!argMultimap.getValue(PREFIX_STUDENT_NAME).isPresent()
                || !argMultimap.getValue(PREFIX_PARENT_NAME).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnlinkCommand.MESSAGE_USAGE));
        }

        String studentName = argMultimap.getValue(PREFIX_STUDENT_NAME).get().trim();
        String parentName = argMultimap.getValue(PREFIX_PARENT_NAME).get().trim();

        Person student = findPersonByName(model, studentName);
        Person parent = findPersonByName(model, parentName);

        if (!(student instanceof Student)) {
            throw new ParseException(MESSAGE_NOT_STUDENT);
        }
        if (!(parent instanceof Parent)) {
            throw new ParseException(MESSAGE_NOT_PARENT);
        }

        return new UnlinkCommand(student, parent);
    }

    /**
     * Retrieves a Person by name (case-insensitive). Returns null if not found.
     */
    private Person findPersonByName(Model model, String name) {
        return model.getFilteredPersonList().stream()
                .filter(p -> p.getName().fullName.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
