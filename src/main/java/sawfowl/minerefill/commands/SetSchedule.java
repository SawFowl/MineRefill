package sawfowl.minerefill.commands;

import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.api.Mine;
import sawfowl.minerefill.api.SourceData;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;

public class SetSchedule extends AbstractCommand {

	public SetSchedule(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		SourceData sourceData = createSourceData(context.cause());
		Locale locale = sourceData.getLocale();
		if(!plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).isPresent()) exception(plugin.getLocales().getComponent(locale, LocalesPaths.NOT_SELECTED));
		if(!context.one(CommandParameters.SCHEDULE).isPresent()) exception(plugin.getLocales().getComponent(locale, LocalesPaths.SCHEDULE_NOT_PRESENT));
		Mine mine = plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).get();
		boolean value = context.one(CommandParameters.SCHEDULE).get();
		mine.setSchedule(value);
		sourceData.sendMessage(getText(locale, LocalesPaths.SCHEDULE_SUCCESS).replace(ReplaceKeys.VALUE, mine.isSchedule() ? plugin.getLocales().getComponent(locale, LocalesPaths.ENABLE) : plugin.getLocales().getComponent(locale, LocalesPaths.DISABLE)).get());
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
