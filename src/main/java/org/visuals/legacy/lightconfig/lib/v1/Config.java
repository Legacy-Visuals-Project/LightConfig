/**
 * LightConfig
 * A config library.
 * <p>
 * Copyright (C) 2025 lowercasebtw
 * Copyright (C) 2025 mixces
 * Copyright (C) 2025 Contributors to the project retain their copyright
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * "MINECRAFT" LINKING EXCEPTION TO THE GPL
 */

package org.visuals.legacy.lightconfig.lib.v1;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.visuals.legacy.lightconfig.lib.v1.field.*;
import org.visuals.legacy.lightconfig.lib.v1.serializer.ConfigSerializer;
import org.visuals.legacy.lightconfig.lib.v1.serializer.JsonSerializer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class Config {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final List<AbstractConfigField<?>> configFields = new ArrayList<>();
    protected final ModContainer modContainer;
    protected final Path path;
    protected final ConfigSerializer<?> serializer;

    public Config(ModContainer modContainer, Path path, ConfigSerializer<?> serializer) {
        this.modContainer = modContainer;
        this.path = path;
        this.serializer = serializer;
        if (!(this.serializer instanceof JsonSerializer)) {
            throw new RuntimeException("Only json serialization is currently supported! Please use JsonSerializer!");
        }
    }

    public Config(ModContainer modContainer, Path path) {
        this(modContainer, path, null);
    }

    public BooleanConfigField booleanFieldOf(final String name, final boolean defaultValue) {
        final BooleanConfigField field = new BooleanConfigField(this, name, defaultValue);
        this.configFields.add(field);
        return field;
    }

    public StringConfigField stringFieldOf(final String name, final String defaultValue) {
        final StringConfigField field = new StringConfigField(this, name, defaultValue);
        this.configFields.add(field);
        return field;
    }

    public <T extends Number> NumericConfigField<T> numericFieldOf(final String name, final T defaultValue) {
        final NumericConfigField<T> field = new NumericConfigField<>(this, name, defaultValue);
        this.configFields.add(field);
        return field;
    }

    public <T extends Enum<T>> EnumConfigField<T> enumFieldOf(final String name, final T defaultValue, final Class<T> clazz) {
        final EnumConfigField<T> field = new EnumConfigField<>(this, name, defaultValue, clazz);
        this.configFields.add(field);
        return field;
    }

    public void load() {
        if (!this.path.toFile().exists()) {
            this.logger.info("Config file doesn't exist! Creating one...");
            this.save();
            return;
        }

        boolean success = true;
        try {
            final String json = Files.readString(this.path);
            final JsonObject object = ((JsonElement) this.serializer.deserialize(json)).getAsJsonObject();
            if (object == null) {
                this.logger.warn("Failed to load config! Defaulting to original settings.");
                success = false;
            } else {
                this.configFields.forEach(field -> {
                    try {
                        field.load(object);
                    } catch (Exception exception) {
                        this.logger.warn(exception.getMessage());
                    }
                });
            }
        } catch (Exception ignored) {
            this.logger.warn("Failed to load config! Error occured when reading file.");
            return;
        }

        if (success) {
            this.logger.info("Config successfully loaded!");
        }
    }

    public void save() {
        final JsonObject object = new JsonObject();
        this.configFields.forEach(field -> {
            try {
                field.save(object);
            } catch (Exception ignored) {
                this.logger.warn("Failed to save config field '{}'!", field.getName());
            }
        });

        try {
            Files.write(this.path, ((JsonSerializer) this.serializer).serialize(object));
        } catch (Exception ignored) {
            this.logger.warn("Failed to save config!");
            return;
        }

        this.logger.info("Config successfully saved!");
    }

    public void reset() {
        // TODO: When implementing the screen system/idk, implement a event listener for like reload resource packs or whatever
        this.configFields.forEach(AbstractConfigField::restore);
        this.save();
    }

    public Logger getLogger() {
        return logger;
    }

    public List<AbstractConfigField<?>> getConfigFields() {
        return configFields;
    }

    public ModContainer getModContainer() {
        return this.modContainer;
    }

    public Path getPath() {
        return path;
    }

    public abstract Screen getConfigScreen(@Nullable Screen parent);
}
