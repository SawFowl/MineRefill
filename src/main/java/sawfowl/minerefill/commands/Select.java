package sawfowl.minerefill.commands;

import org.spongepowered.api.command.Command.Parameterized;

import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.api.Mine;
import sawfowl.minerefill.api.SourceData;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;

public class Select extends AbstractCommand {

	public Select(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		SourceData sourceData = createSourceData(context.cause());
		Locale locale = sourceData.getLocale();
		Optional<Mine> mine = context.one(CommandParameters.MINE);
		if(!mine.isPresent()) exception(plugin.getLocales().getComponent(locale, LocalesPaths.SELECT_EXCEPTION));
		plugin.getMineAPI().addEditableMine(sourceData.getIdentifier(), mine.get());
		sourceData.sendMessage(getText(locale, LocalesPaths.LIST_MINE_SELECTED).replace(ReplaceKeys.NAME, mine.get().getDisplayName(locale)).get());
		return success();
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.LIST)
				.addParameter(CommandParameters.MINE)
				.executor(this)
				.build();
	}

}
