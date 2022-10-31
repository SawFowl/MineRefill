package sawfowl.minerefill.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.math.vector.Vector3i;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import sawfowl.minerefill.configure.Locales;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;

@ConfigSerializable
public class Mine {

	public Mine() {
		lastUpdate = System.currentTimeMillis();
	}

	public Mine(ServerWorld world) {
		this.world = world.key().asString();
		this.uniqueid = UUID.randomUUID();
		lastUpdate = System.currentTimeMillis();
	}

	private Random random = new Random();
	private List<BlockState> reserveBlocks = new ArrayList<>();
	private long lastUpdate = 0;
	private long nextUpdate = 0;

	@Setting("UUID")
	private UUID uniqueid;
	@Setting("Blocks")
	private List<MineBlock> blocks = new ArrayList<>();
	@Setting("ReserveBlocks")
	private List<SerializedBlock> serializedReserveBlocks = new ArrayList<>();
	@Setting("World")
	private String world;
	@Setting("Positions")
	private Positions positions = new Positions();
	@Setting("Schedule")
	private boolean schedule = false;
	@Setting("ScheduleTime")
	private int scheduleInterval = 0;
	@Setting("CustomNames")
	private Map<String, String> customNames = new HashMap<>();

	public List<BlockState> getReserveBlocks() {
		return reserveBlocks.isEmpty() ? (reserveBlocks = serializedReserveBlocks.stream().map(SerializedBlock::getBlockState).collect(Collectors.toList())) : reserveBlocks;
	}

	public List<SerializedBlock> getSerializedReserveBlocks() {
		return serializedReserveBlocks;
	}

	public UUID getUniqueid() {
		return uniqueid;
	}

	public void addReserveBlock(BlockState blockState) {
		SerializedBlock block = new SerializedBlock(blockState);
		if(serializedReserveBlocks.contains(block)) return;
		reserveBlocks.add(blockState);
		serializedReserveBlocks.add(block);
	}

	public boolean containsReserverBlock(BlockState blockState) {
		return serializedReserveBlocks.contains(new SerializedBlock(blockState));
	}

	public List<MineBlock> getBlocks() {
		return blocks;
	}

	public void addBlock(MineBlock mineBlock) {
		if(!blocks.contains(mineBlock)) blocks.add(mineBlock);
	}

	public String getWorldId() {
		return world;
	}

	public Optional<ServerWorld> getWorld() {
		return Sponge.server().worldManager().world(ResourceKey.resolve(world));
	}

	public Positions getPositions() {
		return positions;
	}

	public boolean isSchedule() {
		return schedule && scheduleInterval > 0 && (!blocks.isEmpty() || !serializedReserveBlocks.isEmpty());
	}

	public void setSchedule(boolean schedule) {
		this.schedule = schedule;
	}

	public long getScheduleInterval() {
		return scheduleInterval;
	}

	public void setScheduleTime(int interval) {
		scheduleInterval = interval;
		setNextUpdate(false);
	}

	public void removeDisplayName(Locale locale) {
		if(customNames.containsKey(locale.toLanguageTag())) customNames.remove(locale.toLanguageTag());
	}

	public void addDisplayName(Locale locale, String name) {
		removeDisplayName(locale);
		customNames.put(locale.toLanguageTag(), name);
	}

	public Component getDisplayName(Locale locale) {
		return getDisplayName(locale.toLanguageTag());
	}

	public Component getDisplayName(String locale) {
		return customNames.isEmpty() ? Component.empty() : deserialize((customNames.containsKey(locale) ? customNames.get(locale) : customNames.getOrDefault(org.spongepowered.api.util.locale.Locales.DEFAULT.toLanguageTag(), customNames.values().iterator().next())));
	}

	public Map<String, String> getNames() {
		return customNames;
	}

	public Long getLastUpdate() {
		return lastUpdate;
	}

	public Long getNextUpdate() {
		return nextUpdate;
	}

	public void setNextUpdate(boolean updateLast) {
		if(updateLast) lastUpdate = System.currentTimeMillis();
		this.nextUpdate = lastUpdate + (scheduleInterval * (long) 1000);
	}

