package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/**
 * Parses input arguments and creates a new DeleteCommand object.
 *
 * Supports:
 *   - delete INDEX
 *   - delete [n/NAME] [e/EMAIL] [p/PHONE]   (AND semantics; at least one required)
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    @Override
    public DeleteCommand parse(String args) throws ParseException {
        requireNonNull(args);

        // Tokenize by the 3 prefixes. Anything not captured by a prefix remains in the preamble.
        // splits by n/, e/, p/
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_EMAIL, PREFIX_PHONE);

        boolean hasName = argMultimap.getValue(PREFIX_NAME).isPresent();
        boolean hasEmail = argMultimap.getValue(PREFIX_EMAIL).isPresent();
        boolean hasPhone = argMultimap.getValue(PREFIX_PHONE).isPresent();
        boolean hasAnyPrefix = hasName || hasEmail || hasPhone;

        // If no prefixes are present, assume legacy 'delete INDEX'.
        if (!hasAnyPrefix) {
            String trimmed = args.trim();
            if (trimmed.isEmpty()) {
                throw new ParseException(
                        String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
            try {
                Index index = ParserUtil.parseIndex(trimmed);
                return new DeleteCommand(index);
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE)
                );
            }
        }

        // Attribute-based delete: unwrap values and let ParserUtil validate/construct domain objects.
        Optional<Name> name = Optional.empty();
        Optional<Email> email = Optional.empty();
        Optional<Phone> phone = Optional.empty();

        // utilize existing validation rules to check for valid name / email / number
        if (hasName) {
            String raw = argMultimap.getValue(PREFIX_NAME).get();
            name = Optional.of(ParserUtil.parseName(raw));
        }
        if (hasEmail) {
            String raw = argMultimap.getValue(PREFIX_EMAIL).get();
            email = Optional.of(ParserUtil.parseEmail(raw));
        }
        if (hasPhone) {
            String raw = argMultimap.getValue(PREFIX_PHONE).get();
            phone = Optional.of(ParserUtil.parsePhone(raw));
        }

        if (name.isEmpty() && email.isEmpty() && phone.isEmpty()) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return new DeleteCommand(name, email, phone);
    }
}
