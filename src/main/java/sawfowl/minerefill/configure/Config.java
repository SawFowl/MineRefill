package sawfowl.minerefill.configure;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.localeapi.api.ConfigTypes;
import sawfowl.localeapi.api.serializetools.SerializeOptions;

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

	@Setting("ConfigType")
	@Comment("The type of configuration file for the mine.\nAvailable variants: `.conf`, `.json`, `.yml`.\nIf mines have already been created, this parameter can be safely changed when restarting the server.")
	private String configType = ConfigTypes.HOCON.toString();

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
		if(cachedAlerts.size() == alerts.size()) return cachedAlerts;
		cachedAlerts.clear();
		cachedAlerts.putAll(alerts);
		return cachedAlerts;
	}

	private String[] toStringArray(Object[] path) {
		return Stream.of(path).map(Object::toString).toArray(String[]::new);
	}

	public ConfigurationLoader<? extends ConfigurationNode> createMineConfigLoader(Path configDir, UUID mine) {
		if(!configDir.resolve("Mines").toFile().exists()) configDir.resolve("Mines").toFile().mkdir();
		switch (configType) {
		case ".json": return SerializeOptions.createJsonConfigurationLoader(2).path(configDir.resolve("Mines" + File.separator + mine.toString() + configType)).build();
		case ".yml": return SerializeOptions.createYamlConfigurationLoader(2).path(configDir.resolve("Mines" + File.separator + mine.toString() + configType)).build();
		default: return SerializeOptions.createHoconConfigurationLoader(2).path(configDir.resolve("Mines" + File.separator + mine.toString() + ".conf")).build();
		}
	}

	public Optional<ConfigurationLoader<? extends ConfigurationNode>> createMineConfigLoader(Path configDir, File file) {
		String type = "." + getExtension(file.getName());
		ConfigurationLoader<? extends ConfigurationNode> loader;
		switch (type) {
			case ".json": {
				loader = SerializeOptions.createJsonConfigurationLoader(2).file(file).build();
				break;
			}
			case ".yml": {
				loader = SerializeOptions.createYamlConfigurationLoader(2).file(file).build();
				break;
			}
			case ".conf": {
				loader = SerializeOptions.createHoconConfigurationLoader(2).file(file).build();
				break;
			}
			default: throw new IllegalArgumentException("Unexpected value: " + file.getName());
		}
		if(!type.equals(configType)) {
			try {
				BasicConfigurationNode tempNode = BasicConfigurationNode.root().from(loader.load());
				loader = createMineConfigLoader(configDir, UUID.fromString(tempNode.node("UUID").getString()));
				file.delete();
				loader.save(tempNode);
			} catch (ConfigurateException e) {
				e.printStackTrace();
				return Optional.empty();
			}
		}
		return Optional.ofNullable(loader);
	}

	String getExtension(String fileName) {
		char ch;
		int len;
		if(fileName==null || 
				(len = fileName.length())==0 ||
				(ch = fileName.charAt(len-1))=='/' || ch=='\\' ||
				 ch=='.' )
			return "";
		int dotInd = fileName.lastIndexOf('.'),
			sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
		if(dotInd<=sepInd) return "";
		else return fileName.substring(dotInd+1).toLowerCase();
	}

}
