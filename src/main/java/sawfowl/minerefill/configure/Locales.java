package sawfowl.minerefill.configure;

import java.util.Locale;
import java.util.Map;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import sawfowl.localeapi.api.ConfigTypes;
import sawfowl.localeapi.api.LocaleService;
import sawfowl.localeapi.utils.AbstractLocaleUtil;

public class Locales {

	private final LocaleService localeService;
	private final boolean json;
	private final String pluginid = "minerefill";
	public Locales(LocaleService localeService, boolean json) {
		this.localeService = localeService;
		this.json = json;
		generateDefault(localeService.createPluginLocale(pluginid, ConfigTypes.JSON, org.spongepowered.api.util.locale.Locales.DEFAULT));
		generateRu(localeService.createPluginLocale(pluginid, ConfigTypes.JSON, org.spongepowered.api.util.locale.Locales.RU_RU));
	}

	public String getString(Locale locale, Object... path) {
		return LegacyComponentSerializer.legacyAmpersand().serialize(getText(locale, path));
	}

	public Component getText(Locale locale, Object... path) {
		return getAbstractLocaleUtil(locale).getComponent(json, path);
	}

	public Component getTextReplaced1(Locale locale, Map<String, String> map, Object... path) {
		return getAbstractLocaleUtil(locale).getComponentReplaced1(map, json, path);
	}

	public Component getTextReplaced2(Locale locale, Map<String, Component> map, Object... path) {
		return getAbstractLocaleUtil(locale).getComponentReplaced2(map, json, path);
	}

	public Component getTextFromDefault(Object... path) {
		return getAbstractLocaleUtil(org.spongepowered.api.util.locale.Locales.DEFAULT).getComponent(json, path);
	}

	public LocaleService getLocaleService() {
		return localeService;
	}

	public AbstractLocaleUtil getAbstractLocaleUtil(Locale locale) {
		return localeService.getPluginLocales(pluginid).get(locale);
	}

	private Component toText(String string) {
		return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
	}

	private boolean check(boolean save, AbstractLocaleUtil localeUtil, Component value, String comment, Object... path) {
		return localeUtil.checkComponent(json, value, comment, path) || save;
	}

	private void save(AbstractLocaleUtil localeUtil) {
		localeUtil.saveLocaleNode();
	}

