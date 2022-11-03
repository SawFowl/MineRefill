package sawfowl.minerefill.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.StringWriter;
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
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.NodeResolver;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import org.spongepowered.math.vector.Vector3i;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import sawfowl.minerefill.api.SourceData;
import sawfowl.minerefill.api.event.FillEvent;
import sawfowl.minerefill.configure.Locales;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.api.Mine;

@ConfigSerializable
public class MineData implements Mine {

	public MineData() {
		lastUpdate = System.currentTimeMillis();
	}

	public MineData(ServerWorld world) {
		this.world = world.key().asString();
		this.uniqueid = UUID.randomUUID();
		lastUpdate = System.currentTimeMillis();
	}

	private Random random = new Random();
	private List<BlockState> reserveBlocks = new ArrayList<>();
	private long lastUpdate = 0;
	private long nextUpdate = 0;
	private final MineData instance = this;
	private final Locales locales = ((MineRefill) Sponge.pluginManager().plugin("minerefill").get().instance()).getLocales();
	private final Logger logger = ((MineRefill) Sponge.pluginManager().plugin("minerefill").get().instance()).getLogger();

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
	@Setting("AdditionalData")
	private Map<String, Map<String, String>> additionalData = null;

	@Override
	public List<BlockState> getReserveBlocks() {
		return reserveBlocks.isEmpty() ? (reserveBlocks = serializedReserveBlocks.stream().map(SerializedBlock::getBlockState).collect(Collectors.toList())) : reserveBlocks;
	}

	@Override
	public List<SerializedBlock> getSerializedReserveBlocks() {
		return serializedReserveBlocks;
	}

	@Override
	public UUID getUniqueid() {
		return uniqueid;
	}

	@Override
	public MineData addReserveBlock(BlockState blockState) {
		SerializedBlock block = new SerializedBlock(blockState);
		if(serializedReserveBlocks.contains(block)) return instance;
		reserveBlocks.add(blockState);
		serializedReserveBlocks.add(block);
		return instance;
	}

	@Override
	public boolean containsReserverBlock(BlockState blockState) {
		return serializedReserveBlocks.contains(new SerializedBlock(blockState));
	}

	@Override
	public List<MineBlock> getBlocks() {
		return blocks;
	}

	@Override
	public MineData addBlock(MineBlock mineBlock) {
		if(!blocks.contains(mineBlock)) blocks.add(mineBlock);
		return instance;
	}

	@Override
	public String getWorldId() {
		return world;
	}

	@Override
	public Optional<ServerWorld> getWorld() {
		return Sponge.server().worldManager().world(ResourceKey.resolve(world));
	}

	@Override
	public Positions getPositions() {
		return positions;
	}

	@Override
	public boolean isSchedule() {
		return schedule && scheduleInterval > 0 && (!blocks.isEmpty() || !serializedReserveBlocks.isEmpty());
	}

	@Override
	public MineData setSchedule(boolean schedule) {
		this.schedule = schedule;
		return instance;
	}

	@Override
	public long getScheduleInterval() {
		return scheduleInterval;
	}

	@Override
	public MineData setScheduleTime(int interval) {
		scheduleInterval = interval;
		setNextUpdate(false);
		return instance;
	}

	@Override
	public MineData removeDisplayName(Locale locale) {
		if(customNames.containsKey(locale.toLanguageTag())) customNames.remove(locale.toLanguageTag());
		return instance;
	}

	@Override
	public MineData addDisplayName(Locale locale, String name) {
		removeDisplayName(locale);
		customNames.put(locale.toLanguageTag(), name);
		return instance;
	}

	@Override
	public Component getDisplayName(Locale locale) {
		return getDisplayName(locale.toLanguageTag());
	}

	@Override
	public Component getDisplayName(String locale) {
		return customNames.isEmpty() ? Component.empty() : deserialize((customNames.containsKey(locale) ? customNames.get(locale) : customNames.getOrDefault(org.spongepowered.api.util.locale.Locales.DEFAULT.toLanguageTag(), customNames.values().iterator().next())));
	}

