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
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.ValueReference;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import com.google.inject.Inject;

import sawfowl.localeapi.event.LocaleServiseEvent;
import sawfowl.minerefill.api.Mine;
import sawfowl.minerefill.api.MineAPI;
import sawfowl.minerefill.api.event.PostMineAPIEvent;
import sawfowl.minerefill.commands.MainCommand;
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

	private ConfigurationReference<CommentedConfigurationNode> configurationReference;
	private ValueReference<Config, CommentedConfigurationNode> config;
	private ConfigurationOptions options;

	@Inject
	public MineRefill(PluginContainer pluginContainer, @ConfigDir(sharedRoot = false) Path configDirectory) {
		instance = this;
		logger = LogManager.getLogger("MineRefill");
		this.pluginContainer = pluginContainer;
		configDir = configDirectory;
		mineAPI = new API(instance);
	}

	@Listener
	public void onPostLocaleAPI(LocaleServiseEvent.Construct event) {
		options = event.getLocaleService().getConfigurationOptions();
		try {
			configurationReference = HoconConfigurationLoader.builder().defaultOptions(event.getLocaleService().getConfigurationOptions()).path(configDir.resolve("Config.conf")).build().loadToReference();
			this.config = configurationReference.referenceTo(Config.class);
			configurationReference.save();
		} catch (ConfigurateException e) {
			logger.error(e.getLocalizedMessage());
		}
		locales = new Locales(event.getLocaleService(), getConfig().isJsonLocales());
		if(!configDir.resolve("Mines").toFile().exists()) {
			configDir.resolve("Mines").toFile().mkdir();
		}
	}

	@Listener
	public void onCommandRegister(RegisterCommandEvent<Command.Parameterized> event) {
		event.register(pluginContainer, new MainCommand(instance).build(), "minerefill", "mine");
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
		try {
			configurationReference.load();
			config = configurationReference.referenceTo(Config.class);
		} catch (ConfigurateException e) {
			logger.error(e.getLocalizedMessage());
		}
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

	public ConfigurationOptions getOptions() {
		return options;
	}

	public MineAPI getMineAPI() {
		return mineAPI;
	}

}
