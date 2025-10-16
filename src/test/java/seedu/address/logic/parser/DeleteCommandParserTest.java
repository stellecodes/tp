package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class DeleteCommandParserTest {

    private final DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validIndex_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new DeleteCommand(seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nameOnly_returnsCommand() {
        DeleteCommand expected = new DeleteCommand(
                Optional.of(new Name("Alice Tan")),
                Optional.empty(),
                Optional.empty());

        assertParseSuccess(parser, " n/Alice Tan", expected);
    }

    @Test
    public void parse_emailOnly_returnsCommand() {
        DeleteCommand expected = new DeleteCommand(
                Optional.empty(),
                Optional.of(new Email("alice@example.com")),
                Optional.empty());

        assertParseSuccess(parser, " e/alice@example.com", expected);
    }

    @Test
    public void parse_phoneOnly_returnsCommand() {
        DeleteCommand expected = new DeleteCommand(
                Optional.empty(),
                Optional.empty(),
                Optional.of(new Phone("91234567")));

        assertParseSuccess(parser, " p/91234567", expected);
    }

    @Test
    public void parse_nameAndEmail_returnsCommand() {
        DeleteCommand expected = new DeleteCommand(
                Optional.of(new Name("Alice Tan")),
                Optional.of(new Email("alice@example.com")),
                Optional.empty());

        assertParseSuccess(parser, " n/Alice Tan e/alice@example.com", expected);
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
}
