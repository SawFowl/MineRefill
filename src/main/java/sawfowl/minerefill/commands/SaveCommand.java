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

public class SaveCommand extends AbstractCommand {

	public SaveCommand(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		SourceData sourceData = createSourceData(context.cause());
		Locale locale = sourceData.getLocaleSource().locale();
		if(!plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.NOT_SELECTED));
		Mine mine = plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).get();
		if(mine.getPositions().getMin() == null || mine.getPositions().getMax() == null) exception(plugin.getLocales().getText(locale, LocalesPaths.SAVE_POSITIONS_NOT_PRESENT));
		if(mine.getBlocks().isEmpty() && mine.getReserveBlocks().isEmpty()) exception(plugin.getLocales().getText(locale, LocalesPaths.SAVE_BLOCKS_NOT_PRESENT));
		plugin.getMineAPI().saveMine(mine);
		sourceData.sendMessage(plugin.getLocales().getText(locale, LocalesPaths.SAVE_SUCCESS));
		return success();
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.SAVE)
				.executor(this)
				.build();
	}

}
