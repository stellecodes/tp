package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Optional;

import seedu.address.logic.commands.FindLinkCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@link FindLinkCommand}.
 * Accepts exactly one prefix: n/
 */
public class FindLinkCommandParser implements Parser<FindLinkCommand> {

    @Override
    public FindLinkCommand parse(String args) throws ParseException {
        requireNonNull(args);

        // Tokenizes arguments using the shared CLI tokenizer and the n/ prefix from CliSyntax
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_NAME);

        // Must have no preamble or free text and exactly one n/
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindLinkCommand.MESSAGE_USAGE));
        }

        // Requires exactly one n/ with a non-empty value.
        Optional<String> nameOpt = argMultimap.getValue(CliSyntax.PREFIX_NAME);
        if (nameOpt.isEmpty() || nameOpt.get().trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindLinkCommand.MESSAGE_USAGE));
        }

        // Rejects duplicated n/ and creates the command.
        argMultimap.verifyNoDuplicatePrefixesFor(CliSyntax.PREFIX_NAME);
        return new FindLinkCommand(nameOpt.get());
    }
}