	public Map<Vector3i, BlockState> blocksPrepare() {
		Map<Vector3i, BlockState> map = new HashMap<>();
		List<Vector3i> allPositions = positions.getAllPositions();
		if(allPositions.isEmpty() || (blocks.isEmpty() && getReserveBlocks().isEmpty())) return map;
		if(blocks.isEmpty() && !getReserveBlocks().isEmpty()) {
			for(Vector3i vector3i : allPositions) {
				map.put(vector3i, getRandomReserve());
			}
		} else {
			for(Vector3i vector3i : allPositions) {
				double chance = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0, 100)).setScale(3, RoundingMode.HALF_UP).doubleValue();
				MineBlock[] blocks = this.blocks.stream().filter(b -> (b.getChance() >= chance)).toArray(MineBlock[]::new);
				MineBlock mineBlock = blocks.length == 0 ? null : blocks[0];
				if(mineBlock != null) {
					for(MineBlock block : blocks) {
						if(block.getChance() < mineBlock.getChance()) mineBlock = block;
						if(block.getChance() == mineBlock.getChance() && random.nextBoolean()) mineBlock = block;
					}
					if(random.nextBoolean()) {
						map.put(vector3i, mineBlock.getBlockState());
					} else map.put(vector3i, getRandomReserve());
				} else map.put(vector3i, getRandomReserve());
			}
		}
		return map;
	}

	public boolean fill(PluginContainer container, Locales locales, Logger logger, boolean actionBar, boolean debug) {
		if(!getWorld().isPresent()) return false;
		long async = System.currentTimeMillis();
		ServerWorld world = getWorld().get();
		Map<Vector3i, BlockState> preparedBlocks = blocksPrepare();
		async = System.currentTimeMillis() - async;
		final long asyncTime = async;
		Sponge.server().scheduler().submit(Task.builder().delay(1, TimeUnit.SECONDS).plugin(container).execute(() -> {
			long time = System.currentTimeMillis();
			preparedBlocks.forEach((k, v) -> {
				world.setBlock(k, v);
			});
			lastUpdate = System.currentTimeMillis();
			setNextUpdate(false);
			time = System.currentTimeMillis() - time;
			if(debug && logger != null) logger.info(locales.getString(org.spongepowered.api.util.locale.Locales.DEFAULT, LocalesPaths.UPDATE_CONSOLE).replace(ReplaceKeys.UUID, uniqueid.toString()).replace(ReplaceKeys.NAME, getName()).replace(ReplaceKeys.FULL_TIME, s(asyncTime + time)).replace(ReplaceKeys.ASYNC_TIME, s(asyncTime)));
			sendMessage(actionBar, locales);
		}).build());
		return true;
	}

	private String getName() {
		Component name = getDisplayName("CONSOLE").compact();
		name.decorations().clear();
		name.style().decorations().clear();
		return LegacyComponentSerializer.legacyAmpersand().serialize(name);
	}

	private BlockState getRandomReserve() {
		if(getReserveBlocks().isEmpty()) return BlockTypes.AIR.get().defaultState();
		int size = reserveBlocks.size();
		return size == 1 ? reserveBlocks.get(0) : reserveBlocks.get(random.nextInt(size));
	}

	private Component deserialize(String string) {
		Component component;
		try {
			component = GsonComponentSerializer.gson().deserialize(string);
		} catch (Exception e) {
			component = LegacyComponentSerializer.legacyAmpersand().deserialize(string);
		}
		return component.toString().contains("&") && !component.hasStyling() ? LegacyComponentSerializer.legacyAmpersand().deserialize(string) : component;
	}

	private void sendMessage(boolean actionBar, Locales locales) {
		if(actionBar) {
			getOnlinePlayers().forEach(player -> {
				player.sendActionBar(replace(locales.getText(player.locale(), LocalesPaths.UPDATE_PLAYERS), player.locale()));
			});
		} else getOnlinePlayers().forEach(player -> {
			player.sendMessage(replace(locales.getText(player.locale(), LocalesPaths.UPDATE_PLAYERS), player.locale()));
		});
	}

	private Collection<ServerPlayer> getOnlinePlayers() {
		return Sponge.server().onlinePlayers();
	}

	private Component replace(Component component, Locale locale) {
		return component.replaceText(TextReplacementConfig.builder().match(ReplaceKeys.NAME).replacement(getDisplayName(locale)).build());
	}

	private String s(Object object) {
		return object.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(uniqueid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Mine other = (Mine) obj;
		return Objects.equals(uniqueid, other.uniqueid);
	}

}
