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

public class SetIntervalCommand extends AbstractCommand {

	public SetIntervalCommand(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		SourceData sourceData = createSourceData(context.cause());
		Locale locale = sourceData.getLocaleSource().locale();
		if(!plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.NOT_SELECTED));
		if(!context.one(CommandParameters.TIME).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.INTERVAL_NOT_PRESENT));
		Mine mine = plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).get();
		int time = context.one(CommandParameters.TIME).get();
		mine.setScheduleTime(time);
		sourceData.sendMessage(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.TIME), Arrays.asList(time)), LocalesPaths.INTERVAL_SUCCESS));
		return success();
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.EDIT)
				.addParameter(CommandParameters.TIME)
				.executor(this)
				.build();
	}

}
