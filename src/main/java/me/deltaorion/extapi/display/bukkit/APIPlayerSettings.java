package me.deltaorion.extapi.display.bukkit;

import me.deltaorion.extapi.display.actionbar.ActionBarFactories;
import me.deltaorion.extapi.display.actionbar.ActionBarFactory;
import me.deltaorion.extapi.display.actionbar.renderer.PacketActionBarRenderer;

public class APIPlayerSettings {

    private ActionBarFactory factory;

    public APIPlayerSettings() {
        this.factory = ActionBarFactories.SCHEDULE(new PacketActionBarRenderer());
    }

    public APIPlayerSettings setFactory(ActionBarFactory factory) {
        this.factory = factory;
        return this;
    }

    public ActionBarFactory getFactory() {
        return factory;
    }
}
