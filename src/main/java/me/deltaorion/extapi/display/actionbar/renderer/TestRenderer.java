package me.deltaorion.extapi.display.actionbar.renderer;

import me.deltaorion.extapi.display.actionbar.ActionBarRenderer;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class TestRenderer implements ActionBarRenderer {

    private final List<String> list;
    private final CountDownLatch renderLatch;

    public TestRenderer(List<String> list, CountDownLatch renderLatch) {
        this.list = list;
        this.renderLatch = renderLatch;
    }

    @Override
    public void render(@NotNull Player player, @NotNull String toRender) {
        list.add(toRender);
        if(list.size()>3) {
            throw new RuntimeException("Animation Failed to cancel! Action Bar Test Failed!");
        }
        if(renderLatch.getCount()>0) {
            renderLatch.countDown();
        }
    }
}
