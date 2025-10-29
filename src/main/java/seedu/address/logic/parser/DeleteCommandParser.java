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

        // Tokenize known prefixes; anything else stays in the preamble
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_EMAIL, PREFIX_PHONE);

        // Disallow duplicate identifiers like n/A n/B or e/... e/...
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_EMAIL, PREFIX_PHONE);

        // Raw values (keep as Optional<String> so ParserUtil can throw rich messages)
        Optional<String> rawName  = argMultimap.getValue(PREFIX_NAME);
        Optional<String> rawEmail = argMultimap.getValue(PREFIX_EMAIL);
        Optional<String> rawPhone = argMultimap.getValue(PREFIX_PHONE);
        boolean hasAnyIdentifier = rawName.isPresent() || rawEmail.isPresent() || rawPhone.isPresent();

        // Preamble is the only place an index can appear
        String preamble = argMultimap.getPreamble().trim();

        // Reject mixing index + identifiers
        if (!preamble.isEmpty() && hasAnyIdentifier) {
            // If you don't have this constant, replace with the usual invalid-format message.
            throw new ParseException(DeleteCommand.MESSAGE_EXCLUSIVE_FORMS);
        }

        // INDEX form
        if (!preamble.isEmpty()) {
            Index index = ParserUtil.parseIndex(preamble); // bubbles its own error messages
            return new DeleteCommand(index);
        }

        // IDENTIFIER form (must have at least one)
        if (!hasAnyIdentifier) {
            throw new ParseException(String.format(
                    Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        // Reject empty identifiers like n/, e/, p/
        if (rawName.isPresent()  && rawName.get().trim().isEmpty()
                || rawEmail.isPresent() && rawEmail.get().trim().isEmpty()
                || rawPhone.isPresent() && rawPhone.get().trim().isEmpty()) {
            throw new ParseException(String.format(
                    Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        // Parse with existing validators so detailed messages (email/phone/name constraints) show up
        Optional<Name>  name  = Optional.empty();
        Optional<Email> email = Optional.empty();
        Optional<Phone> phone = Optional.empty();

        if (rawName.isPresent()) {
            name = Optional.of(ParserUtil.parseName(rawName.get()));     // may throw ParseException
        }
        if (rawEmail.isPresent()) {
            email = Optional.of(ParserUtil.parseEmail(rawEmail.get()));  // detailed email constraints
        }
        if (rawPhone.isPresent()) {
            phone = Optional.of(ParserUtil.parsePhone(rawPhone.get()));  // detailed phone constraints
        }

        return new DeleteCommand(name, email, phone);
    }
}

