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
import sawfowl.minerefill.commands.AddBlockCommand;
import sawfowl.minerefill.commands.AddReserveBlockCommand;
import sawfowl.minerefill.commands.CommandParameters;
import sawfowl.minerefill.commands.CreateCommand;
import sawfowl.minerefill.commands.DeleteCommand;
import sawfowl.minerefill.commands.FillCommand;
import sawfowl.minerefill.commands.InfoCommand;
import sawfowl.minerefill.commands.ListCommand;
import sawfowl.minerefill.commands.MainCommand;
import sawfowl.minerefill.commands.ReloadCommand;
import sawfowl.minerefill.commands.SaveCommand;
import sawfowl.minerefill.commands.SetIntervalCommand;
import sawfowl.minerefill.commands.SetNameCommand;
import sawfowl.minerefill.commands.SetPosCommand;
import sawfowl.minerefill.commands.SetScheduleCommand;
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

		Command.Parameterized createCommand = Command.builder()
				.permission(Permissions.EDIT)
				.executor(new CreateCommand(instance))
				.build();
		
		Command.Parameterized deleteCommand = Command.builder()
				.permission(Permissions.EDIT)
				.executor(new DeleteCommand(instance))
				.build();
		
		Command.Parameterized setPosCommand = Command.builder()
				.permission(Permissions.EDIT)
				.addParameter(CommandParameters.POSITION)
				.executor(new SetPosCommand(instance))
				.build();

		Command.Parameterized addBlockCommand = Command.builder()
				.permission(Permissions.EDIT)
				.addParameter(CommandParameters.CHANCE)
				.executor(new AddBlockCommand(instance))
				.build();

		Command.Parameterized addReserveBlockCommand = Command.builder()
				.permission(Permissions.EDIT)
				.executor(new AddReserveBlockCommand(instance))
				.build();

		Command.Parameterized intervalCommand = Command.builder()
				.permission(Permissions.EDIT)
				.addParameter(CommandParameters.TIME)
				.executor(new SetIntervalCommand(instance))
				.build();

		Command.Parameterized scheduleCommand = Command.builder()
				.permission(Permissions.EDIT)
				.addParameter(CommandParameters.SCHEDULE)
				.executor(new SetScheduleCommand(instance))
				.build();

		Command.Parameterized setNameCommand = Command.builder()
				.permission(Permissions.EDIT)
				.addParameters(CommandParameters.LOCALE, CommandParameters.NAME)
				.executor(new SetNameCommand(instance))
				.build();

		Command.Parameterized infoCommand = Command.builder()
				.permission(Permissions.INFO)
				.executor(new InfoCommand(instance))
				.build();

		Command.Parameterized saveCommand = Command.builder()
				.permission(Permissions.SAVE)
				.executor(new SaveCommand(instance))
				.build();

		Command.Parameterized fillCommand = Command.builder()
				.permission(Permissions.FILL)
				.executor(new FillCommand(instance))
				.build();

		Command.Parameterized listCommand = Command.builder()
				.permission(Permissions.LIST)
				.executor(new ListCommand(instance))
				.build();

		Command.Parameterized reloadCommand = Command.builder()
				.permission(Permissions.RELOAD)
				.executor(new ReloadCommand(instance))
				.build();

		Command.Parameterized mainCommand = Command.builder()
				.permission(Permissions.MAIN_COMMAND)
				.executor(new MainCommand(instance))
				.addChild(createCommand, "create")
				.addChild(deleteCommand, "delete")
				.addChild(setPosCommand, "setpos")
				.addChild(addBlockCommand, "addblock")
				.addChild(addReserveBlockCommand, "addreserveblock")
				.addChild(intervalCommand, "interval")
				.addChild(scheduleCommand, "schedule")
				.addChild(setNameCommand, "setname")
				.addChild(infoCommand, "info")
				.addChild(saveCommand, "save")
				.addChild(fillCommand, "fill")
				.addChild(listCommand, "list")
				.addChild(reloadCommand, "reload")
				.build();
		event.register(pluginContainer, mainCommand, "minerefill", "mine");
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
				switch (nextUpdateSeconds) {
				case 600:
					sendMessage(mine, LocalesPaths.NOTIFICATION_10M);
					break;
				case 300:
					sendMessage(mine, LocalesPaths.NOTIFICATION_5M);
					break;
				case 180:
					sendMessage(mine, LocalesPaths.NOTIFICATION_3M);
					break;
				case 120:
					sendMessage(mine, LocalesPaths.NOTIFICATION_2M);
					break;
				case 60:
					sendMessage(mine, LocalesPaths.NOTIFICATION_1M);
					break;
				case 30:
					sendMessage(mine, LocalesPaths.NOTIFICATION_30S);
					break;
				case 10:
					sendMessage(mine, LocalesPaths.NOTIFICATION_10S);
					break;
				case 9:
					sendMessage(mine, LocalesPaths.NOTIFICATION_9S);
					break;
				case 8:
					sendMessage(mine, LocalesPaths.NOTIFICATION_8S);
					break;
				case 7:
					sendMessage(mine, LocalesPaths.NOTIFICATION_7S);
					break;
				case 6:
					sendMessage(mine, LocalesPaths.NOTIFICATION_6S);
					break;
				case 5:
					sendMessage(mine, LocalesPaths.NOTIFICATION_5S);
					break;
				case 4:
					sendMessage(mine, LocalesPaths.NOTIFICATION_4S);
					break;
				case 3:
					sendMessage(mine, LocalesPaths.NOTIFICATION_3S);
					break;
				case 2:
					sendMessage(mine, LocalesPaths.NOTIFICATION_2S);
					break;
				case 1:
					sendMessage(mine, LocalesPaths.NOTIFICATION_1S);
					break;
				}
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
