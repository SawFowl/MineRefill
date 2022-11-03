package sawfowl.minerefill.configure;

public class LocalesPaths {


	private static final Object PATH_EXCEPTIONS = "Exception";
	private static final Object PATH_COMMANDS = "Commands";
	private static final Object PATH_MISC = "Commands";
	private static final Object PATH_TITLE = "Title";
	private static final Object PATH_SUCCESS = "Success";
	private static final Object PATH_COMMAND_MAIN = "Main";
	private static final Object PATH_COMMAND_CREATE = "Create";
	private static final Object PATH_COMMAND_DELETE = "Delete";
	private static final Object PATH_COMMAND_SETPOS = "SetPos";
	private static final Object PATH_COMMAND_ADD_BLOCK = "AddBlock";
	private static final Object PATH_COMMAND_ADD_RESERVE_BLOCK = "AddReserveBlock";
	private static final Object PATH_COMMAND_INTERVAL = "Interval";
	private static final Object PATH_COMMAND_SCHEDULE = "Schedule";
	private static final Object PATH_COMMAND_SET_NAME = "SetName";
	private static final Object PATH_COMMAND_INFO = "Info";
	private static final Object PATH_COMMAND_SAVE = "Save";
	private static final Object PATH_COMMAND_FILL = "Fill";
	private static final Object PATH_COMMAND_LIST = "List";
	private static final Object PATH_COMMAND_SELECT = "Select";
	private static final Object PATH_COMMAND_RELOAD = "Reload";
	private static final Object PATH_UPDATE = "Update";
	private static final Object PATH_COMPLETED = "Completed";
	private static final Object PATH_NOTIFICATIONS = "Notifications";
	private static final Object PATH_MINUTES = "Minutes";
	private static final Object PATH_SECONDS = "Seconds";

	public static final Object[] ONLY_PLAYER = {PATH_COMMANDS, PATH_EXCEPTIONS, "OnlyPlayer"};

	public static final Object[] LOADED_MINES = {PATH_MISC, "LoadedMines"};
	public static final Object[] UPDATE_CONSOLE = {PATH_MISC, PATH_UPDATE, PATH_COMPLETED, "Console"};
	public static final Object[] UPDATE_PLAYERS = {PATH_MISC, PATH_UPDATE, PATH_COMPLETED, "Players"};
	public static final Object[] NOTIFICATION_10M = {PATH_MISC, PATH_UPDATE, PATH_NOTIFICATIONS, PATH_MINUTES, "10"};
	public static final Object[] NOTIFICATION_5M = {PATH_MISC, PATH_UPDATE, PATH_NOTIFICATIONS, PATH_MINUTES, "5"};
	public static final Object[] NOTIFICATION_3M = {PATH_MISC, PATH_UPDATE, PATH_NOTIFICATIONS, PATH_MINUTES, "3"};
	public static final Object[] NOTIFICATION_2M = {PATH_MISC, PATH_UPDATE, PATH_NOTIFICATIONS, PATH_MINUTES, "2"};
	public static final Object[] NOTIFICATION_1M = {PATH_MISC, PATH_UPDATE, PATH_NOTIFICATIONS, PATH_MINUTES, "1"};
	public static final Object[] NOTIFICATION_30S = {PATH_MISC, PATH_UPDATE, PATH_NOTIFICATIONS, PATH_SECONDS, "30"};
	public static final Object[] NOTIFICATION_10S = {PATH_MISC, PATH_UPDATE, PATH_NOTIFICATIONS, PATH_SECONDS, "10"};
	public static final Object[] NOTIFICATION_9S = {PATH_MISC, PATH_UPDATE, PATH_NOTIFICATIONS, PATH_SECONDS, "9"};
	public static final Object[] NOTIFICATION_8S = {PATH_MISC, PATH_UPDATE, PATH_NOTIFICATIONS, PATH_SECONDS, "8"};
	public static final Object[] NOTIFICATION_7S = {PATH_MISC, PATH_UPDATE, PATH_NOTIFICATIONS, PATH_SECONDS, "7"};
	public static final Object[] NOTIFICATION_6S = {PATH_MISC, PATH_UPDATE, PATH_NOTIFICATIONS, PATH_SECONDS, "6"};
	public static final Object[] NOTIFICATION_5S = {PATH_MISC, PATH_UPDATE, PATH_NOTIFICATIONS, PATH_SECONDS, "5"};
	public static final Object[] NOTIFICATION_4S = {PATH_MISC, PATH_UPDATE, PATH_NOTIFICATIONS, PATH_SECONDS, "4"};
	public static final Object[] NOTIFICATION_3S = {PATH_MISC, PATH_UPDATE, PATH_NOTIFICATIONS, PATH_SECONDS, "3"};
	public static final Object[] NOTIFICATION_2S = {PATH_MISC, PATH_UPDATE, PATH_NOTIFICATIONS, PATH_SECONDS, "2"};
	public static final Object[] NOTIFICATION_1S = {PATH_MISC, PATH_UPDATE, PATH_NOTIFICATIONS, PATH_SECONDS, "1"};

