package me.deltaorion.bukkit.command.tabcompletion;

import me.deltaorion.bukkit.item.EMaterial;
import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.sent.SentCommand;
import me.deltaorion.common.command.tabcompletion.CompletionSupplier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CompletersBukkit {

    private CompletersBukkit() {
        throw new UnsupportedOperationException();
    }

    public static CompletionSupplier BUKKIT_PLAYERS() {
        return new CompletionSupplier() {
            @Override
            public List<String> getCompletions(SentCommand command) throws CommandException {
                List<String> completion = new ArrayList<>();
                for(Player player : Bukkit.getOnlinePlayers()) {
                    completion.add(player.getName());
                }
                return completion;
            }
        };
    }

    public static CompletionSupplier EMATERIAL() {
        return new CompletionSupplier() {
            @Override
            public List<String> getCompletions(SentCommand command) throws CommandException {
                List<String> completions = new ArrayList<>();
                for(EMaterial material : EMaterial.valuesThisVersion()) {
                    completions.add(material.name().toLowerCase(Locale.ROOT));
                }
                return completions;
            }
        };
    }

    public static CompletionSupplier EMATERIAL_ALL_VERSION() {
        return new CompletionSupplier() {
            @Override
            public List<String> getCompletions(SentCommand command) throws CommandException {
                List<String> completions = new ArrayList<>();
                for(EMaterial material : EMaterial.values()) {
                    completions.add(material.name().toLowerCase(Locale.ROOT));
                }
                return completions;
            }
        };
    }

    public static CompletionSupplier MATERIAL() {
        return new CompletionSupplier() {
            @Override
            public List<String> getCompletions(SentCommand command) throws CommandException {
                List<String> completions = new ArrayList<>();
                for(Material material : Material.values()) {
                    completions.add(material.name().toLowerCase(Locale.ROOT));
                }
                return completions;
            }
        };
    }


}
