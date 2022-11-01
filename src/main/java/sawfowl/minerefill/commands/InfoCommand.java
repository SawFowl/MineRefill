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
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.locale.LocaleSource;
import org.spongepowered.api.util.locale.Locales;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import sawfowl.localeapi.utils.AbstractLocaleUtil;
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;
import sawfowl.minerefill.data.Mine;

public class InfoCommand extends AbstractCommand {

	public InfoCommand(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		Audience audience = context.cause().audience();
		Locale locale = audience instanceof LocaleSource ? ((LocaleSource) audience).locale() : Locales.DEFAULT;
		if(!(audience instanceof ServerPlayer)) exception(plugin.getLocales().getText(locale, LocalesPaths.ONLY_PLAYER));
		ServerPlayer player = (ServerPlayer) audience;
		if(!plugin.getEditableMine(player.uniqueId()).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.NOT_SELECTED));
		Mine mine = plugin.getEditableMine(player.uniqueId()).get();
		List<Component> text = new ArrayList<>();
		text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(mine.getUniqueid())), LocalesPaths.INFO_UUID));
		text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.MIN, ReplaceKeys.MAX), Arrays.asList(mine.getPositions().getMin(), mine.getPositions().getMax())), LocalesPaths.INFO_COORDS));
		text.add(plugin.getLocales().getTextReplaced2(locale, AbstractLocaleUtil.replaceMapComponents(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(mine.isSchedule() ? plugin.getLocales().getText(locale, LocalesPaths.ENABLE) : plugin.getLocales().getText(locale, LocalesPaths.DISABLE))), LocalesPaths.INFO_AUTO_UPDATE));
		text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(mine.getScheduleInterval())), LocalesPaths.INFO_UPDATE_INTERVAL));
		if(mine.getBlocks().isEmpty()) {
			text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(0)), LocalesPaths.INFO_BLOCKS_VARIANTS));
		} else text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(mine.getBlocks().size())), LocalesPaths.INFO_BLOCKS_VARIANTS).hoverEvent(HoverEvent.showText(plugin.getLocales().getText(locale, LocalesPaths.INFO_HOVER))).clickEvent(SpongeComponents.executeCallback(cause -> {sendBlocksList(player, mine);})));
		if(mine.getReserveBlocks().isEmpty()) {
			text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(0)), LocalesPaths.INFO_RESERVE_BLOCKS_VARIANTS));
		} else text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(mine.getReserveBlocks().size())), LocalesPaths.INFO_RESERVE_BLOCKS_VARIANTS).hoverEvent(HoverEvent.showText(plugin.getLocales().getText(locale, LocalesPaths.INFO_HOVER))).clickEvent(SpongeComponents.executeCallback(cause -> {sendReserveBlocksList(player, mine);})));
		if(mine.getNames().isEmpty()) {
			text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(0)), LocalesPaths.INFO_NAMES_VARIANTS));
		} else text.add(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(mine.getNames().size())), LocalesPaths.INFO_NAMES_VARIANTS).hoverEvent(HoverEvent.showText(plugin.getLocales().getText(locale, LocalesPaths.INFO_HOVER))).clickEvent(SpongeComponents.executeCallback(cause -> {sendNames(player, mine);})));
		sendPagination(audience, locale, text, plugin.getLocales().getText(locale, LocalesPaths.INFO_MAIN_TITLE), plugin.getLocales().getText(locale, LocalesPaths.PADDING));
		return success();
	}

	private void sendBlocksList(ServerPlayer player, Mine mine) {
		List<Component> text = new ArrayList<>();
		mine.getBlocks().forEach(block -> {
			Component first = toText(" &a" + block.getSerializedBlock().getType() + " &f- &b" + block.getChance() + "% ");
			Component second = (!player.hasPermission(Permissions.EDIT) ? toText("") : plugin.getLocales().getText(player.locale(), LocalesPaths.REMOVE).hoverEvent(HoverEvent.showText(plugin.getLocales().getText(player.locale(), LocalesPaths.INFO_CLICK_TO_REMOVE))).clickEvent(SpongeComponents.executeCallback(cause -> {
				mine.getBlocks().remove(block);
				sendBlocksList(player, mine);
			})));
			text.add(first.append(second));
		});
		sendPagination(player, player.locale(), text, plugin.getLocales().getText(player.locale(), LocalesPaths.INFO_BLOCKS_LIST_TITLE), plugin.getLocales().getText(player.locale(), LocalesPaths.PADDING));
	}

	private void sendReserveBlocksList(ServerPlayer player, Mine mine) {
		List<Component> text = new ArrayList<>();
		mine.getSerializedReserveBlocks().forEach(block -> {
			Component first = toText(" &a" + block.getType()+ " ");
			Component second = (!player.hasPermission(Permissions.EDIT) ? toText("") : plugin.getLocales().getText(player.locale(), LocalesPaths.REMOVE).hoverEvent(HoverEvent.showText(plugin.getLocales().getText(player.locale(), LocalesPaths.INFO_CLICK_TO_REMOVE))).clickEvent(SpongeComponents.executeCallback(cause -> {
				mine.getSerializedReserveBlocks().remove(block);
				mine.getReserveBlocks().clear();
				mine.getReserveBlocks();
				sendReserveBlocksList(player, mine);
			})));
			text.add(first.append(second));
		});
		sendPagination(player, player.locale(), text, plugin.getLocales().getText(player.locale(), LocalesPaths.INFO_RESERVE_BLOCKS_LIST_TITLE), plugin.getLocales().getText(player.locale(), LocalesPaths.PADDING));
	}

	private void sendNames(ServerPlayer player, Mine mine) {
		List<Component> text = new ArrayList<>();
		mine.getNames().keySet().forEach(locale -> {
			Component first = toText(" &a" + locale + " &f- ").append(mine.getDisplayName(locale)).append(toText(" "));
			Component second = (!player.hasPermission(Permissions.EDIT) ? toText("") : plugin.getLocales().getText(player.locale(), LocalesPaths.REMOVE).hoverEvent(HoverEvent.showText(plugin.getLocales().getText(player.locale(), LocalesPaths.INFO_CLICK_TO_REMOVE))).clickEvent(SpongeComponents.executeCallback(cause -> {
				mine.getNames().remove(locale);
				sendNames(player, mine);
			})));
			text.add(first.append(second));
		});
		sendPagination(player, player.locale(), text, plugin.getLocales().getText(player.locale(), LocalesPaths.INFO_NAMES_LIST_TITLE), plugin.getLocales().getText(player.locale(), LocalesPaths.PADDING));
	
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.INFO)
				.executor(this)
				.build();
	}

}
