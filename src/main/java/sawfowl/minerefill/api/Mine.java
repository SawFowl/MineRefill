package sawfowl.minerefill.api;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3i;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.text.Component;
import sawfowl.minerefill.data.AdditionalData;
import sawfowl.minerefill.data.MineBlock;
import sawfowl.minerefill.data.MineData;
import sawfowl.minerefill.data.Positions;
import sawfowl.minerefill.data.SerializedBlock;

public interface Mine {

	static Mine create(ServerWorld world) {
		return new MineData(world);
	}

	List<MineBlock> getBlocks();

	List<BlockState> getReserveBlocks();

	List<SerializedBlock> getSerializedReserveBlocks();

	UUID getUniqueid();

	Mine addReserveBlock(BlockState blockState);

	boolean containsReserverBlock(BlockState blockState);

	Mine addBlock(MineBlock mineBlock);

	String getWorldId();

	Optional<ServerWorld> getWorld();

	Positions getPositions();

	boolean isSchedule();

	Mine setSchedule(boolean schedule);

	long getScheduleInterval();

	Mine setScheduleTime(int interval);

	Mine removeDisplayName(Locale locale);

	Mine addDisplayName(Locale locale, String name);

	Component getDisplayName(Locale locale);

	Component getDisplayName(String locale);

	Map<String, String> getNames();

	Long getLastUpdate();

	Long getNextUpdate();

	Mine setNextUpdate(boolean updateLast);

	Map<Vector3i, BlockState> blocksPrepare();

	Mine fill(PluginContainer container, boolean actionBar, boolean debug, SourceData source);

	/**
	 * Getting additional data that is created by other plugins.<br>
	 * After getting the data, they must be converted to the desired type.<br>
	 * Exemple: YourDataClass yourDataClass = (YourDataClass) additionalData;
	 */
	Optional<AdditionalData> getAdditionalData(PluginContainer container, String dataName, Class<AdditionalData> clazz);

	/**
	 * Write additional data created by another plugin.
	 */
	Mine addAdditionalData(AdditionalData additionalData);

	/**
	 * Deleting additional data created by another plugin.
	 */
	Mine removeAdditionalData(PluginContainer container, String dataName);

}
