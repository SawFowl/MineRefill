package sawfowl.minerefill;

import java.util.UUID;

public class Permissions {

	public static final String MAIN_COMMAND = "minerefill.user.maincommand";
	public static final String EDIT = "minerefill.staff.edit";
	public static final String SAVE = "minerefill.staff.save";
	public static final String FILL = "minerefill.staff.fill";
	public static final String RELOAD = "minerefill.staff.reload";
	public static final String LIST = "minerefill.user.list";
	public static final String INFO = "minerefill.user.info";
	public static final String TELEPORT = "minerefill.user.teleport";

	public static String teleport(UUID mineUUID) {
		return "minerefill.user.teleport." + mineUUID;
	}

}
