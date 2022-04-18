package me.deltaorion.common.test.command;

import me.deltaorion.common.APIPermissions;
import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.FunctionalCommand;
import me.deltaorion.common.command.sent.SentCommand;
import net.jcip.annotations.GuardedBy;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ShouldBeRunAsyncCommand extends FunctionalCommand {

    @GuardedBy("this") private final Set<UUID> running;

    public ShouldBeRunAsyncCommand() {
        super(APIPermissions.COMMAND,NO_USAGE);
        this.running = new HashSet<>();
    }

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        synchronized (running) {
            if(running.contains(command.getSender().getUniqueId())) {
                command.getSender().sendMessage("This command is already running. Please wait!");
                return;
            } else {
                running.add(command.getSender().getUniqueId());
            }
        }
        command.getSender().sendMessage("Preparing HUGE Calculation!");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            synchronized (running) {
                running.remove(command.getSender().getUniqueId());
            }
            command.getSender().sendMessage("Calculation Interrupted from Server Shutdown");
            Thread.currentThread().interrupt();
            return;
        }

        command.getSender().sendMessage("The meaning of Life the Universe and Everything is 42");

        synchronized (running) {
            running.remove(command.getSender().getUniqueId());
        }
    }
}