	public static final Object[] PADDING = {PATH_COMMANDS, PATH_MISC, "Padding"};
	public static final Object[] ENABLE = {PATH_COMMANDS, PATH_MISC, "Enable"};
	public static final Object[] DISABLE = {PATH_COMMANDS, PATH_MISC, "Disable"};
	public static final Object[] REMOVE = {PATH_COMMANDS, PATH_MISC, "Remove"};
	public static final Object[] NOT_SELECTED = {PATH_COMMANDS, PATH_MISC, PATH_EXCEPTIONS, "Notselected"};

	public static final Object[] MAIN_TITLE = {PATH_COMMANDS, PATH_COMMAND_MAIN, PATH_TITLE};
	public static final Object[] MAIN_CREATE = {PATH_COMMANDS, PATH_COMMAND_MAIN, PATH_COMMAND_CREATE};
	public static final Object[] MAIN_DELETE = {PATH_COMMANDS, PATH_COMMAND_MAIN, PATH_COMMAND_DELETE};
	public static final Object[] MAIN_SETPOS = {PATH_COMMANDS, PATH_COMMAND_MAIN, PATH_COMMAND_SETPOS};
	public static final Object[] MAIN_ADD_BLOCK = {PATH_COMMANDS, PATH_COMMAND_MAIN, PATH_COMMAND_ADD_BLOCK};
	public static final Object[] MAIN_ADD_RESERVE_BLOCK = {PATH_COMMANDS, PATH_COMMAND_MAIN, PATH_COMMAND_ADD_RESERVE_BLOCK};
	public static final Object[] MAIN_INTERVAL = {PATH_COMMANDS, PATH_COMMAND_MAIN, PATH_COMMAND_INTERVAL};
	public static final Object[] MAIN_SCHEDULE = {PATH_COMMANDS, PATH_COMMAND_MAIN, PATH_COMMAND_SCHEDULE};
	public static final Object[] MAIN_SET_NAME = {PATH_COMMANDS, PATH_COMMAND_MAIN, PATH_COMMAND_SET_NAME};
	public static final Object[] MAIN_INFO = {PATH_COMMANDS, PATH_COMMAND_MAIN, PATH_COMMAND_INFO};
	public static final Object[] MAIN_SAVE = {PATH_COMMANDS, PATH_COMMAND_MAIN, PATH_COMMAND_SAVE};
	public static final Object[] MAIN_FILL = {PATH_COMMANDS, PATH_COMMAND_MAIN, PATH_COMMAND_FILL};
	public static final Object[] MAIN_LIST = {PATH_COMMANDS, PATH_COMMAND_MAIN, PATH_COMMAND_LIST};
	public static final Object[] MAIN_SELECT = {PATH_COMMANDS, PATH_COMMAND_MAIN, PATH_COMMAND_SELECT};
	public static final Object[] MAIN_RELOAD = {PATH_COMMANDS, PATH_COMMAND_MAIN, PATH_COMMAND_RELOAD};

