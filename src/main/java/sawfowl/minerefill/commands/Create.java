package sawfowl.minerefill.commands;

import java.util.Locale;

import org.spongepowered.api.adventure.SpongeComponents;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.locale.LocaleSource;

import net.kyori.adventure.audience.Audience;

import sawfowl.localeapi.api.services.LocaleService;
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.api.Mine;
import sawfowl.minerefill.configure.LocalesPaths;

public class Create extends AbstractCommand {

	public Create(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		Audience audience = context.cause().audience();
		Locale locale = audience instanceof LocaleSource ? ((LocaleSource) audience).locale() : LocaleService.getInstance().getSystemOrDefaultLocale();
		if(!(audience instanceof ServerPlayer)) exception(plugin.getLocales().getComponent(locale, LocalesPaths.ONLY_PLAYER));
		ServerPlayer player = (ServerPlayer) audience;
		if(plugin.getMineAPI().getEditableMines().containsKey(player.uniqueId().toString())) {
			player.sendMessage(plugin.getLocales().getComponent(locale, LocalesPaths.CREATE_EDIT_OTHER).clickEvent(SpongeComponents.executeCallback(cause -> {
				create(player);
			})));
		} else create(player);
		return success();
	}

	private void create(ServerPlayer player) {
		Mine mine = Mine.create(player.world());
		plugin.getMineAPI().getEditableMines().put(player.uniqueId().toString(), mine);
		player.sendMessage(plugin.getLocales().getComponent(player.locale(), LocalesPaths.CREATE_SUCCESS));
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.EDIT)
				.executor(this)
				.build();
	}

}
