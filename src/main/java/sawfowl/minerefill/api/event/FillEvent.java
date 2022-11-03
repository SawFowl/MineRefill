package sawfowl.minerefill.api.event;

import java.util.Map;
import java.util.Optional;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3i;

import sawfowl.minerefill.api.Mine;
import sawfowl.minerefill.api.SourceData;

public interface FillEvent extends Event {

	public interface Pre extends FillEvent, Cancellable {

		public Map<Vector3i, BlockState> getPreparedBlocks();

		public long getUsedTimeInAsync();

	}

	public interface Post extends FillEvent {

		public long getUsedTume();

		public long getUsedTimeInAsync();

		public Map<Vector3i, BlockState> getBlocks();

	}

	public SourceData getSource();

	public default Optional<ServerPlayer> getPlayer() {
		return getSource().getAudience() instanceof ServerPlayer ? Optional.ofNullable((ServerPlayer) getSource().getAudience()) : Optional.empty();
	}

	public Mine getMine();

	public ServerWorld getWorld();

}
