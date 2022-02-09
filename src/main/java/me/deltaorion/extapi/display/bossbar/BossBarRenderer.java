package me.deltaorion.extapi.display.bossbar;

import org.jetbrains.annotations.NotNull;

public interface BossBarRenderer {

    public void setMessage(@NotNull String render);

    public void setProgress(float progress);

    public void setVisible(boolean visible);

    public void update();
}
