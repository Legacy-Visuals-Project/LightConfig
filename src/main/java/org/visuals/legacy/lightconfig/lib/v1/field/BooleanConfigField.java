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
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.visuals.legacy.lightconfig.lib.ConfigTranslate;
import org.visuals.legacy.lightconfig.lib.v1.Config;

public class BooleanConfigField extends GenericConfigField<Boolean> {
    public BooleanConfigField(final Config config, final String name, final boolean defaultValue) {
        super(config, name, defaultValue);
    }

    @Override
    public void load(JsonObject object) throws Exception {
        if (!object.has(this.name)) {
            throw new Exception("Failed to load value for '" + this.name + "', object didn't contain a value for it.");
        } else {
            final JsonElement element = object.get(this.name);
            if (!element.isJsonPrimitive() || (element instanceof final JsonPrimitive primitive && !primitive.isBoolean())) {
                throw new Exception("Failed to load value for '" + this.name + "', type does not match.");
            } else {
                this.setValue(element.getAsBoolean());
            }
        }
    }

    @Override
    public void save(JsonObject object) {
        object.addProperty(this.name, this.value);
    }

    @Override
    public Button createWidget() {
        return this.createWidget(() -> {
        });
    }

    public Button createWidget(final Runnable onClick) {
        final String translationKey = this.getTranslationKey();
        final Component translate = Component.translatable(translationKey);
        return Button.builder(ConfigTranslate.TEMPLATE.apply(translate, ConfigTranslate.toggle(this.isEnabled())), (button) -> {
                    this.toggle();
                    onClick.run();
                    button.setMessage(ConfigTranslate.TEMPLATE.apply(translate, ConfigTranslate.toggle(this.isEnabled())));
                })
                .tooltip(Tooltip.create(ConfigTranslate.tooltip(translationKey)))
                .build();
    }

    public void toggle() {
        this.setValue(!this.value);
    }

    public boolean isEnabled() {
        return this.value;
    }
}
