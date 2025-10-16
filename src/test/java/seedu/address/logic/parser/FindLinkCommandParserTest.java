package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindLinkCommand;

public class FindLinkCommandParserTest {

    private final FindLinkCommandParser parser = new FindLinkCommandParser();

    @Test
    public void parse_validArgs_returnsCommand() {
        // exact spacing
        assertParseSuccess(parser, " n/John Tan",
                new FindLinkCommand("John Tan"));

        // leading/trailing whitespace around the whole input is OK
        assertParseSuccess(parser, "   n/John Tan   ",
                new FindLinkCommand("John Tan"));

        // name with internal multiple spaces is preserved
        assertParseSuccess(parser, " n/John   Tan",
                new FindLinkCommand("John   Tan"));
    }

    @Test
    public void parseTrailingGarbage_treatedAsPartOfName_success() {
        // The parser does not reject trailing “unexpected” text — it becomes part of the name.
        assertParseSuccess(parser, " n/ John Tan unexpected",
                new FindLinkCommand("John Tan unexpected"));
    }

    @Test
    public void parse_missingPrefix_failure() {
        // No n/ prefix at all
        assertParseFailure(parser, " John Tan",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindLinkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyName_failure() {
        // Only the prefix, no content
        assertParseFailure(parser, " n/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindLinkCommand.MESSAGE_USAGE));

        // Only the prefix with spaces after it
        assertParseFailure(parser, " n/   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindLinkCommand.MESSAGE_USAGE));
    }
}
