package sawfowl.minerefill;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import com.google.inject.Inject;

import sawfowl.localeapi.api.ConfigTypes;
import sawfowl.localeapi.api.config.ReferencedConfig;
import sawfowl.localeapi.api.serializetools.ItemStackSerializerType;
import sawfowl.minerefill.api.Mine;
import sawfowl.minerefill.api.MineAPI;
import sawfowl.minerefill.api.event.PostMineAPIEvent;
import sawfowl.minerefill.commands.Main;
import sawfowl.minerefill.configure.Config;
import sawfowl.minerefill.configure.Locales;

@Plugin("minerefill")
public class MineRefill {

	private static MineRefill instance;
	private PluginContainer pluginContainer;
	private Logger logger;
	private Locales locales;
	private Path configDir;
	private MineAPI mineAPI;
	private ReferencedConfig<Config> config;

	@Inject
	public MineRefill(PluginContainer pluginContainer, @ConfigDir(sharedRoot = false) Path configDirectory) {
		instance = this;
		logger = LogManager.getLogger("MineRefill");
		this.pluginContainer = pluginContainer;
		configDir = configDirectory;
		mineAPI = new API(instance);
		config = ReferencedConfig.create(pluginContainer, configDirectory, "Config", ConfigTypes.HOCON, ItemStackSerializerType.JSON, null, Config.class);
		locales = new Locales(pluginContainer);
		if(!configDir.resolve("Mines").toFile().exists()) {
			configDir.resolve("Mines").toFile().mkdir();
		}
	}

	@Listener
	public void onCommandRegister(RegisterCommandEvent<Command.Parameterized> event) {
		event.register(pluginContainer, new Main(instance).build(), "minerefill", "mine");
	}

	@Listener
	public void onStart(StartedEngineEvent<Server> event) {
		((API) mineAPI).startSchedule();
		Sponge.eventManager().post(new PostMineAPIEvent() {
			
			@Override
			public Cause cause() {
				return Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, pluginContainer).build(), pluginContainer);
			}
			
			@Override
			public MineAPI getAPI() {
				return mineAPI;
			}
		});
	}

	public void reload() {
		config.load();
		mineAPI.getMines().clear();
		((API) mineAPI).loadMines();
		if(mineAPI.getEditableMines().isEmpty()) return;
		Map<String, Mine> edit = new HashMap<>(mineAPI.getEditableMines());
		mineAPI.getEditableMines().clear();
		edit.forEach((editor, mine) -> {
			mineAPI.getMines().stream().filter(m -> (m.getUniqueid().equals(mine.getUniqueid()))).findFirst().ifPresent(m -> {
				mineAPI.getEditableMines().put(editor, m);
			});
		});
	}

	public static MineRefill getInstance() {
		return instance;
	}

	public PluginContainer getPluginContainer() {
		return pluginContainer;
	}

	public Path getConfigDir() {
		return configDir;
	}

	public Logger getLogger() {
		return logger;
	}

	public Locales getLocales() {
		return locales;
	}

	public Config getConfig() {
		return config.get();
	}

	public MineAPI getMineAPI() {
		return mineAPI;
	}

}
