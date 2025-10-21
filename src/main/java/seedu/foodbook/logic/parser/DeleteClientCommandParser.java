package seedu.foodbook.logic.parser;

import static seedu.foodbook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.foodbook.logic.commands.DeleteClientCommand;
import seedu.foodbook.logic.parser.exceptions.ParseException;
import seedu.foodbook.model.person.Name;

/**
 * Parses input arguments and creates a new DeleteClientCommand object
 */
public class DeleteClientCommandParser implements Parser<DeleteClientCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteClientCommand
     * and returns a DeleteClientCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteClientCommand parse(String args) throws ParseException {
        try {
            Name toBeDeleted = ParserUtil.parseName(args);
            return new DeleteClientCommand(toBeDeleted);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteClientCommand.MESSAGE_USAGE), pe);
        }
    }

}
