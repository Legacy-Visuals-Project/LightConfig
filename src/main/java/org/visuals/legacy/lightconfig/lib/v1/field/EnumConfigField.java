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
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.visuals.legacy.lightconfig.lib.v1.ConfigTranslate;
import org.visuals.legacy.lightconfig.lib.v1.Config;

import java.util.Arrays;
import java.util.Optional;

public class EnumConfigField<T extends Enum<T>> extends GenericConfigField<T> {
    private final Class<T> enumClazz;

    public EnumConfigField(Config config, String name, T defaultValue, Class<T> clazz) {
        super(config, name, defaultValue);
        this.enumClazz = clazz;
    }

    @Override
    public void load(JsonObject object) throws Exception {
        if (!object.has(this.name)) {
            throw new Exception("Failed to load value for '" + this.name + "', object didn't contain a value for it.");
        } else {
            final JsonElement element = object.get(this.name);
            if (!element.isJsonPrimitive() || (element instanceof final JsonPrimitive primitive && !primitive.isString())) {
                throw new Exception("Failed to load value for '" + this.name + "', type does not match.");
            } else {
                Optional<T> opt = Arrays.stream(this.enumClazz.getEnumConstants())
                        .filter(cons -> cons.name().equals(element.getAsString()))
                        .findFirst();
                this.setValue(opt.orElseThrow(() -> new Exception("Failed to load value for '" + this.name + "', invalid enum value.")));
            }
        }
    }

    @Override
    public void save(JsonObject object) {
        object.addProperty(this.name, this.value.name());
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
                .tooltip(Tooltip.create(ConfigTranslate.tooltip(translationKey)))
                .build();
    }

    private Component getDisplayText(final Component translate, final String translationKey) {
        return ConfigTranslate.TEMPLATE.apply(translate, Component.translatable(translationKey + '.' + this.getValue().name()));
    }
}
