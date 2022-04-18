package me.deltaorion.common.test.command;

import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.FunctionalCommand;
import me.deltaorion.common.command.SingleActionCommand;
import me.deltaorion.common.command.sent.SentCommand;

public class PathCCommand extends FunctionalCommand {

    protected PathCCommand() {
        super("15","/testcommand pathc [pathca/pathcb]");
        registerArgument("PathCA", new SingleActionCommand() {
            @Override
            public void commandLogic(SentCommand command) throws CommandException {
                command.getSender().sendMessage("Took Path C-A");
                if(command.hasArg(0)) {
                    command.getSender().sendMessage(command.getArgOrFail(0));
                }
            }
        });

        registerArgument("PathCB", new FunctionalCommand("16") {
            @Override
            public void commandLogic(SentCommand command) throws CommandException {
                command.getSender().sendMessage("Took Path C-B");
            }
        });
    }


    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        command.getSender().sendMessage("Test Command Path C");
        for(String arg: getCommandArgs()) {
            command.getSender().sendMessage(" > "+arg);
        }
    }
}
