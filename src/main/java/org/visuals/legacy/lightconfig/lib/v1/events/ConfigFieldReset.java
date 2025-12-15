package org.visuals.legacy.lightconfig.lib.v1.events;

import org.visuals.legacy.lightconfig.lib.v1.field.AbstractConfigField;

public class ConfigFieldReset extends Event {
	private final AbstractConfigField<?> field;

	public ConfigFieldReset(final AbstractConfigField<?> field) {
		this.field = field;
	}

	public <T> AbstractConfigField<T> getField() {
		return (AbstractConfigField<T>) this.field;
	}
}
