package sawfowl.minerefill;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.locale.LocaleSource;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.ValueReference;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import sawfowl.minerefill.api.SourceData;
import sawfowl.minerefill.api.Mine;
import sawfowl.minerefill.api.MineAPI;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;
import sawfowl.minerefill.data.MineData;

class API implements MineAPI {

	private final MineRefill plugin;
	API(MineRefill plugin) {
		this.plugin = plugin;
	}

	private Set<Mine> mines = new HashSet<>();
	private Map<String, Mine> editableMines = new HashMap<>();

	@Override
	public Set<Mine> getMines() {
		return mines;
	}

	@Override
	public Map<String, Mine> getEditableMines() {
		return editableMines;
	}

	@Override
	public void removeEditableMine(String editor) {
		if(editableMines.containsKey(editor)) editableMines.remove(editor);
	}

	@Override
	public void addEditableMine(String editor, Mine mine) {
		removeEditableMine(editor);
		editableMines.put(editor, mine);
	}

	@Override
	public Optional<Mine> getEditableMine(String editor) {
		return editableMines.containsKey(editor) ? Optional.ofNullable(editableMines.get(editor)) : Optional.empty();
	}

	@Override
	public void saveMine(Mine mine) {
		if(!mines.contains(mine)) mines.add(mine);
		try {
			ConfigurationReference<CommentedConfigurationNode> mineConfigReference = HoconConfigurationLoader.builder().defaultOptions(plugin.getOptions()).path(plugin.getConfigDir().resolve("Mines" + File.separator + mine.getUniqueid().toString() + ".conf")).build().loadToReference();
			ValueReference<MineData, CommentedConfigurationNode> mineConfig = mineConfigReference.referenceTo(MineData.class);
			mineConfigReference.save();
			mineConfig.setAndSave((@Nullable MineData) mine);
		} catch (ConfigurateException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
		}
	}

	@Override
	public void deleteMine(Mine mine) {
		plugin.getConfigDir().resolve("Mines" + File.separator + mine.getUniqueid().toString() + ".conf").toFile().delete();
		mines.remove(mine);
	}

	void startSchedule() {
		loadMines();
		plugin.getLogger().info(plugin.getLocales().getString(org.spongepowered.api.util.locale.Locales.DEFAULT, LocalesPaths.LOADED_MINES).replace(ReplaceKeys.SIZE, Objects.toString(mines.size())));
		Sponge.asyncScheduler().submit((plugin.getConfig().getDelayAfterStart() > 0 ? Task.builder().delay(plugin.getConfig().getDelayAfterStart(), TimeUnit.MINUTES) : Task.builder()).interval(1, TimeUnit.SECONDS).plugin(plugin.getPluginContainer()).execute(() -> {
			for(Mine mine : mines) if(mine.isSchedule() && mine.getWorld().isPresent()) {
				int nextUpdateSeconds = (int) ((mine.getNextUpdate() / 1000) - (System.currentTimeMillis() / 1000));
				if(nextUpdateSeconds == 0) mine.fill(plugin.getPluginContainer(), plugin.getConfig().isActionBarMessages(), plugin.getConfig().isDebug(), new SourceData() {

					@Override
					public Subject getSubject() {
						return Sponge.systemSubject();
					}
					
					@Override
					public LocaleSource getLocaleSource() {
						return Sponge.systemSubject();
					}
					
					@Override
					public Audience getAudience() {
						return Sponge.systemSubject();
					}
				});
				if(nextUpdateSeconds < 0) mine.setNextUpdate(true);
				if(plugin.getConfig().getAlerts().containsKey(nextUpdateSeconds)) sendMessage(mine, plugin.getConfig().getAlerts().get(nextUpdateSeconds));
			}
		}).build());
	}

	void loadMines() {
		File minesFolder = plugin.getConfigDir().resolve("Mines").toFile();
		if(!minesFolder.exists()) return;
		for(File mineFile : Arrays.stream(minesFolder.listFiles()).filter(file -> (file.getName().contains(".conf"))).collect(Collectors.toList())) {
			try {
				ConfigurationReference<CommentedConfigurationNode> mineConfigReference = HoconConfigurationLoader.builder().defaultOptions(plugin.getOptions()).file(mineFile).build().loadToReference();
				ValueReference<MineData, CommentedConfigurationNode> mineConfig = mineConfigReference.referenceTo(MineData.class);
				mines.add(mineConfig.get());
			} catch (ConfigurateException e) {
				plugin.getLogger().error(e.getLocalizedMessage());
				continue;
			}
		}
		for(Mine mine : mines) mine.setNextUpdate(true);
	}

	private void sendMessage(Mine mine, Object[] path) {
		if(plugin.getConfig().isActionBarMessages()) {
			getOnlinePlayers().forEach(player -> {
				player.sendActionBar(replace(plugin.getLocales().getText(player.locale(), path), player.locale(), mine));
			});
		} else getOnlinePlayers().forEach(player -> {
			player.sendMessage(replace(plugin.getLocales().getText(player.locale(), path), player.locale(), mine));
		});
	}

	private Collection<ServerPlayer> getOnlinePlayers() {
		return Sponge.server().onlinePlayers();
	}

	private Component replace(Component component, Locale locale, Mine mine) {
		return component.replaceText(TextReplacementConfig.builder().match(ReplaceKeys.NAME).replacement(mine.getDisplayName(locale)).build());
	}

}
