package sawfowl.minerefill;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.ValueReference;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import com.google.inject.Inject;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import sawfowl.localeapi.event.LocaleServiseEvent;
import sawfowl.minerefill.commands.MainCommand;
import sawfowl.minerefill.configure.Config;
import sawfowl.minerefill.configure.Locales;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;
import sawfowl.minerefill.data.Mine;

@Plugin("minerefill")
public class MineRefill {

	private static MineRefill instance;
	private PluginContainer pluginContainer;
	private Logger logger;
	private Locales locales;
	private Path configDir;
	private ConfigurationReference<CommentedConfigurationNode> configurationReference;
	private ValueReference<Config, CommentedConfigurationNode> config;
	private ConfigurationOptions options;
	private Set<Mine> mines = new HashSet<>();
	private Map<UUID, Mine> editableMines = new HashMap<>();

	@Inject
	public MineRefill(PluginContainer pluginContainer, @ConfigDir(sharedRoot = false) Path configDirectory) {
		instance = this;
		logger = LogManager.getLogger("MineRefill");
		this.pluginContainer = pluginContainer;
		configDir = configDirectory;
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
		loadMines();
		logger.info(locales.getString(org.spongepowered.api.util.locale.Locales.DEFAULT, LocalesPaths.LOADED_MINES).replace(ReplaceKeys.SIZE, Objects.toString(mines.size())));
		Sponge.asyncScheduler().submit((getConfig().getDelayAfterStart() > 0 ? Task.builder().delay(getConfig().getDelayAfterStart(), TimeUnit.MINUTES) : Task.builder()).interval(1, TimeUnit.SECONDS).plugin(pluginContainer).execute(() -> {
			for(Mine mine : mines) if(mine.isSchedule() && mine.getWorld().isPresent()) {
				int nextUpdateSeconds = (int) ((mine.getNextUpdate() / 1000) - (System.currentTimeMillis() / 1000));
				if(nextUpdateSeconds == 0) mine.fill(pluginContainer, locales, logger, getConfig().isActionBarMessages(), getConfig().isDebug());
				if(nextUpdateSeconds < 0) mine.setNextUpdate(true);
				if(getConfig().getAlerts().containsKey(nextUpdateSeconds)) sendMessage(mine, getConfig().getAlerts().get(nextUpdateSeconds));
			}
		}).build());
	}

	public void loadMines() {
		File minesFolder = configDir.resolve("Mines").toFile();
		if(!minesFolder.exists()) return;
		for(File mineFile : Arrays.stream(minesFolder.listFiles()).filter(file -> (file.getName().contains(".conf"))).collect(Collectors.toList())) {
			try {
				ConfigurationReference<CommentedConfigurationNode> mineConfigReference = HoconConfigurationLoader.builder().defaultOptions(options).file(mineFile).build().loadToReference();
				ValueReference<Mine, CommentedConfigurationNode> mineConfig = mineConfigReference.referenceTo(Mine.class);
				mines.add(mineConfig.get());
			} catch (ConfigurateException e) {
				logger.error(e.getLocalizedMessage());
				continue;
			}
		}
		for(Mine mine : mines) mine.setNextUpdate(true);
	}

	public void reload() {
		try {
			configurationReference.load();
			config = configurationReference.referenceTo(Config.class);
			mines.clear();
			loadMines();
		} catch (ConfigurateException e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	public static MineRefill getInstance() {
		return instance;
	}

	public PluginContainer getPluginContainer() {
		return pluginContainer;
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

	public Set<Mine> getMines() {
		return mines;
	}

	public Map<UUID, Mine> getEditableMines() {
		return editableMines;
	}

	public void removeEditableMine(UUID player) {
		if(editableMines.containsKey(player)) editableMines.remove(player);
	}

	public void addEditableMine(UUID player, Mine mine) {
		removeEditableMine(player);
		editableMines.put(player, mine);
	}

	public Optional<Mine> getEditableMine(UUID player) {
		return editableMines.containsKey(player) ? Optional.ofNullable(editableMines.get(player)) : Optional.empty();
	}

	public void saveMine(Mine mine) {
		if(!mines.contains(mine)) mines.add(mine);
		try {
			ConfigurationReference<CommentedConfigurationNode> mineConfigReference = HoconConfigurationLoader.builder().defaultOptions(options).path(configDir.resolve("Mines" + File.separator + mine.getUniqueid().toString() + ".conf")).build().loadToReference();
			ValueReference<Mine, CommentedConfigurationNode> mineConfig = mineConfigReference.referenceTo(Mine.class);
			mineConfigReference.save();
			mineConfig.setAndSave(mine);
		} catch (ConfigurateException e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	public void deleteMine(Mine mine) {
		configDir.resolve("Mines" + File.separator + mine.getUniqueid().toString() + ".conf").toFile().delete();
		mines.remove(mine);
	}

	private void sendMessage(Mine mine, Object[] path) {
		if(getConfig().isActionBarMessages()) {
			getOnlinePlayers().forEach(player -> {
				player.sendActionBar(replace(locales.getText(player.locale(), path), player.locale(), mine));
			});
		} else getOnlinePlayers().forEach(player -> {
			player.sendMessage(replace(locales.getText(player.locale(), path), player.locale(), mine));
		});
	}

	private Collection<ServerPlayer> getOnlinePlayers() {
		return Sponge.server().onlinePlayers();
	}

	private Component replace(Component component, Locale locale, Mine mine) {
		return component.replaceText(TextReplacementConfig.builder().match(ReplaceKeys.NAME).replacement(mine.getDisplayName(locale)).build());
	}

}
