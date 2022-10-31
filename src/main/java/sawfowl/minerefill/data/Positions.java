package sawfowl.minerefill.data;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.math.vector.Vector3i;

@ConfigSerializable
public class Positions {

	public Positions(){}

	@Setting("Min")
	private Position min;
	@Setting("Max")
	private Position max;

	public Vector3i getMin() {
		return min == null ? null : min.getVector3i();
	}

	public Vector3i getMax() {
		return max == null ? null : max.getVector3i();
	}

	public void setFirst(Vector3i vector3i) {
		min = new Position(vector3i);
	}

	public void setSecond(Vector3i vector3i) {
		if(min == null) {
			min = new Position(vector3i);
			return;
		} else {
			max = new Position(vector3i);
		}
		if(min != null && max != null) {
			Vector3i minimum = max.getVector3i().min(min.getVector3i());
			Vector3i maximum = max.getVector3i().max(min.getVector3i());
			min = new Position(minimum);
			max = new Position(maximum);
		}
	}

	public void setPosition(Vector3i vector3i, boolean first) {
		if(first) {
			setFirst(vector3i);
		} else {
			setSecond(vector3i);
		}
	}

	public List<Vector3i> getAllPositions() {
		List<Vector3i> allPositions = new ArrayList<Vector3i>();
		if(min == null || max == null) return allPositions;
		for(int x = min.x; x <= (int) max.x; x++) {
			for(int y = min.y; y <= (int) max.y; y++) {
				for(int z = min.z; z <= (int) max.z; z++) {
					allPositions.add(Vector3i.from(x, y, z));
				}
			}
		}
		return allPositions;
	}

}
