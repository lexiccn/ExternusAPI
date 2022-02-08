package me.deltaorion.extapi.display.actionbar.renderer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.deltaorion.extapi.display.actionbar.ActionBarRenderer;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.locale.message.Message;

import java.lang.reflect.InvocationTargetException;

public class PacketActionBarRenderer implements ActionBarRenderer {

    @Override
    public void render(BukkitApiPlayer player, Message message, Object... args) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.CHAT);

        packet.getChatComponents().write(0, WrappedChatComponent.fromText(message.toString(player.getLocale(),args)));
        packet.getBytes().write(0, EnumWrappers.ChatType.GAME_INFO.getId());

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player.getPlayer(), packet);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