	private void generateDefault(AbstractLocaleUtil localeUtil) {

		boolean save = check(false, localeUtil, toText("&cOnly the player can execute this command."), null, LocalesPaths.ONLY_PLAYER);

		save = check(save, localeUtil, toText("Loaded mines: " + ReplaceKeys.SIZE), null, LocalesPaths.LOADED_MINES);
		save = check(save, localeUtil, toText("Mine " + ReplaceKeys.UUID + " " + ReplaceKeys.NAME + " updated in " + ReplaceKeys.FULL_TIME + "ms. Of these in asynchronous mode - " + ReplaceKeys.ASYNC_TIME + "ms."), null, LocalesPaths.UPDATE_CONSOLE);
		save = check(save, localeUtil, toText("&6Mine " + ReplaceKeys.NAME + "&6 has been completely updated."), null, LocalesPaths.UPDATE_PLAYERS);
		save = check(save, localeUtil, toText("&eMine " + ReplaceKeys.NAME + "&e will be updated in &b10 &eminutes."), null, LocalesPaths.NOTIFICATION_10M);
		save = check(save, localeUtil, toText("&eMine " + ReplaceKeys.NAME + "&e will be updated in &b5 &eminutes."), null, LocalesPaths.NOTIFICATION_5M);
		save = check(save, localeUtil, toText("&eMine " + ReplaceKeys.NAME + "&e will be updated in &b3 &eminutes."), null, LocalesPaths.NOTIFICATION_3M);
		save = check(save, localeUtil, toText("&eMine " + ReplaceKeys.NAME + "&e will be updated in &b2 &eminutes."), null, LocalesPaths.NOTIFICATION_2M);
		save = check(save, localeUtil, toText("&eMine " + ReplaceKeys.NAME + "&e will be updated in &b1 &eminute."), null, LocalesPaths.NOTIFICATION_1M);
		save = check(save, localeUtil, toText("&eMine " + ReplaceKeys.NAME + "&e will be updated in &b30 &eseconds."), null, LocalesPaths.NOTIFICATION_30S);
		save = check(save, localeUtil, toText("&eLaunch update mine " + ReplaceKeys.NAME + "&e in &b10 &eseconds."), null, LocalesPaths.NOTIFICATION_10S);
		save = check(save, localeUtil, toText("&eLaunch update mine " + ReplaceKeys.NAME + "&e in &b9 &eseconds."), null, LocalesPaths.NOTIFICATION_9S);
		save = check(save, localeUtil, toText("&eLaunch update mine " + ReplaceKeys.NAME + "&e in &b8 &eseconds."), null, LocalesPaths.NOTIFICATION_8S);
		save = check(save, localeUtil, toText("&eLaunch update mine " + ReplaceKeys.NAME + "&e in &b7 &eseconds."), null, LocalesPaths.NOTIFICATION_7S);
		save = check(save, localeUtil, toText("&eLaunch update mine " + ReplaceKeys.NAME + "&e in &b6 &eseconds."), null, LocalesPaths.NOTIFICATION_6S);
		save = check(save, localeUtil, toText("&eLaunch update mine " + ReplaceKeys.NAME + "&e in &b5 &eseconds."), null, LocalesPaths.NOTIFICATION_5S);
		save = check(save, localeUtil, toText("&eLaunch update mine " + ReplaceKeys.NAME + "&e in &b4 &eseconds."), null, LocalesPaths.NOTIFICATION_4S);
		save = check(save, localeUtil, toText("&eLaunch update mine " + ReplaceKeys.NAME + "&e in &b3 &eseconds."), null, LocalesPaths.NOTIFICATION_3S);
		save = check(save, localeUtil, toText("&eLaunch update mine " + ReplaceKeys.NAME + "&e in &b2 &eseconds."), null, LocalesPaths.NOTIFICATION_2S);
		save = check(save, localeUtil, toText("&eLaunch update mine " + ReplaceKeys.NAME + "&e in &b1 &esecond."), null, LocalesPaths.NOTIFICATION_1S);

		save = check(save, localeUtil, toText("&3="), null, LocalesPaths.PADDING);
		save = check(save, localeUtil, toText("&7[&aENABLED&7]"), null, LocalesPaths.ENABLE);
		save = check(save, localeUtil, toText("&7[&cDISABLED&7]"), null, LocalesPaths.DISABLE);
		save = check(save, localeUtil, toText("&7[&cRemove&7]"), null, LocalesPaths.REMOVE);
		save = check(save, localeUtil, toText("&cSelect a mine via the \"/minerefill list\" command"), null, LocalesPaths.NOT_SELECTED);

		save = check(save, localeUtil, toText("&3List of commands"), null, LocalesPaths.MAIN_TITLE);
		save = check(save, localeUtil, toText("&f- &aCreate mine."), null, LocalesPaths.MAIN_CREATE);
		save = check(save, localeUtil, toText("&f- &aDelete selected mine."), null, LocalesPaths.MAIN_DELETE);
		save = check(save, localeUtil, toText("&f- &aSet position 1 or 2."), null, LocalesPaths.MAIN_SETPOS);
		save = check(save, localeUtil, toText("&f- &aAdd a block to the mine that you are looking at."), null, LocalesPaths.MAIN_ADD_BLOCK);
		save = check(save, localeUtil, toText("&f- &aAdd a reserve block to the mine that you are looking at."), null, LocalesPaths.MAIN_ADD_RESERVE_BLOCK);
		save = check(save, localeUtil, toText("&f- &aTime in seconds between mine updates."), null, LocalesPaths.MAIN_INTERVAL);
		save = check(save, localeUtil, toText("&f- &aEnable/disable automatic filling of the mine."), null, LocalesPaths.MAIN_SCHEDULE);
		save = check(save, localeUtil, toText("&f- &aSetting the name of the mine."), null, LocalesPaths.MAIN_SET_NAME);
		save = check(save, localeUtil, toText("&f- &aInformation about the mine."), null, LocalesPaths.MAIN_INFO);
		save = check(save, localeUtil, toText("&f- &aSave the selected mine."), null, LocalesPaths.MAIN_SAVE);
		save = check(save, localeUtil, toText("&f- &aFill the mine."), null, LocalesPaths.MAIN_FILL);
		save = check(save, localeUtil, toText("&f- &aList of mines."), null, LocalesPaths.MAIN_LIST);
		save = check(save, localeUtil, toText("&f- &aSelecting a mine by its UUID."), null, LocalesPaths.MAIN_SELECT);
		save = check(save, localeUtil, toText("&f- &aReloading the plugin."), null, LocalesPaths.MAIN_RELOAD);

		save = check(save, localeUtil, toText("&eYou are already editing another mine. Click on this message to confirm the creation of a new mine."), null, LocalesPaths.CREATE_EDIT_OTHER);
		save = check(save, localeUtil, toText("&aThe mine has been created. Set the coordinates and other settings, and then you can save it."), null, LocalesPaths.CREATE_SUCCESS);

		save = check(save, localeUtil, toText("&cThe mine being edited was not previously saved."), null, LocalesPaths.DELETE_UNSAVED);
		save = check(save, localeUtil, toText("&aYou removed the mine."), null, LocalesPaths.DELETE_SUCCESS);

		save = check(save, localeUtil, toText("&cSelect which position you want to set."), null, LocalesPaths.SETPOS_UNSELECTED);
		save = check(save, localeUtil, toText("&aYou have set the position &b№" + ReplaceKeys.POSITION + "&a."), null, LocalesPaths.SETPOS_SUCCESS);

		save = check(save, localeUtil, toText("&cSpecify the chance of the block appearing."), null, LocalesPaths.ADD_BLOCK_CHANCE_NOT_PRESENT);
		save = check(save, localeUtil, toText("&cYou must look at the block. Maximum block search distance = 5."), null, LocalesPaths.ADD_BLOCK_BLOCK_NOT_PRESENT);
		save = check(save, localeUtil, toText("&cMine already contains such a block with the same chance."), null, LocalesPaths.ADD_BLOCK_ALREADY_EXIST);
		save = check(save, localeUtil, toText("&aYou added block &e\"&b" + ReplaceKeys.BLOCK + "&e\"&a with a chance of choice = &b" + ReplaceKeys.CHANCE + "%&a."), null, LocalesPaths.ADD_BLOCK_SUCCESS);

		save = check(save, localeUtil, toText("&cYou must look at the block. Maximum block search distance = 5."), null, LocalesPaths.ADD_RESERVE_BLOCK_BLOCK_NOT_PRESENT);
		save = check(save, localeUtil, toText("&cThis block is already a reserve in the selected mine."), null, LocalesPaths.ADD_RESERVE_BLOCK_ALREADY_EXIST);
		save = check(save, localeUtil, toText("&aYou have added a reserve block &e\"&b" + ReplaceKeys.BLOCK + "&e\" &ato the mine."), null, LocalesPaths.ADD_RESERVE_BLOCK_SUCCESS);

		save = check(save, localeUtil, toText("&cSpecify the time between updates in seconds."), null, LocalesPaths.INTERVAL_NOT_PRESENT);
		save = check(save, localeUtil, toText("&aNew interval between mine updates = &b" + ReplaceKeys.TIME + "&a."), null, LocalesPaths.INTERVAL_SUCCESS);

		save = check(save, localeUtil, toText("&cYou did not specify whether the mine should automatically update."), null, LocalesPaths.SCHEDULE_NOT_PRESENT);
		save = check(save, localeUtil, toText("&aMine auto-update: " + ReplaceKeys.VALUE + "&a."), null, LocalesPaths.SCHEDULE_SUCCESS);

		save = check(save, localeUtil, toText("&cSpecify the localization to set the name."), null, LocalesPaths.SET_NAME_LOCALE_NOT_PRESENT);
		save = check(save, localeUtil, toText("&cSpecify the name of the mine."), null, LocalesPaths.SET_NAME_NAME_NOT_PRESENT);
		save = check(save, localeUtil, toText("&aYou have set the name of the mine."), null, LocalesPaths.SET_NAME_SUCCESS);

		save = check(save, localeUtil, toText("&bMine info"), null, LocalesPaths.INFO_MAIN_TITLE);
		save = check(save, localeUtil, toText("&bBlocks list"), null, LocalesPaths.INFO_BLOCKS_LIST_TITLE);
		save = check(save, localeUtil, toText("&bReserve blocks list"), null, LocalesPaths.INFO_RESERVE_BLOCKS_LIST_TITLE);
		save = check(save, localeUtil, toText("&bNames list"), null, LocalesPaths.INFO_NAMES_LIST_TITLE);
		save = check(save, localeUtil, toText("&eUUID&f: &b" + ReplaceKeys.VALUE), null, LocalesPaths.INFO_UUID);
		save = check(save, localeUtil, toText("&eCoordinates&f: &b" + ReplaceKeys.MIN + " &3➣ &b" + ReplaceKeys.MAX), null, LocalesPaths.INFO_COORDS);
		save = check(save, localeUtil, toText("&eAutoupdate&f: &b" + ReplaceKeys.VALUE), null, LocalesPaths.INFO_AUTO_UPDATE);
		save = check(save, localeUtil, toText("&eAuto update interval&f: &b" + ReplaceKeys.VALUE), null, LocalesPaths.INFO_UPDATE_INTERVAL);
		save = check(save, localeUtil, toText("&eBlocks&f: &b" + ReplaceKeys.VALUE), null, LocalesPaths.INFO_BLOCKS_VARIANTS);
		save = check(save, localeUtil, toText("&eReserve blocks&f: &b" + ReplaceKeys.VALUE), null, LocalesPaths.INFO_RESERVE_BLOCKS_VARIANTS);
		save = check(save, localeUtil, toText("&eLocalized names&f: &b" + ReplaceKeys.VALUE), null, LocalesPaths.INFO_NAMES_VARIANTS);
		save = check(save, localeUtil, toText("&5Click to remove"), null, LocalesPaths.INFO_CLICK_TO_REMOVE);
		save = check(save, localeUtil, toText("&5Click to display the list"), null, LocalesPaths.INFO_HOVER);

		save = check(save, localeUtil, toText("&cMine does not have one or both positions set."), null, LocalesPaths.SAVE_POSITIONS_NOT_PRESENT);
		save = check(save, localeUtil, toText("&cThere are no blocks in the mine."), null, LocalesPaths.SAVE_BLOCKS_NOT_PRESENT);
		save = check(save, localeUtil, toText("&aMine has been saved."), null, LocalesPaths.SAVE_SUCCESS);

		save = check(save, localeUtil, toText("&aMine " + ReplaceKeys.NAME + "&a update launched."), null, LocalesPaths.FILL_SUCCESS);

		save = check(save, localeUtil, toText("&bMines list"), null, LocalesPaths.LIST_TITLE);
		save = check(save, localeUtil, toText("&cWorld &e\"&b" + ReplaceKeys.NAME + "&e\"&c is not loaded."), null, LocalesPaths.LIST_WORLD_NOT_LOADED);
		save = check(save, localeUtil, toText("&7[&aTP&7] "), null, LocalesPaths.LIST_MINE_TELEPORT_ALLOWED);
		save = check(save, localeUtil, toText("&7[&cTP&6] "), null, LocalesPaths.LIST_MINE_TELEPORT_DISALLOWED);
		save = check(save, localeUtil, toText("&aYou have selected mine " + ReplaceKeys.NAME + "&a."), null, LocalesPaths.LIST_MINE_SELECTED);
		save = check(save, localeUtil, toText("&bClicking on a mine will select it to get information, edit it, or manually filling. The available actions depend on the permissions you have."), null, LocalesPaths.LIST_INFO);

		save = check(save, localeUtil, toText("&cMine with this UUID was not found."), null, LocalesPaths.SELECT_EXCEPTION);

		save = check(save, localeUtil, toText("&aPlugin reloaded."), null, LocalesPaths.RELOAD_SUCCESS);
		
		if(save) save(localeUtil);
	}

