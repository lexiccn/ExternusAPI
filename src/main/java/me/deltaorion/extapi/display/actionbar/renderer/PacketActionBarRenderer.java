package me.deltaorion.extapi.display.actionbar.renderer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.deltaorion.extapi.display.actionbar.ActionBarRenderer;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.protocol.WrapperPlayServerChat;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class PacketActionBarRenderer implements ActionBarRenderer {

    @Override
    public void render(@NotNull BukkitApiPlayer player, @NotNull String toRender) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.CHAT);
        //create chat packet with the wrapped UTF-8 Chat component
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(toRender));
        //let it know that this is an action bar
        packet.getBytes().write(0, EnumWrappers.ChatType.GAME_INFO.getId());

        //send it, rethrow the exception to cancel the running action bar
        Player p = player.getPlayer();


        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
