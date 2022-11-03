package sawfowl.minerefill.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.adventure.SpongeComponents;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import sawfowl.localeapi.utils.AbstractLocaleUtil;
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.api.Mine;
import sawfowl.minerefill.api.SourceData;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;

public class InfoCommand extends AbstractCommand {

	public InfoCommand(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		SourceData sourceData = createSourceData(context.cause());
		Locale locale = sourceData.getLocaleSource().locale();
		if(!plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.NOT_SELECTED));
		Mine mine = plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).get();
		List<Component> text = new ArrayList<>();
		text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(mine.getUniqueid())), LocalesPaths.INFO_UUID));
		text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.MIN, ReplaceKeys.MAX), Arrays.asList(mine.getPositions().getMin(), mine.getPositions().getMax())), LocalesPaths.INFO_COORDS));
		text.add(plugin.getLocales().getTextReplaced2(locale, AbstractLocaleUtil.replaceMapComponents(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(mine.isSchedule() ? plugin.getLocales().getText(locale, LocalesPaths.ENABLE) : plugin.getLocales().getText(locale, LocalesPaths.DISABLE))), LocalesPaths.INFO_AUTO_UPDATE));
		text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(mine.getScheduleInterval())), LocalesPaths.INFO_UPDATE_INTERVAL));
		if(mine.getBlocks().isEmpty()) {
			text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(0)), LocalesPaths.INFO_BLOCKS_VARIANTS));
		} else text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(mine.getBlocks().size())), LocalesPaths.INFO_BLOCKS_VARIANTS).hoverEvent(HoverEvent.showText(plugin.getLocales().getText(locale, LocalesPaths.INFO_HOVER))).clickEvent(SpongeComponents.executeCallback(cause -> {sendBlocksList(sourceData, locale, mine);})));
		if(mine.getReserveBlocks().isEmpty()) {
			text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(0)), LocalesPaths.INFO_RESERVE_BLOCKS_VARIANTS));
		} else text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(mine.getReserveBlocks().size())), LocalesPaths.INFO_RESERVE_BLOCKS_VARIANTS).hoverEvent(HoverEvent.showText(plugin.getLocales().getText(locale, LocalesPaths.INFO_HOVER))).clickEvent(SpongeComponents.executeCallback(cause -> {sendReserveBlocksList(sourceData, locale, mine);})));
		if(mine.getNames().isEmpty()) {
			text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(0)), LocalesPaths.INFO_NAMES_VARIANTS));
		} else text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(mine.getNames().size())), LocalesPaths.INFO_NAMES_VARIANTS).hoverEvent(HoverEvent.showText(plugin.getLocales().getText(locale, LocalesPaths.INFO_HOVER))).clickEvent(SpongeComponents.executeCallback(cause -> {sendNames(sourceData, locale, mine);})));
		sendPagination(sourceData.getAudience(), locale, text, plugin.getLocales().getText(locale, LocalesPaths.INFO_MAIN_TITLE), plugin.getLocales().getText(locale, LocalesPaths.PADDING));
		return success();
	}

	private void sendBlocksList(SourceData sourceData, Locale locale, Mine mine) {
		List<Component> text = new ArrayList<>();
		mine.getBlocks().forEach(block -> {
			Component first = toText(" &a" + block.getSerializedBlock().getType() + " &f- &b" + block.getChance() + "% ");
			Component second = (!sourceData.getSubject().hasPermission(Permissions.EDIT) ? toText("") : plugin.getLocales().getText(locale, LocalesPaths.REMOVE).hoverEvent(HoverEvent.showText(plugin.getLocales().getText(locale, LocalesPaths.INFO_CLICK_TO_REMOVE))).clickEvent(SpongeComponents.executeCallback(cause -> {
				mine.getBlocks().remove(block);
				sendBlocksList(sourceData, locale, mine);
			})));
			text.add(first.append(second));
		});
		sendPagination(sourceData.getAudience(), locale, text, plugin.getLocales().getText(locale, LocalesPaths.INFO_BLOCKS_LIST_TITLE), plugin.getLocales().getText(locale, LocalesPaths.PADDING));
	}

	private void sendReserveBlocksList(SourceData sourceData, Locale locale, Mine mine) {
		List<Component> text = new ArrayList<>();
		mine.getSerializedReserveBlocks().forEach(block -> {
			Component first = toText(" &a" + block.getType()+ " ");
			Component second = (!sourceData.getSubject().hasPermission(Permissions.EDIT) ? toText("") : plugin.getLocales().getText(locale, LocalesPaths.REMOVE).hoverEvent(HoverEvent.showText(plugin.getLocales().getText(locale, LocalesPaths.INFO_CLICK_TO_REMOVE))).clickEvent(SpongeComponents.executeCallback(cause -> {
				mine.getSerializedReserveBlocks().remove(block);
				mine.getReserveBlocks().clear();
				mine.getReserveBlocks();
				sendReserveBlocksList(sourceData, locale, mine);
			})));
			text.add(first.append(second));
		});
		sendPagination(sourceData.getAudience(), locale, text, plugin.getLocales().getText(locale, LocalesPaths.INFO_RESERVE_BLOCKS_LIST_TITLE), plugin.getLocales().getText(locale, LocalesPaths.PADDING));
	}

	private void sendNames(SourceData sourceData, Locale locale, Mine mine) {
		List<Component> text = new ArrayList<>();
		mine.getNames().keySet().forEach(name -> {
			Component first = toText(" &a" + locale + " &f- ").append(mine.getDisplayName(locale)).append(toText(" "));
			Component second = (!sourceData.getSubject().hasPermission(Permissions.EDIT) ? toText("") : plugin.getLocales().getText(locale, LocalesPaths.REMOVE).hoverEvent(HoverEvent.showText(plugin.getLocales().getText(locale, LocalesPaths.INFO_CLICK_TO_REMOVE))).clickEvent(SpongeComponents.executeCallback(cause -> {
				mine.getNames().remove(name);
				sendNames(sourceData, locale, mine);
			})));
			text.add(first.append(second));
		});
		sendPagination(sourceData.getAudience(), locale, text, plugin.getLocales().getText(locale, LocalesPaths.INFO_NAMES_LIST_TITLE), plugin.getLocales().getText(locale, LocalesPaths.PADDING));
	
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.INFO)
				.executor(this)
				.build();
	}

}
