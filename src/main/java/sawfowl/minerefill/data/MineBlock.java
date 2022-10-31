package sawfowl.minerefill.data;

import java.util.Objects;
import java.util.Optional;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class MineBlock {

	public MineBlock(){}

	public MineBlock(BlockState block, double chance) {
		this.block = new SerializedBlock(block);
		this.chance = chance;
	}

	@Setting("Block")
	private SerializedBlock block;
	@Setting("Chance")
	private double chance;

	public BlockState getBlockState() {
		return block.getBlockState();
	}

	public SerializedBlock getSerializedBlock() {
		return block;
	}

	public double getChance() {
		return chance;
	}

	public Optional<BlockState> trySelectBlock(double chance) {
		return this.chance > 0 && this.chance > chance ? Optional.ofNullable(getBlockState()) : Optional.empty();
	}

	public boolean lessThanAnother(MineBlock mineBlock) {
		return chance < mineBlock.chance;
	}

	@Override
	public int hashCode() {
		return Objects.hash(block, chance);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MineBlock other = (MineBlock) obj;
		return Objects.equals(block, other.block)
				&& Double.doubleToLongBits(chance) == Double.doubleToLongBits(other.chance);
	}

}
