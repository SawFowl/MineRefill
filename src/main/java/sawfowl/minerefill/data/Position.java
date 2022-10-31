package sawfowl.minerefill.data;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.math.vector.Vector3i;

@ConfigSerializable
public class Position {

	public Position(){}

	public Position(Vector3i vector3i) {
		x = vector3i.x();
		y = vector3i.y();
		z = vector3i.z();
	}

	@Setting("X")
	int x;
	@Setting("Y")
	int y;
	@Setting("Z")
	int z;

	public Vector3i getVector3i() {
		return Vector3i.from(x, y, z);
	}

}
