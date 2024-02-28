package sawfowl.minerefill.commands;

import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.locale.LocaleSource;

import net.kyori.adventure.audience.Audience;

import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.configure.LocalesPaths;

public class Reload extends AbstractCommand {

	public Reload(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		Audience audience = context.cause().audience();
		Locale locale = audience instanceof LocaleSource ? ((LocaleSource) audience).locale() : plugin.getLocales().getLocaleService().getSystemOrDefaultLocale();
		plugin.reload();
		audience.sendMessage(plugin.getLocales().getComponent(locale, LocalesPaths.RELOAD_SUCCESS));
		return success();
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.RELOAD)
				.executor(this)
				.build();
	}

}
