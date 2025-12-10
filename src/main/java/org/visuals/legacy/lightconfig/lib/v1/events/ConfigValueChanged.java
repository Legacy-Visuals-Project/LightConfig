package org.visuals.legacy.lightconfig.lib.v1.events;

public class ConfigValueChanged<T> extends Event {
	private final T oldValue;
	private final T newValue;

	public ConfigValueChanged(T oldValue, T newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public T getOldValue() {
		return this.oldValue;
	}

	public T getNewValue() {
		return this.newValue;
	}
}
