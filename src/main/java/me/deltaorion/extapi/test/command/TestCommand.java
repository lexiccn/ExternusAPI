package me.deltaorion.extapi.test.command;

import me.deltaorion.extapi.command.Command;
import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.FunctionalCommand;
import me.deltaorion.extapi.command.SingleActionCommand;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.command.tabcompletion.Completers;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.locale.message.Message;
import org.bukkit.Material;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class TestCommand extends FunctionalCommand {

    public TestCommand() {
        super("5","/testcommand <integer> <boolean> <float> <material> <duration>", Message.valueOf("Test Command Logic"));
        registerArgument("PathA", new SingleActionCommand() {
            @Override
            public void commandLogic(SentCommand command) {
                command.getSender().sendMessage("You just took Path A!");
            }
        });

        registerArgument("PathB", new FunctionalCommand("10") {
            @Override
            public void commandLogic(SentCommand command) throws CommandException {
                command.getSender().sendMessage("You just took Path B! This should only work if you have the right permission!");
            }
        });

        registerArgument("PathC",new PathCCommand());

        registerArgument("help", new SingleActionCommand() {
            @Override
            public void commandLogic(SentCommand command) {
                printHelpMenu(command.getSender());
            }
        });

        registerCompleter(1, Completers.INTEGERS(0,10));
        registerCompleter(2,Completers.BOOLEANS());
        registerCompleter(3,Completers.FLOATS(0,1,0.1,2));
        registerCompleter(4,Completers.ENUMS(Material.class));
    }

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        if(!command.hasArg(0)) {
            printHelpMenu(command.getSender());
            return;
        }

        command.getSender().sendMessage("Integer: "+command.getArgOrBlank(0).asIntOrDefault(0));
        command.getSender().sendMessage("Boolean: "+command.getArgOrBlank(1).asBooleanOrDefault(true));
        command.getSender().sendMessage("Float: "+command.getArgOrBlank(2).asDoubleOrDefault(3.1415));
        command.getSender().sendMessage("Material: "+command.getArgOrBlank(3).asEnumOrDefault(Material.class,Material.COMMAND));
        command.getSender().sendMessage("Duration: "+command.getArgOrBlank(4).asDurationOrDefault(Duration.of(3, ChronoUnit.DAYS)));
    }

    private void printHelpMenu(Sender sender) {
        sender.sendMessage("TestCommand Help");
        for(Map.Entry<String, Command> entry : getFunctions().entrySet()) {
            sender.sendMessage(" > " + entry.getKey() + ": "+entry.getValue().getDescription());
        }
    }

}
