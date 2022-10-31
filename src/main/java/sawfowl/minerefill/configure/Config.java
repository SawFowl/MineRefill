package sawfowl.minerefill.configure;

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

}
