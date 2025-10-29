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

        ArgumentMultimap am = ArgumentTokenizer.tokenize(
                args, PREFIX_NAME, PREFIX_EMAIL, PREFIX_PHONE);

        // If there is a preamble, we treat it as the index form.
        // But if any identifier prefixes ALSO appear, reject as mixed usage.
        String preamble = am.getPreamble().trim();
        boolean hasName  = am.getValue(PREFIX_NAME).isPresent();
        boolean hasEmail = am.getValue(PREFIX_EMAIL).isPresent();
        boolean hasPhone = am.getValue(PREFIX_PHONE).isPresent();
        boolean hasAnyPrefix = hasName || hasEmail || hasPhone;

        if (!preamble.isEmpty()) {
            if (hasAnyPrefix) {
                throw new ParseException(DeleteCommand.MESSAGE_EXCLUSIVE_FORMS);
            }
            // index-only form
            Index index = ParserUtil.parseIndex(preamble);
            return new DeleteCommand(index);
        }

        // identifier-only form
        am.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_EMAIL, PREFIX_PHONE);

        Optional<String> rawName  = am.getValue(PREFIX_NAME);
        Optional<String> rawEmail = am.getValue(PREFIX_EMAIL);
        Optional<String> rawPhone = am.getValue(PREFIX_PHONE);

        // must have at least one identifier
        if (rawName.isEmpty() && rawEmail.isEmpty() && rawPhone.isEmpty()) {
            throw new ParseException(String.format(
                    Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        // reject empty values like n/, e/, p/
        if (rawName.isPresent() && rawName.get().trim().isEmpty()
                || rawEmail.isPresent() && rawEmail.get().trim().isEmpty()
                || rawPhone.isPresent() && rawPhone.get().trim().isEmpty()) {
            throw new ParseException(String.format(
                    Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        // Parse to domain objects (will validate formats)
        Optional<Name>  name  = rawName.map(s -> {
            try { return ParserUtil.parseName(s); } catch (ParseException e) { throw new RuntimeException(e); }
        });
        Optional<Email> email = rawEmail.map(s -> {
            try { return ParserUtil.parseEmail(s); } catch (ParseException e) { throw new RuntimeException(e); }
        });
        Optional<Phone> phone = rawPhone.map(s -> {
            try { return ParserUtil.parsePhone(s); } catch (ParseException e) { throw new RuntimeException(e); }
        });

        // Re-throw any parse failures cleanly (unwrap the RuntimeException wrappers)
        try {
            return new DeleteCommand(name, email, phone);
        } catch (RuntimeException re) {
            if (re.getCause() instanceof ParseException pe) throw pe;
            throw re;
        }
    }
}

