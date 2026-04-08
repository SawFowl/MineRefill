package sawfowl.minerefill;

import java.io.File;
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

import org.checkerframework.checker.nullness.qual.Nullable;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.permission.Subject;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;

import sawfowl.minerefill.api.SourceData;
import sawfowl.localeapi.api.ConfigTypes;
import sawfowl.localeapi.api.config.ReferencedConfig;
import sawfowl.localeapi.api.serializetools.ItemStackSerializerType;
import sawfowl.localeapi.api.services.ConfigurationService;
import sawfowl.localeapi.api.services.LocaleService;
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
		createMineConfig(mine.getUniqueid(), (@Nullable MineData) mine);
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
					public Locale getLocale() {
						return LocaleService.getInstance().getSystemOrDefaultLocale();
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
		for(File mineFile : minesFolder.listFiles()) {
			if(!ConfigTypes.isValidExtension(ConfigTypes.getExtension(mineFile.getName()))) continue;
			ReferencedConfig<MineData> mineConfig = createMineConfig(mineFile);
			mines.add(mineConfig.get());
		}
		for(Mine mine : mines) mine.setNextUpdate(true);
	}

	private void sendMessage(Mine mine, Object[] path) {
		if(plugin.getConfig().isActionBarMessages()) {
			getOnlinePlayers().forEach(player -> {
				player.sendActionBar(replace(plugin.getLocales().getComponent(player.locale(), path), player.locale(), mine));
			});
		} else getOnlinePlayers().forEach(player -> {
			player.sendMessage(replace(plugin.getLocales().getComponent(player.locale(), path), player.locale(), mine));
		});
	}

	private Collection<ServerPlayer> getOnlinePlayers() {
		return Sponge.server().onlinePlayers();
	}

	private Component replace(Component component, Locale locale, Mine mine) {
		return component.replaceText(TextReplacementConfig.builder().match(ReplaceKeys.NAME).replacement(mine.getDisplayName(locale)).build());
	}

	public ReferencedConfig<MineData> createMineConfig(File file) {
		if(!plugin.getConfigDir().resolve("Mines").toFile().exists()) plugin.getConfigDir().resolve("Mines").toFile().mkdir();
		return ConfigurationService.getInstance().createReferencedConfig(plugin.getPluginContainer(), MineData.class).fromFile(file).setItemStackSerializerType(ItemStackSerializerType.JSON).build();
	}

	public ReferencedConfig<MineData> createMineConfig(UUID mine, MineData mineData) {
		if(!plugin.getConfigDir().resolve("Mines").toFile().exists()) plugin.getConfigDir().resolve("Mines").toFile().mkdir();
		return ConfigurationService.getInstance().createReferencedConfig(plugin.getPluginContainer(), mineData).setPath(plugin.getConfigDir()).setName(mine.toString()).setType(ConfigTypes.HOCON).setItemStackSerializerType(ItemStackSerializerType.JSON).build();
	}

}