	private void generateRu(AbstractLocaleUtil localeUtil) {

		boolean save = check(false, localeUtil, toText("Только игрок может выполнять эту команду."), null, LocalesPaths.ONLY_PLAYER);

		save = check(save, localeUtil, toText("Загружено шахт: " + ReplaceKeys.SIZE), null, LocalesPaths.LOADED_MINES);
		save = check(save, localeUtil, toText("Шахта " + ReplaceKeys.UUID + " " + ReplaceKeys.NAME + " обновлена за " + ReplaceKeys.FULL_TIME + "мс. Из них в асинхронном режиме - " + ReplaceKeys.ASYNC_TIME + "мс."), null, LocalesPaths.UPDATE_CONSOLE);
		save = check(save, localeUtil, toText("&6Шахта " + ReplaceKeys.NAME + "&6 полностью обновлена."), null, LocalesPaths.UPDATE_PLAYERS);
		save = check(save, localeUtil, toText("&eШахта " + ReplaceKeys.NAME + "&e будет обновлена через &b10 &eминут."), null, LocalesPaths.NOTIFICATION_10M);
		save = check(save, localeUtil, toText("&eШахта " + ReplaceKeys.NAME + "&e будет обновлена через &b5 &eминут."), null, LocalesPaths.NOTIFICATION_5M);
		save = check(save, localeUtil, toText("&eШахта " + ReplaceKeys.NAME + "&e будет обновлена через &b3 &eминуты."), null, LocalesPaths.NOTIFICATION_3M);
		save = check(save, localeUtil, toText("&eШахта " + ReplaceKeys.NAME + "&e будет обновлена через &b2 &eминуты."), null, LocalesPaths.NOTIFICATION_2M);
		save = check(save, localeUtil, toText("&eШахта " + ReplaceKeys.NAME + "&e будет обновлена через &b1 &eминуту."), null, LocalesPaths.NOTIFICATION_1M);
		save = check(save, localeUtil, toText("&eШахта " + ReplaceKeys.NAME + "&e будет обновлена через &b30 &eсекунд."), null, LocalesPaths.NOTIFICATION_30S);
		save = check(save, localeUtil, toText("&eЗапуск обновления шахты " + ReplaceKeys.NAME + "&e через &b10 &eсекунд."), null, LocalesPaths.NOTIFICATION_10S);
		save = check(save, localeUtil, toText("&eЗапуск обновления шахты " + ReplaceKeys.NAME + "&e через &b9 &eсекунд."), null, LocalesPaths.NOTIFICATION_9S);
		save = check(save, localeUtil, toText("&eЗапуск обновления шахты " + ReplaceKeys.NAME + "&e через &b8 &eсекунд."), null, LocalesPaths.NOTIFICATION_8S);
		save = check(save, localeUtil, toText("&eЗапуск обновления шахты " + ReplaceKeys.NAME + "&e через &b7 &eсекунд."), null, LocalesPaths.NOTIFICATION_7S);
		save = check(save, localeUtil, toText("&eЗапуск обновления шахты " + ReplaceKeys.NAME + "&e через &b6 &eсекунд."), null, LocalesPaths.NOTIFICATION_6S);
		save = check(save, localeUtil, toText("&eЗапуск обновления шахты " + ReplaceKeys.NAME + "&e через &b5 &eсекунд."), null, LocalesPaths.NOTIFICATION_5S);
		save = check(save, localeUtil, toText("&eЗапуск обновления шахты " + ReplaceKeys.NAME + "&e через &b4 &eсекунды."), null, LocalesPaths.NOTIFICATION_4S);
		save = check(save, localeUtil, toText("&eЗапуск обновления шахты " + ReplaceKeys.NAME + "&e через &b3 &eсекунды."), null, LocalesPaths.NOTIFICATION_3S);
		save = check(save, localeUtil, toText("&eЗапуск обновления шахты " + ReplaceKeys.NAME + "&e через &b2 &eсекунды."), null, LocalesPaths.NOTIFICATION_2S);
		save = check(save, localeUtil, toText("&eЗапуск обновления шахты " + ReplaceKeys.NAME + "&e через &b1 &eсекунду."), null, LocalesPaths.NOTIFICATION_1S);

		save = check(save, localeUtil, toText("&3="), null, LocalesPaths.PADDING);
		save = check(save, localeUtil, toText("&7[&aВКЛЮЧЕНО&7]"), null, LocalesPaths.ENABLE);
		save = check(save, localeUtil, toText("&7[&cВЫКЛЮЧЕНО&7]"), null, LocalesPaths.DISABLE);
		save = check(save, localeUtil, toText("&7[&cУдалить&7]"), null, LocalesPaths.REMOVE);
		save = check(save, localeUtil, toText("&cВыберите шахту через /minerefill list"), null, LocalesPaths.NOT_SELECTED);

		save = check(save, localeUtil, toText("&3Список команд"), null, LocalesPaths.MAIN_TITLE);
		save = check(save, localeUtil, toText("&f- &aСоздать шахту."), null, LocalesPaths.MAIN_CREATE);
		save = check(save, localeUtil, toText("&f- &aУдалить выбранную шахту."), null, LocalesPaths.MAIN_DELETE);
		save = check(save, localeUtil, toText("&f- &aУстановить позицию 1 или 2."), null, LocalesPaths.MAIN_SETPOS);
		save = check(save, localeUtil, toText("&f- &aДобавить в шахту блок на который вы смотрите."), null, LocalesPaths.MAIN_ADD_BLOCK);
		save = check(save, localeUtil, toText("&f- &aДобавить в шахту резервный блок на который вы смотрите."), null, LocalesPaths.MAIN_ADD_RESERVE_BLOCK);
		save = check(save, localeUtil, toText("&f- &aВремя в секундах между обновлениями шахты."), null, LocalesPaths.MAIN_INTERVAL);
		save = check(save, localeUtil, toText("&f- &aВключение/отключение автоматического заполнения шахты."), null, LocalesPaths.MAIN_SCHEDULE);
		save = check(save, localeUtil, toText("&f- &aУстановка имени шахты."), null, LocalesPaths.MAIN_SET_NAME);
		save = check(save, localeUtil, toText("&f- &aИнформация о шахте."), null, LocalesPaths.MAIN_INFO);
		save = check(save, localeUtil, toText("&f- &aСохранить выбранную шахту."), null, LocalesPaths.MAIN_SAVE);
		save = check(save, localeUtil, toText("&f- &aЗаполнить шахту."), null, LocalesPaths.MAIN_FILL);
		save = check(save, localeUtil, toText("&f- &aСписок шахт."), null, LocalesPaths.MAIN_LIST);
		save = check(save, localeUtil, toText("&f- &aВыбор шахты по ее UUID."), null, LocalesPaths.MAIN_SELECT);
		save = check(save, localeUtil, toText("&f- &aПерезагрузка плагина."), null, LocalesPaths.MAIN_RELOAD);

		save = check(save, localeUtil, toText("&eВы уже редактируете другую шахту. Кликните на это сообщение для подтверждения создания новой шахты."), null, LocalesPaths.CREATE_EDIT_OTHER);
		save = check(save, localeUtil, toText("&aШахта создана. Задайте координаты и прочие настройки, после чего ее можно будет сохранить."), null, LocalesPaths.CREATE_SUCCESS);

		save = check(save, localeUtil, toText("&cРедактируемая шахта не была ранее сохранена."), null, LocalesPaths.DELETE_UNSAVED);
		save = check(save, localeUtil, toText("&aВы удалили шахту."), null, LocalesPaths.DELETE_SUCCESS);

		save = check(save, localeUtil, toText("&cВыберите какую позицию вы хотите установить."), null, LocalesPaths.SETPOS_UNSELECTED);
		save = check(save, localeUtil, toText("&aВы установили позицию &b№" + ReplaceKeys.POSITION + "&a."), null, LocalesPaths.SETPOS_SUCCESS);

		save = check(save, localeUtil, toText("&cУкажите шанс появления блока."), null, LocalesPaths.ADD_BLOCK_CHANCE_NOT_PRESENT);
		save = check(save, localeUtil, toText("&cВы должны смотреть на блок. Максимальная дистанция поиска блока = 5."), null, LocalesPaths.ADD_BLOCK_BLOCK_NOT_PRESENT);
		save = check(save, localeUtil, toText("&cШахта уже содержит такой блок с таким же шансом."), null, LocalesPaths.ADD_BLOCK_ALREADY_EXIST);
		save = check(save, localeUtil, toText("&aВы добавили блок &e\"&b" + ReplaceKeys.BLOCK + "&e\"&a с шансом выбора = &b" + ReplaceKeys.CHANCE + "%&a."), null, LocalesPaths.ADD_BLOCK_SUCCESS);

		save = check(save, localeUtil, toText("&cВы должны смотреть на блок. Максимальная дистанция поиска блока = 5."), null, LocalesPaths.ADD_RESERVE_BLOCK_BLOCK_NOT_PRESENT);
		save = check(save, localeUtil, toText("&cДанный блок уже является резервным в выбранной шахте."), null, LocalesPaths.ADD_RESERVE_BLOCK_ALREADY_EXIST);
		save = check(save, localeUtil, toText("&aВы добавили в шахту резервный блок &e\"&b" + ReplaceKeys.BLOCK + "&e\"&a."), null, LocalesPaths.ADD_RESERVE_BLOCK_SUCCESS);

		save = check(save, localeUtil, toText("&cУкажите время между обновлениями в секундах."), null, LocalesPaths.INTERVAL_NOT_PRESENT);
		save = check(save, localeUtil, toText("&aНовый интервал между обновлениями шахты = &b" + ReplaceKeys.TIME + "&a."), null, LocalesPaths.INTERVAL_SUCCESS);

		save = check(save, localeUtil, toText("&cВы не указали должна ли шахта автоматически обновляться."), null, LocalesPaths.SCHEDULE_NOT_PRESENT);
		save = check(save, localeUtil, toText("&aАвтообновление шахты: " + ReplaceKeys.VALUE + "&a."), null, LocalesPaths.SCHEDULE_SUCCESS);

		save = check(save, localeUtil, toText("&cУкажите локализацию для установки имени."), null, LocalesPaths.SET_NAME_LOCALE_NOT_PRESENT);
		save = check(save, localeUtil, toText("&cУкажите имя шахты."), null, LocalesPaths.SET_NAME_NAME_NOT_PRESENT);
		save = check(save, localeUtil, toText("&aВы установили имя шахты."), null, LocalesPaths.SET_NAME_SUCCESS);

		save = check(save, localeUtil, toText("&bИнформация о шахте"), null, LocalesPaths.INFO_MAIN_TITLE);
		save = check(save, localeUtil, toText("&bСписок блоков"), null, LocalesPaths.INFO_BLOCKS_LIST_TITLE);
		save = check(save, localeUtil, toText("&bСписок резервных блоков"), null, LocalesPaths.INFO_RESERVE_BLOCKS_LIST_TITLE);
		save = check(save, localeUtil, toText("&bСписок имен"), null, LocalesPaths.INFO_NAMES_LIST_TITLE);
		save = check(save, localeUtil, toText("&eUUID&f: &b" + ReplaceKeys.VALUE), null, LocalesPaths.INFO_UUID);
		save = check(save, localeUtil, toText("&eКоординаты&f: &b" + ReplaceKeys.MIN + " &3➣ &b" + ReplaceKeys.MAX), null, LocalesPaths.INFO_COORDS);
		save = check(save, localeUtil, toText("&eАвтообновление&f: &b" + ReplaceKeys.VALUE), null, LocalesPaths.INFO_AUTO_UPDATE);
		save = check(save, localeUtil, toText("&eИнтервал автообновления&f: &b" + ReplaceKeys.VALUE), null, LocalesPaths.INFO_UPDATE_INTERVAL);
		save = check(save, localeUtil, toText("&eБлоков&f: &b" + ReplaceKeys.VALUE), null, LocalesPaths.INFO_BLOCKS_VARIANTS);
		save = check(save, localeUtil, toText("&eРезервных блоков&f: &b" + ReplaceKeys.VALUE), null, LocalesPaths.INFO_RESERVE_BLOCKS_VARIANTS);
		save = check(save, localeUtil, toText("&eЛокализованных имен&f: &b" + ReplaceKeys.VALUE), null, LocalesPaths.INFO_NAMES_VARIANTS);
		save = check(save, localeUtil, toText("&5Клик для удаления"), null, LocalesPaths.INFO_CLICK_TO_REMOVE);
		save = check(save, localeUtil, toText("&5Клик для отображения списка"), null, LocalesPaths.INFO_HOVER);

		save = check(save, localeUtil, toText("&cУ шахты не установлена одна или обе позиции."), null, LocalesPaths.SAVE_POSITIONS_NOT_PRESENT);
		save = check(save, localeUtil, toText("&cВ шахте нет никаких блоков."), null, LocalesPaths.SAVE_BLOCKS_NOT_PRESENT);
		save = check(save, localeUtil, toText("&aШахта сохранена."), null, LocalesPaths.SAVE_SUCCESS);

		save = check(save, localeUtil, toText("&aЗапущенно обновление шахты " + ReplaceKeys.NAME + "&a."), null, LocalesPaths.FILL_SUCCESS);

		save = check(save, localeUtil, toText("&bСписок шахт"), null, LocalesPaths.LIST_TITLE);
		save = check(save, localeUtil, toText("&cМир &e\"&b" + ReplaceKeys.NAME + "&e\"&c не загружен."), null, LocalesPaths.LIST_WORLD_NOT_LOADED);
		save = check(save, localeUtil, toText("&7[&aTP&7] "), null, LocalesPaths.LIST_MINE_TELEPORT_ALLOWED);
		save = check(save, localeUtil, toText("&7[&cTP&6] "), null, LocalesPaths.LIST_MINE_TELEPORT_DISALLOWED);
		save = check(save, localeUtil, toText("&aВы выбрали шахту " + ReplaceKeys.NAME + "&a."), null, LocalesPaths.LIST_MINE_SELECTED);
		save = check(save, localeUtil, toText("&bКлик по шахте выберет ее для получения информации, редактирования или ручного заполнения. Доступные действия зависят от имеющихся у вас разрешений."), null, LocalesPaths.LIST_INFO);

		save = check(save, localeUtil, toText("&cШахта с таким UUID не найдена."), null, LocalesPaths.SELECT_EXCEPTION);

		save = check(save, localeUtil, toText("&aПлагин перезагружен."), null, LocalesPaths.RELOAD_SUCCESS);

		if(save) save(localeUtil);
	}

}
