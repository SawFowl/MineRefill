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

	public Vector3i getCenter() {
		return Vector3i.from(max.x - ((max.x - min.x) / 2), max.y - ((max.y - min.y) / 2), max.z - ((max.z - min.z) / 2));
	}

	public void setFirst(Vector3i vector3i) {
		min = new Position(vector3i);
		calc();
	}

	public void setSecond(Vector3i vector3i) {
		max = new Position(vector3i);
		calc();
	}

	public void setPosition(Vector3i vector3i, boolean first) {
		if(first) {
			setFirst(vector3i);
		} else setSecond(vector3i);
	}

	public boolean isSet() {
		return min != null && max != null;
	}

	public List<Vector3i> getAllPositions() {
		List<Vector3i> allPositions = new ArrayList<Vector3i>();
		if(!isSet()) return allPositions;
		for(int x = min.x; x <= (int) max.x; x++) {
			for(int y = min.y; y <= (int) max.y; y++) {
				for(int z = min.z; z <= (int) max.z; z++) {
					allPositions.add(Vector3i.from(x, y, z));
				}
			}
		}
		return allPositions;
	}

	public List<Vector3i> getAllCorners() {
		List<Vector3i> corners = new ArrayList<Vector3i>();
		corners.add(min.getVector3i());
		
		corners.add(Vector3i.from(max.x, min.y, min.z));
		corners.add(Vector3i.from(min.x, min.y, max.z));
		corners.add(Vector3i.from(max.x, min.y, max.z));
		
		corners.add(Vector3i.from(min.x, max.y, max.z));
		corners.add(Vector3i.from(max.x, max.y, min.z));
		corners.add(Vector3i.from(min.x, max.y, min.z));
		
		corners.add(max.getVector3i());
		return corners;
	}

	private void calc() {
		if(!isSet()) return;
		Vector3i minimum = max.getVector3i().min(min.getVector3i());
		Vector3i maximum = max.getVector3i().max(min.getVector3i());
		min = new Position(minimum);
		max = new Position(maximum);
	}

}
