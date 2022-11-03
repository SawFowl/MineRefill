package sawfowl.minerefill.api;

import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.locale.LocaleSource;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public interface SourceData {

	public Subject getSubject();

	public Audience getAudience();

	public LocaleSource getLocaleSource();

	public default String getIdentifier() {
		return getSubject() instanceof Identifiable ? ((Identifiable) getSubject()).uniqueId().toString() : getSubject().identifier();
	}

	public default void sendMessage(Component component) {
		getAudience().sendMessage(component);
	}

}
