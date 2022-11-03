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

public class DeleteCommand extends AbstractCommand {

	public DeleteCommand(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		SourceData sourceData = createSourceData(context.cause());
		Locale locale = sourceData.getLocaleSource().locale();
		if(!plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.NOT_SELECTED));
		Mine mine = plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).get();
		if(!plugin.getMineAPI().getMines().contains(mine)) exception(plugin.getLocales().getText(locale, LocalesPaths.DELETE_UNSAVED));
		plugin.getMineAPI().deleteMine(mine);
		plugin.getMineAPI().getEditableMines().remove(sourceData.getIdentifier());
		sourceData.sendMessage(plugin.getLocales().getText(locale, LocalesPaths.DELETE_SUCCESS));
		return success();
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.EDIT)
				.executor(this)
				.build();
	}

}
