package sawfowl.minerefill.commands;

import java.util.Collection;
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

import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;

public class List extends AbstractCommand {

	public List(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		Audience audience = context.cause().audience();
		Locale locale = audience instanceof LocaleSource ? ((LocaleSource) audience).locale() : Locales.DEFAULT;
		boolean isPlayer = audience instanceof ServerPlayer;
		Collection<Component> list = isPlayer ? plugin.getMineAPI().getMines().stream().map(mine -> (!((ServerPlayer) audience).hasPermission(Permissions.teleport(mine.getUniqueid())) ? plugin.getLocales().getComponent(locale, LocalesPaths.LIST_MINE_TELEPORT_DISALLOWED) : plugin.getLocales().getComponent(locale, LocalesPaths.LIST_MINE_TELEPORT_ALLOWED).clickEvent(SpongeComponents.executeCallback(cause -> {
			if(mine.getWorld().isPresent()) {
				((ServerPlayer) audience).setLocation(ServerLocation.of(mine.getWorld().get(), mine.getPositions().getMax()));
			} else audience.sendMessage(plugin.getLocales().getComponent(locale, LocalesPaths.LIST_WORLD_NOT_LOADED));
		})).append(mine.getDisplayName(locale).clickEvent(SpongeComponents.executeCallback(cause -> {
				plugin.getMineAPI().addEditableMine(((ServerPlayer) audience).uniqueId().toString(), mine);
				audience.sendMessage(getText(locale, LocalesPaths.LIST_MINE_SELECTED).replace(ReplaceKeys.NAME, mine.getDisplayName(locale)).get());
			})).append(toText(" &a" + mine.getWorldId() + " &3" + mine.getPositions().getMin() + " &f➣ &b" + mine.getPositions().getMax()))))).collect(Collectors.toList()) :
				plugin.getMineAPI().getMines().stream().map(mine -> (mine.getDisplayName(locale).append(toText(" " + mine.getUniqueid() + " &a" + mine.getWorldId() + " &3" + mine.getPositions().getMin() + " &f➣ &b" + mine.getPositions().getMax())))).collect(Collectors.toList());
		sendPagination(audience, locale, list, plugin.getLocales().getComponent(locale, LocalesPaths.LIST_TITLE), plugin.getLocales().getComponent(locale, LocalesPaths.PADDING));
		if(!list.isEmpty() && isPlayer && (context.cause().hasPermission(Permissions.EDIT) || context.cause().hasPermission(Permissions.FILL) || context.cause().hasPermission(Permissions.INFO))) audience.sendMessage(plugin.getLocales().getComponent(locale, LocalesPaths.LIST_INFO));;
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
