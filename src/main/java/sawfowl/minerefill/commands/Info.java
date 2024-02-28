package sawfowl.minerefill.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.adventure.SpongeComponents;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;

import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.api.Mine;
import sawfowl.minerefill.api.SourceData;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;

public class Info extends AbstractCommand {

	public Info(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		SourceData sourceData = createSourceData(context.cause());
		Locale locale = sourceData.getLocaleSource().locale();
		if(!plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).isPresent()) exception(plugin.getLocales().getComponent(locale, LocalesPaths.NOT_SELECTED));
		Mine mine = plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).get();
		List<Component> text = new ArrayList<>();
		text.add(getText(locale, LocalesPaths.INFO_UUID).replace(ReplaceKeys.VALUE, mine.getUniqueid()).get());
		text.add(getText(locale, LocalesPaths.INFO_COORDS).replace(new String[]{ReplaceKeys.MIN, ReplaceKeys.MAX}, mine.getPositions().getMin(), mine.getPositions().getMax()).get());
		text.add(getText(locale, LocalesPaths.INFO_AUTO_UPDATE).replace(ReplaceKeys.VALUE, mine.isSchedule() ? plugin.getLocales().getComponent(locale, LocalesPaths.ENABLE) : getComponent(locale, LocalesPaths.DISABLE)).get());
		text.add(getText(locale, LocalesPaths.INFO_UPDATE_INTERVAL).replace(ReplaceKeys.VALUE, mine.getScheduleInterval()).get());
		if(mine.getBlocks().isEmpty()) {
			text.add(getText(locale, LocalesPaths.INFO_BLOCKS_VARIANTS).replace(ReplaceKeys.VALUE, 0).get());
		} else text.add(getText(locale, LocalesPaths.INFO_BLOCKS_VARIANTS).replace(ReplaceKeys.VALUE, mine.getBlocks().size()).createCallBack(cause -> sendBlocksList(sourceData, locale, mine)).get().hoverEvent(HoverEvent.showText(plugin.getLocales().getComponent(locale, LocalesPaths.INFO_HOVER))));
		if(mine.getReserveBlocks().isEmpty()) {
			text.add(getText(locale, LocalesPaths.INFO_RESERVE_BLOCKS_VARIANTS).replace(ReplaceKeys.VALUE, 0).get());
		} else text.add(getText(locale, LocalesPaths.INFO_RESERVE_BLOCKS_VARIANTS).replace(ReplaceKeys.VALUE, mine.getReserveBlocks().size()).createCallBack(cause -> sendReserveBlocksList(sourceData, locale, mine)).get().hoverEvent(HoverEvent.showText(plugin.getLocales().getComponent(locale, LocalesPaths.INFO_HOVER))));
		if(mine.getNames().isEmpty()) {
			text.add(getText(locale, LocalesPaths.INFO_NAMES_VARIANTS).replace(ReplaceKeys.VALUE, 0).get());
		} else text.add(getText(locale, LocalesPaths.INFO_NAMES_VARIANTS).replace(ReplaceKeys.VALUE, mine.getNames().size()).createCallBack(cause -> sendNames(sourceData, locale, mine)).get().hoverEvent(HoverEvent.showText(plugin.getLocales().getComponent(locale, LocalesPaths.INFO_HOVER))));
		sendPagination(sourceData.getAudience(), locale, text, plugin.getLocales().getComponent(locale, LocalesPaths.INFO_MAIN_TITLE), plugin.getLocales().getComponent(locale, LocalesPaths.PADDING));
		return success();
	}

	private void sendBlocksList(SourceData sourceData, Locale locale, Mine mine) {
		List<Component> text = new ArrayList<>();
		mine.getBlocks().forEach(block -> {
			Component first = toText(" &a" + block.getBlockId() + " &f- &b" + block.getChance() + "% ");
			Component second = (!sourceData.getSubject().hasPermission(Permissions.EDIT) ? toText("") : plugin.getLocales().getComponent(locale, LocalesPaths.REMOVE).hoverEvent(HoverEvent.showText(plugin.getLocales().getComponent(locale, LocalesPaths.INFO_CLICK_TO_REMOVE))).clickEvent(SpongeComponents.executeCallback(cause -> {
				mine.getBlocks().remove(block);
				sendBlocksList(sourceData, locale, mine);
			})));
			text.add(first.append(second));
		});
		sendPagination(sourceData.getAudience(), locale, text, plugin.getLocales().getComponent(locale, LocalesPaths.INFO_BLOCKS_LIST_TITLE), plugin.getLocales().getComponent(locale, LocalesPaths.PADDING));
	}

	private void sendReserveBlocksList(SourceData sourceData, Locale locale, Mine mine) {
		List<Component> text = new ArrayList<>();
		mine.getSerializedReserveBlocks().forEach(block -> {
			Component first = toText(" &a" + block.getType()+ " ");
			Component second = (!sourceData.getSubject().hasPermission(Permissions.EDIT) ? toText("") : plugin.getLocales().getComponent(locale, LocalesPaths.REMOVE).hoverEvent(HoverEvent.showText(plugin.getLocales().getComponent(locale, LocalesPaths.INFO_CLICK_TO_REMOVE))).clickEvent(SpongeComponents.executeCallback(cause -> {
				mine.getSerializedReserveBlocks().remove(block);
				mine.getReserveBlocks().clear();
				mine.getReserveBlocks();
				sendReserveBlocksList(sourceData, locale, mine);
			})));
			text.add(first.append(second));
		});
		sendPagination(sourceData.getAudience(), locale, text, plugin.getLocales().getComponent(locale, LocalesPaths.INFO_RESERVE_BLOCKS_LIST_TITLE), plugin.getLocales().getComponent(locale, LocalesPaths.PADDING));
	}

	private void sendNames(SourceData sourceData, Locale locale, Mine mine) {
		List<Component> text = new ArrayList<>();
		mine.getNames().keySet().forEach(name -> {
			Component first = toText(" &a" + locale + " &f- ").append(mine.getDisplayName(locale)).append(toText(" "));
			Component second = (!sourceData.getSubject().hasPermission(Permissions.EDIT) ? toText("") : plugin.getLocales().getComponent(locale, LocalesPaths.REMOVE).hoverEvent(HoverEvent.showText(plugin.getLocales().getComponent(locale, LocalesPaths.INFO_CLICK_TO_REMOVE))).clickEvent(SpongeComponents.executeCallback(cause -> {
				mine.getNames().remove(name);
				sendNames(sourceData, locale, mine);
			})));
			text.add(first.append(second));
		});
		sendPagination(sourceData.getAudience(), locale, text, plugin.getLocales().getComponent(locale, LocalesPaths.INFO_NAMES_LIST_TITLE), plugin.getLocales().getComponent(locale, LocalesPaths.PADDING));
	
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.INFO)
				.executor(this)
				.build();
	}

}
