package sawfowl.minerefill.commands;

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
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.api.Mine;
import sawfowl.minerefill.configure.LocalesPaths;

public class CreateCommand extends AbstractCommand {

	public CreateCommand(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		Audience audience = context.cause().audience();
		Locale locale = audience instanceof LocaleSource ? ((LocaleSource) audience).locale() : Locales.DEFAULT;
		if(!(audience instanceof ServerPlayer)) exception(plugin.getLocales().getText(locale, LocalesPaths.ONLY_PLAYER));
		ServerPlayer player = (ServerPlayer) audience;
		if(plugin.getMineAPI().getEditableMines().containsKey(player.uniqueId().toString())) {
			player.sendMessage(plugin.getLocales().getText(locale, LocalesPaths.CREATE_EDIT_OTHER).clickEvent(SpongeComponents.executeCallback(cause -> {
				create(player);
			})));
		} else create(player);
		return success();
	}

	private void create(ServerPlayer player) {
		Mine mine = Mine.create(player.world());
		plugin.getMineAPI().getEditableMines().put(player.uniqueId().toString(), mine);
		player.sendMessage(plugin.getLocales().getText(player.locale(), LocalesPaths.CREATE_SUCCESS));
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.EDIT)
				.executor(this)
				.build();
	}

}
