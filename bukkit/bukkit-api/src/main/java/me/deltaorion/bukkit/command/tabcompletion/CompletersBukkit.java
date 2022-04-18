package me.deltaorion.bukkit.command.tabcompletion;

import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.sent.SentCommand;
import me.deltaorion.common.command.tabcompletion.CompletionSupplier;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CompletersBukkit {

    private CompletersBukkit() {
        throw new UnsupportedOperationException();
    }

    public static CompletionSupplier BUKKIT_PLAYERS(Server server) {
        return new CompletionSupplier() {
            @Override
            public List<String> getCompletions(SentCommand command) throws CommandException {
                List<String> completion = new ArrayList<>();
                for(Player player : server.getOnlinePlayers()) {
                    completion.add(player.getName());
                }
                return completion;
            }
        };
    }
}
