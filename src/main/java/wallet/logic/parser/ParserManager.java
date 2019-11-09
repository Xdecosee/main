package wallet.logic.parser;

import wallet.exception.InsufficientParameters;
import wallet.logic.command.*;

import java.text.ParseException;


/**
 * The ParserManager Class that handles all CommandParser classes.
 */
public class ParserManager {

    /**
     * Parses the user input command and returns the corresponding Command object.
     *
     * @param fullCommand The input of user.
     * @return The corresponding Command object.
     */
    public Command parseCommand(String fullCommand) throws ParseException {
        String[] arguments = fullCommand.split(" ", 2);

        switch (arguments[0]) {
        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments[1].toLowerCase());

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments[1].toLowerCase());

        case ListCommand.COMMAND_WORD:
            return new ListCommandParser().parse(arguments[1].toLowerCase());

        case ViewCommand.COMMAND_WORD:
            try {
                return new ViewCommandParser().parse(arguments[1].toLowerCase());
            } catch (ArrayIndexOutOfBoundsException err) {
                throw new InsufficientParameters("view command currently has no parameters!");
            }

        case SetBudgetCommand.COMMAND_WORD:
            try {
                return new SetBudgetParser().parse(arguments[1].toLowerCase());
            } catch (ArrayIndexOutOfBoundsException err) {
                throw new InsufficientParameters("budget command currently has no parameters!");
            }

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments[1].toLowerCase());

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case ReminderCommand.COMMAND_WORD:
            return new ReminderCommandParser().parse(arguments[1].toLowerCase());

        case DoneCommand.COMMAND_WORD:
            try {
                return new DoneCommandParser().parse(arguments[1].toLowerCase());
            } catch (ArrayIndexOutOfBoundsException err) {
                throw new InsufficientParameters("done command currently has no parameters!");
            }

        case ExportCommand.COMMAND_WORD:
            return new ExportCommandParser().parse(arguments[1].toLowerCase());

        case ImportCommand.COMMAND_WORD:
            return new ImportCommandParser().parse(arguments[1].toLowerCase());

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand(arguments[1]);

        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommand();

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        case GenerateCommand.COMMAND_WORD:
            return new GenerateCommand();

        case CurrencyCommand.COMMAND_WORD:
            try {
                return new CurrencyParser().parse(arguments[1].toLowerCase());
            } catch (ArrayIndexOutOfBoundsException err) {
                throw new InsufficientParameters("currency command currently has no parameters!");
            }

        default:
            return null;
        }
    }
}
