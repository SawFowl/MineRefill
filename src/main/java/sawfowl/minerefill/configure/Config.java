package sawfowl.minerefill.configure;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Config {

	public Config() {}

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
	private Map<Integer, String[]> alerts = Stream.of(
			new PlayerAlert(600, toStringArray(LocalesPaths.NOTIFICATION_10M)),
			new PlayerAlert(300, toStringArray(LocalesPaths.NOTIFICATION_5M)),
			new PlayerAlert(180, toStringArray(LocalesPaths.NOTIFICATION_3M)),
			new PlayerAlert(120, toStringArray(LocalesPaths.NOTIFICATION_2M)),
			new PlayerAlert(60, toStringArray(LocalesPaths.NOTIFICATION_1M)),
			new PlayerAlert(30, toStringArray(LocalesPaths.NOTIFICATION_30S)),
			new PlayerAlert(10, toStringArray(LocalesPaths.NOTIFICATION_10S)),
			new PlayerAlert(9, toStringArray(LocalesPaths.NOTIFICATION_9S)),
			new PlayerAlert(8, toStringArray(LocalesPaths.NOTIFICATION_8S)),
			new PlayerAlert(7, toStringArray(LocalesPaths.NOTIFICATION_7S)),
			new PlayerAlert(6, toStringArray(LocalesPaths.NOTIFICATION_6S)),
			new PlayerAlert(5, toStringArray(LocalesPaths.NOTIFICATION_5S)),
			new PlayerAlert(4, toStringArray(LocalesPaths.NOTIFICATION_4S)),
			new PlayerAlert(3, toStringArray(LocalesPaths.NOTIFICATION_3S)),
			new PlayerAlert(2, toStringArray(LocalesPaths.NOTIFICATION_2S)),
			new PlayerAlert(1, toStringArray(LocalesPaths.NOTIFICATION_1S))
			).collect(Collectors.toMap(alert -> alert.getTime(), alert -> alert.getPath()));

	private Map<Integer, Object[]> cachedAlerts = new HashMap<Integer, Object[]>();

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
		if(cachedAlerts.size() == alerts.size()) return cachedAlerts;
		cachedAlerts.clear();
		cachedAlerts.putAll(alerts);
		return cachedAlerts;
	}

	private String[] toStringArray(Object[] path) {
		return Stream.of(path).map(Object::toString).toArray(String[]::new);
	}

}
