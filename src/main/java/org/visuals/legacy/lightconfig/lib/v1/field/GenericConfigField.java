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

import com.google.gson.JsonObject;
import net.minecraft.client.gui.components.AbstractWidget;
import org.visuals.legacy.lightconfig.lib.v1.Config;

public class GenericConfigField<T> extends AbstractConfigField<T> {
    protected T value;

    public GenericConfigField(Config config, String name, T defaultValue) {
        super(config, name, defaultValue);
        this.value = defaultValue;
    }

    @Override
    public void load(JsonObject object) throws Exception {
        throw new RuntimeException("Unimplemented load for " + this.getClass().getName());
    }

    @Override
    public void save(JsonObject object) throws Exception {
        throw new RuntimeException("Unimplemented save for " + this.getClass().getName());
    }

    @Override
    public void restore() {
        this.setValue(this.getDefaultValue());
    }

    @Override
    public AbstractWidget createWidget() {
        throw new RuntimeException("Unimplemented createWidget for " + this.getClass().getName());
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
