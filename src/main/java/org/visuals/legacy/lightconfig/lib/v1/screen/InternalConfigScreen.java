package org.visuals.legacy.lightconfig.lib.v1.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.visuals.legacy.lightconfig.lib.v1.Config;

public class InternalConfigScreen extends Screen {
    private final Screen parent;
    private final Config config;

    public InternalConfigScreen(Component title, Config config, Screen parent) {
        super(title);
        this.parent = parent;
        this.config = config;
    }

    @Override
    protected void init() {
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void onClose() {
        this.config.save();
        this.minecraft.setScreen(this.parent);
    }
}
