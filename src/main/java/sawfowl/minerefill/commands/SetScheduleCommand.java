package sawfowl.minerefill.commands;

import java.util.Arrays;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import sawfowl.localeapi.utils.AbstractLocaleUtil;
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.api.Mine;
import sawfowl.minerefill.api.SourceData;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;

public class SetScheduleCommand extends AbstractCommand {

	public SetScheduleCommand(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		SourceData sourceData = createSourceData(context.cause());
		Locale locale = sourceData.getLocaleSource().locale();
		if(!plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.NOT_SELECTED));
		if(!context.one(CommandParameters.SCHEDULE).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.SCHEDULE_NOT_PRESENT));
		Mine mine = plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).get();
		boolean value = context.one(CommandParameters.SCHEDULE).get();
		mine.setSchedule(value);
		sourceData.sendMessage(plugin.getLocales().getTextReplaced2(locale, AbstractLocaleUtil.replaceMapComponents(Arrays.asList(ReplaceKeys.VALUE), Arrays.asList(mine.isSchedule() ? plugin.getLocales().getText(locale, LocalesPaths.ENABLE) : plugin.getLocales().getText(locale, LocalesPaths.DISABLE))), LocalesPaths.SCHEDULE_SUCCESS));
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
