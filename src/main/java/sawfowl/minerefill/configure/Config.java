package sawfowl.minerefill.configure;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Config {

	public Config() {}

	@Setting("JsonLocales")
	private boolean jsonLocales = true;
	@Setting("Debug")
	@Comment("Display in the console information about how much time it took to generate blocks in the mine.")
	private boolean debug = true;

	@Setting("DelayAfterStart")
	@Comment("Delay in minutes before the start of the mines update.")
	private int delayAfterStart = 1;

	@Setting("ActionBarMessages")
	@Comment("If true, players will receive ActionBar messages instead of chat.")
	private boolean actionBarMessages = true;

	@Setting("PlayerAlerts")
	@Comment("The key is the time in seconds before the mine updates. The value is the name of the section with the message text in the language files.")
	private Map<Integer, String[]> alerts = new HashMap<Integer, String[]>();
	private Map<Integer, Object[]> cachedAlerts = new HashMap<Integer, Object[]>();

	public boolean isJsonLocales() {
		return jsonLocales;
	}

	public boolean isDebug() {
		return debug;
	}

	public int getDelayAfterStart() {
		return delayAfterStart;
	}

	public boolean isActionBarMessages() {
		return actionBarMessages;
	}

	public Map<Integer, Object[]> getAlerts() {
		if(alerts.isEmpty()) return fill();
		if(cachedAlerts.size() == alerts.size()) return cachedAlerts;
		cachedAlerts.clear();
		cachedAlerts.putAll(alerts);
		return cachedAlerts;
	}

	private Map<Integer, Object[]> fill() {
		alerts.put(600, toStringArray(LocalesPaths.NOTIFICATION_10M));
		alerts.put(300, toStringArray(LocalesPaths.NOTIFICATION_5M));
		alerts.put(180, toStringArray(LocalesPaths.NOTIFICATION_3M));
		alerts.put(120, toStringArray(LocalesPaths.NOTIFICATION_2M));
		alerts.put(60, toStringArray(LocalesPaths.NOTIFICATION_1M));
		alerts.put(30, toStringArray(LocalesPaths.NOTIFICATION_30S));
		alerts.put(10, toStringArray(LocalesPaths.NOTIFICATION_10S));
		alerts.put(9, toStringArray(LocalesPaths.NOTIFICATION_9S));
		alerts.put(8, toStringArray(LocalesPaths.NOTIFICATION_8S));
		alerts.put(7, toStringArray(LocalesPaths.NOTIFICATION_7S));
		alerts.put(6, toStringArray(LocalesPaths.NOTIFICATION_6S));
		alerts.put(5, toStringArray(LocalesPaths.NOTIFICATION_5S));
		alerts.put(4, toStringArray(LocalesPaths.NOTIFICATION_4S));
		alerts.put(3, toStringArray(LocalesPaths.NOTIFICATION_3S));
		alerts.put(2, toStringArray(LocalesPaths.NOTIFICATION_2S));
		alerts.put(1, toStringArray(LocalesPaths.NOTIFICATION_1S));
		cachedAlerts.putAll(alerts);
		return cachedAlerts;
	}

	private String[] toStringArray(Object[] path) {
		return Stream.of(path).map(Object::toString).toArray(String[]::new);
	}

}