	@Override
	public Map<String, String> getNames() {
		return customNames;
	}

	@Override
	public Long getLastUpdate() {
		return lastUpdate;
	}

	@Override
	public Long getNextUpdate() {
		return nextUpdate;
	}

	@Override
	public MineData setNextUpdate(boolean updateLast) {
		if(updateLast) lastUpdate = System.currentTimeMillis();
		this.nextUpdate = lastUpdate + (scheduleInterval * (long) 1000);
		return instance;
	}

	@Override
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
				if(random.nextBoolean()) {
					double chance = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0, 100)).setScale(3, RoundingMode.HALF_UP).doubleValue();
					MineBlock[] blocks = this.blocks.stream().filter(b -> (b.getChance() >= chance)).toArray(MineBlock[]::new);
					MineBlock mineBlock = blocks.length == 0 ? null : blocks[0];
					if(mineBlock != null) {
						for(MineBlock block : blocks) {
							if(block.getChance() < mineBlock.getChance()) mineBlock = block;
							if(block.getChance() == mineBlock.getChance() && random.nextBoolean()) mineBlock = block;
						}
						map.put(vector3i, mineBlock.getBlockState());
					} else map.put(vector3i, getRandomReserve());
				} else map.put(vector3i, getRandomReserve());
			}
		}
		return map;
	}

	@Override
	public MineData fill(PluginContainer container, boolean actionBar, boolean debug, SourceData source) {
		if(!getWorld().isPresent()) return instance;
		long time = System.currentTimeMillis();
		ServerWorld world = getWorld().get();
		Map<Vector3i, BlockState> preparedBlocks = blocksPrepare();
		long asyncTime = System.currentTimeMillis() - time;
		Sponge.server().scheduler().submit(Task.builder().delay(1, TimeUnit.SECONDS).plugin(container).execute(() -> {
			FillEvent.Pre preEvent = new FillEvent.Pre() {

				boolean cancelled = false;
				@Override
				public Cause cause() {
					return Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, container).add(EventContextKeys.SUBJECT, source.getSubject()).add(EventContextKeys.AUDIENCE, source.getAudience()).build(), container);
				}

				@Override
				public SourceData getSource() {
					return source;
				}

				@Override
				public Mine getMine() {
					return instance;
				}

				@Override
				public ServerWorld getWorld() {
					return instance.getWorld().get();
				}

				@Override
				public Map<Vector3i, BlockState> getPreparedBlocks() {
					return preparedBlocks;
				}

				@Override
				public boolean isCancelled() {
					return cancelled;
				}

				@Override
				public void setCancelled(boolean cancel) {
					cancelled = cancel;
				}

				@Override
				public long getUsedTimeInAsync() {
					return asyncTime;
				}
				
			};
			Sponge.eventManager().post(preEvent);
			if(preEvent.isCancelled()) return;
			long syncTime = System.currentTimeMillis();
			preparedBlocks.forEach((k, v) -> {
				world.setBlock(k, v);
			});
			lastUpdate = System.currentTimeMillis();
			setNextUpdate(false);
			long fullTime = asyncTime + (System.currentTimeMillis() - syncTime);
			if(debug && logger != null) logger.info(locales.getString(org.spongepowered.api.util.locale.Locales.DEFAULT, LocalesPaths.UPDATE_CONSOLE).replace(ReplaceKeys.UUID, uniqueid.toString()).replace(ReplaceKeys.NAME, getName()).replace(ReplaceKeys.FULL_TIME, s(fullTime)).replace(ReplaceKeys.ASYNC_TIME, s(asyncTime)));
			sendMessage(actionBar, locales);
			FillEvent.Post postEvent = new FillEvent.Post() {

				@Override
				public Cause cause() {
					return Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, container).add(EventContextKeys.SUBJECT, source.getSubject()).add(EventContextKeys.AUDIENCE, source.getAudience()).build(), container);
				}

				@Override
				public SourceData getSource() {
					return source;
				}

				@Override
				public Mine getMine() {
					return instance;
				}

				@Override
				public ServerWorld getWorld() {
					return instance.getWorld().get();
				}

				@Override
				public Map<Vector3i, BlockState> getBlocks() {
					return preparedBlocks;
				}

				@Override
				public long getUsedTimeInAsync() {
					return asyncTime;
				}

				@Override
				public long getUsedTume() {
					return fullTime;
				}
				
			};
			Sponge.eventManager().post(postEvent);
			}).build());
		return instance;
	}

	/**
	 * Getting additional data that is created by other plugins.<br>
	 * After getting the data, they must be converted to the desired type.<br>
	 * Exemple: YourDataClass yourDataClass = (YourDataClass) additionalData;
	 */
	@Override
	public Optional<AdditionalData> getAdditionalData(PluginContainer container, String dataName, Class<AdditionalData> clazz) {
		if(this.additionalData == null || !this.additionalData.containsKey(container.metadata().id()) || !this.additionalData.get(container.metadata().id()).containsKey(dataName)) return Optional.empty();
		String string = this.additionalData.get(container.metadata().id()).get(dataName);
		StringReader source = new StringReader(string);
		YamlConfigurationLoader loader = YamlConfigurationLoader.builder().nodeStyle(NodeStyle.FLOW).source(() -> new BufferedReader(source)).build();
		try {
			ConfigurationNode node = loader.load();
			AdditionalData data = node.get(clazz);
			data.container = container;
			data.plugin = container.metadata().id();
			data.key = dataName;
			return Optional.ofNullable(data);
		} catch (ConfigurateException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	/**
	 * Write additional data created by another plugin.
	 */
	@Override
	public MineData addAdditionalData(AdditionalData additionalData) {
		if(this.additionalData == null) this.additionalData = new HashMap<String, Map<String, String>>();
		removeAdditionalData(additionalData.container, additionalData.plugin);
		if(!this.additionalData.containsKey(additionalData.plugin)) this.additionalData.put(additionalData.plugin, new HashMap<String, String>());
		StringWriter sink = new StringWriter();
		ConfigurationOptions options = ConfigurationOptions.defaults().serializers(
				TypeSerializerCollection.defaults().childBuilder().registerAnnotatedObjects(
						ObjectMapper.factoryBuilder().addNodeResolver(NodeResolver.onlyWithSetting()).build()).build());
		YamlConfigurationLoader loader = YamlConfigurationLoader.builder().nodeStyle(NodeStyle.FLOW).defaultOptions(options).sink(() -> new BufferedWriter(sink)).build();
		ConfigurationNode node = loader.createNode();
		try {
			node.set(AdditionalData.class, additionalData);
			if(!node.node("__class__").virtual()) node.removeChild("__class__");
			loader.save(node);
		} catch (ConfigurateException e) {
			e.printStackTrace();
		}
		String toWrite = sink.toString();
		if(toWrite.endsWith("\n")) toWrite = toWrite.substring(0, toWrite.length()-1);
		this.additionalData.get(additionalData.plugin).put(additionalData.key, toWrite);
		return instance;
	}

	/**
	 * Deleting additional data created by another plugin.
	 */
	@Override
	public MineData removeAdditionalData(PluginContainer container, String dataName) {
		if(this.additionalData.containsKey(container.metadata().id()) && this.additionalData.get(container.metadata().id()).containsKey(dataName)) this.additionalData.get(container.metadata().id()).remove(dataName);
		return instance;
	}

	private String getName() {
		Component name = getDisplayName("CONSOLE").compact();
		String toReturn = LegacyComponentSerializer.legacyAmpersand().serialize(name);
		while(toReturn.indexOf('&') != -1 && !toReturn.endsWith("&") && isStyleChar(toReturn.charAt(toReturn.indexOf("&") + 1))) toReturn = toReturn.replaceAll("&" + toReturn.charAt(toReturn.indexOf("&") + 1), "");
		return toReturn;
	}

	private boolean isStyleChar(char ch) {
		return "0123456789abcdefklmnor".indexOf(ch) != -1;
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
		MineData other = (MineData) obj;
		return Objects.equals(uniqueid, other.uniqueid);
	}

}
