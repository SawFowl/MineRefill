package sawfowl.minerefill.commands;

import java.util.Arrays;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.locale.LocaleSource;
import org.spongepowered.api.util.locale.Locales;

import net.kyori.adventure.audience.Audience;
import sawfowl.localeapi.utils.AbstractLocaleUtil;
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;
import sawfowl.minerefill.data.Mine;

public class FillCommand extends AbstractCommand {

	public FillCommand(MineRefill plugin) {
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
		Sponge.asyncScheduler().executor(plugin.getPluginContainer()).execute(() -> {
			mine.fill(plugin.getPluginContainer(), plugin.getLocales(), plugin.getLogger(), plugin.getConfig().isActionBarMessages(), plugin.getConfig().isDebug());
			player.sendMessage(plugin.getLocales().getTextReplaced2(locale, AbstractLocaleUtil.replaceMapComponents(Arrays.asList(ReplaceKeys.NAME), Arrays.asList(mine.getDisplayName(locale))), LocalesPaths.FILL_SUCCESS));
		});
		return success();
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.FILL)
				.executor(this)
				.build();
	}

}
