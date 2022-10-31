package sawfowl.minerefill.data;

import java.io.IOException;
import java.util.Objects;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class SerializedBlock {

	public SerializedBlock(){}

	public SerializedBlock(BlockState blockState) {
		this.blockState = blockState;
		blockId = blockID(blockState);
		if(!blockState.toContainer().get(DataQuery.of("UnsafeData")).isPresent()) return;
		try {
			nbt = DataFormats.JSON.get().write((DataView) blockState.toContainer().get(DataQuery.of("UnsafeData")).get());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	BlockState blockState;
	@Setting("BlockType")
	String blockId = "minecraft:air";
	@Setting("NBT")
	String nbt;

	public String getType() {
		return blockId;
	}

	public String getNBT() {
		return nbt;
	}

	public BlockState getBlockState() {
		try {
			return blockState != null ? blockState : deserialize();
		} catch (InvalidDataException | IOException e) {
			e.printStackTrace();
			return BlockTypes.AIR.get().defaultState();
		}
	}

	private BlockState deserialize() throws InvalidDataException, IOException {
		return blockState = nbt == null ? getBlock() : BlockState.builder().build(getBlock().toContainer().set(DataQuery.of("UnsafeData"), DataFormats.JSON.get().read(nbt))).orElse(BlockTypes.AIR.get().defaultState());
	}

	private BlockState getBlock() {
		return Sponge.game().registry(RegistryTypes.BLOCK_TYPE).findValue(ResourceKey.resolve(blockId)).orElse(BlockTypes.AIR.get()).defaultState();
	}

	private String blockID(BlockState block) {
		return Sponge.game().registry(RegistryTypes.BLOCK_TYPE).valueKey(block.type()).asString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(blockId, nbt);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerializedBlock other = (SerializedBlock) obj;
		return Objects.equals(blockId, other.blockId) && Objects.equals(nbt, other.nbt);
	}
}
