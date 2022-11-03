package sawfowl.minerefill.api.event;

import org.spongepowered.api.event.Event;

import sawfowl.minerefill.api.MineAPI;

public interface PostMineAPIEvent extends Event {

	public MineAPI getAPI();

}
