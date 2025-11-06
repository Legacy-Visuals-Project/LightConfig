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

package org.visuals.legacy.lightconfig.lib.v1.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.visuals.legacy.lightconfig.lib.v1.Config;
import org.visuals.legacy.lightconfig.lib.v1.Translations;
import org.visuals.legacy.lightconfig.lib.v1.serialization.ConfigDeserializer;
import org.visuals.legacy.lightconfig.lib.v1.serialization.ConfigSerializer;
import org.visuals.legacy.lightconfig.lib.v1.serialization.Json;
import org.visuals.legacy.lightconfig.lib.v1.type.Type;

import java.util.Arrays;

public class EnumConfigField<T extends Enum<T>> extends AbstractConfigField<T> {
    private final Class<T> enumClazz;

    private final Type<T> ENUM_TYPE = new Type<>() {
        @Override
        public @Nullable T read(ConfigDeserializer<?> deserializer, String name) {
            final JsonElement element = ((Json.Deserializer) deserializer).object().get(name);
            if (element == null || !element.isJsonPrimitive() || (element instanceof final JsonPrimitive primitive && !primitive.isString())) {
                return null;
            } else {
                return Arrays.stream(EnumConfigField.this.enumClazz.getEnumConstants())
                        .filter(cons -> cons.name().equals(element.getAsString()))
                        .findFirst()
                        .orElse(null);
            }
        }

        @Override
        public void write(ConfigSerializer<?> serializer, String name, T value) {
            ((Json.Serializer) serializer).object().addProperty(name, value.name());
        }
    };

    public EnumConfigField(Config config, String name, T defaultValue, Class<T> clazz) {
        super(config, name, defaultValue);
        this.enumClazz = clazz;
    }

    @Override
    public void load(ConfigDeserializer<?> deserializer) throws Exception {
        final T value = ENUM_TYPE.read(deserializer, this.name);
        if (value == null) {
            throw new Exception("Failed to load value for '" + this.name + "'");
        } else {
            this.setValue(value);
        }
    }

    @Override
    public void save(ConfigSerializer<?> serializer) {
        ENUM_TYPE.write(serializer, this.name, this.value);
    }

    @Override
    public AbstractWidget createWidget() {
        final String translationKey = this.getTranslationKey();
        final Component translate = Component.translatable(translationKey);
        return Button.builder(getDisplayText(translate, translationKey), (button) -> {
                    T[] constants = this.enumClazz.getEnumConstants();
                    T next = constants[(this.value.ordinal() + 1) % constants.length];
                    this.setValue(next);
                    button.setMessage(getDisplayText(translate, translationKey));
                })
                .tooltip(Tooltip.create(Translations.tooltip(translationKey)))
                .build();
    }

    private Component getDisplayText(final Component translate, final String translationKey) {
        return Translations.TEMPLATE.apply(translate, Component.translatable(translationKey + '.' + this.getValue().name()));
    }
}
