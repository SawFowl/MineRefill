package sawfowl.minerefill.commands;

import java.util.Arrays;
import java.util.Locale;

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

public class SetScheduleCommand extends AbstractCommand {

	public SetScheduleCommand(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		Audience audience = context.cause().audience();
		Locale locale = audience instanceof LocaleSource ? ((LocaleSource) audience).locale() : Locales.DEFAULT;
		if(!(audience instanceof ServerPlayer)) exception(plugin.getLocales().getText(locale, LocalesPaths.ONLY_PLAYER));
		ServerPlayer player = (ServerPlayer) audience;
		if(!plugin.getEditableMine(player.uniqueId()).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.NOT_SELECTED));
		if(!context.one(CommandParameters.SCHEDULE).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.SCHEDULE_NOT_PRESENT));
		Mine mine = plugin.getEditableMine(player.uniqueId()).get();
		boolean value = context.one(CommandParameters.SCHEDULE).get();
		mine.setSchedule(value);
		player.sendMessage(plugin.getLocales().getTextReplaced2(locale, AbstractLocaleUtil.replaceMapComponents(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(mine.isSchedule() ? plugin.getLocales().getText(locale, LocalesPaths.ENABLE) : plugin.getLocales().getText(locale, LocalesPaths.DISABLE))), LocalesPaths.SCHEDULE_SUCCESS));
		return success();
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.EDIT)
				.addParameter(CommandParameters.SCHEDULE)
				.executor(this)
				.build();
	}

}
