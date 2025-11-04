package org.visuals.legacy.lightconfig.lib.v1.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.visuals.legacy.lightconfig.lib.v1.Config;

public class ConfigScreenBuilder {
    private final Config config;
    private Component title = Component.empty();

    private ConfigScreenBuilder(Config config) {
        this.config = config;
    }

    public static ConfigScreenBuilder builder(Config config) {
        return new ConfigScreenBuilder(config);
    }

    // TODO

    public ConfigScreenBuilder setTitle(Component title) {
        this.title = title;
        return this;
    }

    public Screen build(@Nullable Screen parent) {
        return new InternalConfigScreen(this.title, this.config, parent);
    }
}