	public static final Object[] CREATE_EDIT_OTHER = {PATH_COMMANDS, PATH_COMMAND_CREATE, "EditOther"};
	public static final Object[] CREATE_SUCCESS = {PATH_COMMANDS, PATH_COMMAND_CREATE, "Success"};

	public static final Object[] DELETE_UNSAVED = {PATH_COMMANDS, PATH_COMMAND_DELETE, PATH_EXCEPTIONS, "Unsaved"};
	public static final Object[] DELETE_SUCCESS = {PATH_COMMANDS, PATH_COMMAND_DELETE, "Success"};

	public static final Object[] SETPOS_UNSELECTED = {PATH_COMMANDS, PATH_COMMAND_SETPOS, PATH_EXCEPTIONS, "Unselected"};
	public static final Object[] SETPOS_SUCCESS = {PATH_COMMANDS, PATH_COMMAND_SETPOS, "Success"};

	public static final Object[] ADD_BLOCK_CHANCE_NOT_PRESENT = {PATH_COMMANDS, PATH_COMMAND_ADD_BLOCK, PATH_EXCEPTIONS, "ChanceNotPresent"};
	public static final Object[] ADD_BLOCK_BLOCK_NOT_PRESENT = {PATH_COMMANDS, PATH_COMMAND_ADD_BLOCK,  PATH_EXCEPTIONS, "BlockNotPresent"};
	public static final Object[] ADD_BLOCK_ALREADY_EXIST = {PATH_COMMANDS, PATH_COMMAND_ADD_BLOCK,  PATH_EXCEPTIONS, "AlreadyExist"};
	public static final Object[] ADD_BLOCK_SUCCESS = {PATH_COMMANDS, PATH_COMMAND_ADD_BLOCK, PATH_SUCCESS};

	public static final Object[] ADD_RESERVE_BLOCK_BLOCK_NOT_PRESENT = {PATH_COMMANDS, PATH_COMMAND_ADD_RESERVE_BLOCK, PATH_EXCEPTIONS, "BlockNotPresent"};
	public static final Object[] ADD_RESERVE_BLOCK_ALREADY_EXIST = {PATH_COMMANDS, PATH_COMMAND_ADD_RESERVE_BLOCK,  PATH_EXCEPTIONS, "AlreadyExist"};
	public static final Object[] ADD_RESERVE_BLOCK_SUCCESS = {PATH_COMMANDS, PATH_COMMAND_ADD_RESERVE_BLOCK, PATH_SUCCESS};

	public static final Object[] INTERVAL_NOT_PRESENT = {PATH_COMMANDS, PATH_COMMAND_INTERVAL, PATH_EXCEPTIONS, "NotPresent"};
	public static final Object[] INTERVAL_SUCCESS = {PATH_COMMANDS, PATH_COMMAND_INTERVAL, PATH_SUCCESS};

	public static final Object[] SCHEDULE_NOT_PRESENT = {PATH_COMMANDS, PATH_COMMAND_SCHEDULE, PATH_EXCEPTIONS, "NotPresent"};
	public static final Object[] SCHEDULE_SUCCESS = {PATH_COMMANDS, PATH_COMMAND_SCHEDULE, PATH_SUCCESS};

	public static final Object[] SET_NAME_LOCALE_NOT_PRESENT = {PATH_COMMANDS, PATH_COMMAND_SCHEDULE, PATH_EXCEPTIONS, "LocaleNotPresent"};
	public static final Object[] SET_NAME_NAME_NOT_PRESENT = {PATH_COMMANDS, PATH_COMMAND_SCHEDULE, PATH_EXCEPTIONS, "NameNotPresent"};
	public static final Object[] SET_NAME_SUCCESS = {PATH_COMMANDS, PATH_COMMAND_SET_NAME, PATH_SUCCESS};

