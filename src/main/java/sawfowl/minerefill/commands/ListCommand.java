package sawfowl.minerefill.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.spongepowered.api.adventure.SpongeComponents;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.locale.LocaleSource;
import org.spongepowered.api.util.locale.Locales;
import org.spongepowered.api.world.server.ServerLocation;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.localeapi.utils.AbstractLocaleUtil;
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;

public class ListCommand extends AbstractCommand {

	public ListCommand(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		Audience audience = context.cause().audience();
		Locale locale = audience instanceof LocaleSource ? ((LocaleSource) audience).locale() : Locales.DEFAULT;
		List<Component> list = audience instanceof ServerPlayer ? plugin.getMines().stream().map(mine -> (!((ServerPlayer) audience).hasPermission(Permissions.teleport(mine.getUniqueid())) ? plugin.getLocales().getText(locale, LocalesPaths.LIST_MINE_TELEPORT_DISALLOWED) : plugin.getLocales().getText(locale, LocalesPaths.LIST_MINE_TELEPORT_ALLOWED).clickEvent(SpongeComponents.executeCallback(cause -> {
			if(mine.getWorld().isPresent()) {
				((ServerPlayer) audience).setLocation(ServerLocation.of(mine.getWorld().get(), mine.getPositions().getMax()));
			} else audience.sendMessage(plugin.getLocales().getText(locale, LocalesPaths.LIST_WORLD_NOT_LOADED));
		})).append(mine.getDisplayName(locale).clickEvent(SpongeComponents.executeCallback(cause -> {
				plugin.addEditableMine(((ServerPlayer) audience).uniqueId(), mine);
				audience.sendMessage(plugin.getLocales().getTextReplaced2(locale, AbstractLocaleUtil.replaceMapComponents(Arrays.asList(ReplaceKeys.NAME), Arrays.asList(mine.getDisplayName(locale))), LocalesPaths.LIST_MINE_SELECTED));
			})).append(toText(" &a" + mine.getWorldId() + " &3" + mine.getPositions().getMin() + " &f-> &b" + mine.getPositions().getMax()))))).collect(Collectors.toList()) :
				plugin.getMines().stream().map(mine -> (mine.getDisplayName(locale).append(toText(" &a" + mine.getWorldId() + " &3" + mine.getPositions().getMin() + " &f-> &b" + mine.getPositions().getMax())))).collect(Collectors.toList());
		sendPagination(audience, locale, list, plugin.getLocales().getText(locale, LocalesPaths.LIST_TITLE), plugin.getLocales().getText(locale, LocalesPaths.PADDING));
		if(!list.isEmpty() && audience instanceof ServerPlayer && (context.cause().hasPermission(Permissions.EDIT) || context.cause().hasPermission(Permissions.FILL) || context.cause().hasPermission(Permissions.INFO))) audience.sendMessage(plugin.getLocales().getText(locale, LocalesPaths.LIST_INFO));;
		return success();
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.LIST)
				.executor(this)
				.build();
	}

}
