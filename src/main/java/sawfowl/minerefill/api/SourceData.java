package sawfowl.minerefill.api;

import java.util.Locale;

import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.Identifiable;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public interface SourceData {

	public Subject getSubject();

	public Audience getAudience();

	public Locale getLocale();

	public default String getIdentifier() {
		return getSubject() instanceof Identifiable ? ((Identifiable) getSubject()).uniqueId().toString() : getSubject().identifier();
	}

	public default void sendMessage(Component component) {
		getAudience().sendMessage(component);
	}

}