	public static final Object[] INFO_MAIN_TITLE = {PATH_COMMANDS, PATH_COMMAND_INFO, PATH_TITLE, PATH_COMMAND_MAIN};
	public static final Object[] INFO_BLOCKS_LIST_TITLE = {PATH_COMMANDS, PATH_COMMAND_INFO, PATH_TITLE, "BlocksList"};
	public static final Object[] INFO_RESERVE_BLOCKS_LIST_TITLE = {PATH_COMMANDS, PATH_COMMAND_INFO, PATH_TITLE, "ReserveBlocksList"};
	public static final Object[] INFO_NAMES_LIST_TITLE = {PATH_COMMANDS, PATH_COMMAND_INFO, PATH_TITLE, "Names"};
	public static final Object[] INFO_UUID = {PATH_COMMANDS, PATH_COMMAND_INFO, "UUID"};
	public static final Object[] INFO_COORDS = {PATH_COMMANDS, PATH_COMMAND_INFO, "Coords"};
	public static final Object[] INFO_AUTO_UPDATE = {PATH_COMMANDS, PATH_COMMAND_INFO, "AutoUpdate"};
	public static final Object[] INFO_UPDATE_INTERVAL = {PATH_COMMANDS, PATH_COMMAND_INFO, "UpdateInterval"};
	public static final Object[] INFO_BLOCKS_VARIANTS = {PATH_COMMANDS, PATH_COMMAND_INFO, "BlocksVariants"};
	public static final Object[] INFO_RESERVE_BLOCKS_VARIANTS = {PATH_COMMANDS, PATH_COMMAND_INFO, "ReserveBlocksVariants"};
	public static final Object[] INFO_NAMES_VARIANTS = {PATH_COMMANDS, PATH_COMMAND_INFO, "NamesVariants"};
	public static final Object[] INFO_CLICK_TO_REMOVE = {PATH_COMMANDS, PATH_COMMAND_INFO, "ClickToRemove"};
	public static final Object[] INFO_HOVER = {PATH_COMMANDS, PATH_COMMAND_INFO, "Hover"};

	public static final Object[] SAVE_POSITIONS_NOT_PRESENT = {PATH_COMMANDS, PATH_COMMAND_SAVE, PATH_EXCEPTIONS, "PositionsNotPresent"};
	public static final Object[] SAVE_BLOCKS_NOT_PRESENT = {PATH_COMMANDS, PATH_COMMAND_SAVE, PATH_EXCEPTIONS, "BlocksNotPresent"};
	public static final Object[] SAVE_SUCCESS = {PATH_COMMANDS, PATH_COMMAND_SAVE, PATH_SUCCESS};

	public static final Object[] FILL_SUCCESS = {PATH_COMMANDS, PATH_COMMAND_FILL, PATH_SUCCESS};

	public static final Object[] LIST_TITLE = {PATH_COMMANDS, PATH_COMMAND_LIST, PATH_TITLE};
	public static final Object[] LIST_WORLD_NOT_LOADED = {PATH_COMMANDS, PATH_COMMAND_LIST, PATH_EXCEPTIONS, "WorldNotLoaded"};
	public static final Object[] LIST_MINE_TELEPORT_ALLOWED = {PATH_COMMANDS, PATH_COMMAND_LIST, "TeleportAllowed"};
	public static final Object[] LIST_MINE_TELEPORT_DISALLOWED = {PATH_COMMANDS, PATH_COMMAND_LIST, "TeleportDisallowed"};
	public static final Object[] LIST_MINE_SELECTED = {PATH_COMMANDS, PATH_COMMAND_LIST, "MineSelected"};
	public static final Object[] LIST_INFO = {PATH_COMMANDS, PATH_COMMAND_LIST, PATH_COMMAND_INFO};

	public static final Object[] SELECT_EXCEPTION = {PATH_COMMANDS, PATH_COMMAND_SELECT, PATH_EXCEPTIONS, "NotFound"};

	public static final Object[] RELOAD_SUCCESS = {PATH_COMMANDS, PATH_COMMAND_RELOAD, PATH_SUCCESS};

}
