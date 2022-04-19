package me.deltaorion.bukkit.test.command_old;

import me.deltaorion.bukkit.item.EMaterial;
import me.deltaorion.common.APIPermissions;
import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.sent.CommandArg;
import me.deltaorion.common.command.sent.MessageErrors;
import me.deltaorion.common.plugin.ApiPlugin;
import me.deltaorion.common.test.mock.TestEnum;
import me.deltaorion.common.test.mock.TestSender;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerParseCommand implements CommandExecutor {

    private final ApiPlugin plugin;

    public PlayerParseCommand(ApiPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission(APIPermissions.COMMAND)) {
            sender.sendMessage(MessageErrors.NO_PERMISSION().toString(APIPermissions.COMMAND));
            return true;
        }
        String arg = args.length == 0 ? "DeltaOrion" : args[0];
        CommandArg cArg = new CommandArg(new TestSender(),plugin,arg,0);
        sender.sendMessage("Parsed: "+cArg.parseOrDefault(Player.class,null));
        sender.sendMessage("Parse Fails");
        CommandArg failArg = new CommandArg(new TestSender() ,plugin,arg,0);
        try {
            sender.sendMessage("Result: "+failArg.asInt());
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
        }

        try {
            sender.sendMessage("Result: "+failArg.asFloat());
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
        }


        try {
            sender.sendMessage("Result: "+failArg.asDouble());
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
        }

        try {
            sender.sendMessage("Result: "+failArg.asBoolean());
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
        }

        try {
            sender.sendMessage("Result: "+failArg.asEnum(TestEnum.class,"Test Enum"));
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
        }

        try {
            sender.sendMessage("Result: "+failArg.asDuration());
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
        }

        try {
            sender.sendMessage("Result: "+failArg.parse(TestEnum.class));
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
        }

        try {
            sender.sendMessage("Result: "+failArg.parse(EMaterial.class));
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
        }

        try {
            sender.sendMessage("Result: "+failArg.parse(Material.class));
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
        }

        try {
            sender.sendMessage("Result: "+failArg.parse(Player.class));
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
        }

        return true;
    }
}
