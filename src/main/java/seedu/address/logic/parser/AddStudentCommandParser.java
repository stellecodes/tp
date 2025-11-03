package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Dictionary;
import java.util.Set;

import seedu.address.logic.commands.AddStudentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Role;
import seedu.address.model.person.Student;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddStudentCommand object
 */
public class AddStudentCommandParser extends AddCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the AddStudentCommand
     * and returns an AddStudentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddStudentCommand parse(String args) throws ParseException {
        StringBuilder wrongPrefixes = new StringBuilder();
        if (!args.contains("n/")) {
            wrongPrefixes.append("n/NAME ");
        }
        if (!args.contains("p/")) {
            wrongPrefixes.append("p/PHONE ");
        }
        if (!args.contains("e/")) {
            wrongPrefixes.append("e/EMAIL ");
        }
        if (!args.contains("a/")) {
            wrongPrefixes.append("a/ADDRESS ");
        }
        if (!wrongPrefixes.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    "Missing required fields: " + wrongPrefixes + "\n" + AddStudentCommand.MESSAGE_USAGE));
        }
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_REMARK, PREFIX_TAG);

        Dictionary<String, Object> fieldSet = setCommonFields(Role.STUDENT, argMultimap);

        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Student student = new Student((Name) fieldSet.get("name"), (Phone) fieldSet.get("phone"),
                (Email) fieldSet.get("email"), (Address) fieldSet.get("address"),
                (Remark) fieldSet.get("remark"), tagList);

        return new AddStudentCommand(student);
    }

}
