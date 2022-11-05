package sawfowl.minerefill.commands;

import org.spongepowered.api.command.Command.Parameterized;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import sawfowl.localeapi.api.TextUtils;
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.api.Mine;
import sawfowl.minerefill.api.SourceData;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;

public class SelectCommand extends AbstractCommand {

	public SelectCommand(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		SourceData sourceData = createSourceData(context.cause());
		Locale locale = sourceData.getLocaleSource().locale();
		Optional<Mine> mine = getMine(context);
		if(!mine.isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.SELECT_EXCEPTION));
		plugin.getMineAPI().addEditableMine(sourceData.getIdentifier(), mine.get());
		sourceData.getAudience().sendMessage(plugin.getLocales().getTextReplaced2(locale, TextUtils.replaceMapComponents(Arrays.asList(ReplaceKeys.NAME), Arrays.asList(mine.get().getDisplayName(locale))), LocalesPaths.LIST_MINE_SELECTED));
		return success();
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.LIST)
				.addParameter(CommandParameters.UUID)
				.executor(this)
				.build();
	}

	private Optional<Mine> getMine(CommandContext context) {
		return plugin.getMineAPI().getMines().stream().filter(mine -> (mine.getUniqueid().equals(getMineUUID(context)))).findFirst();
	}

	private UUID getMineUUID(CommandContext context) {
		return context.one(CommandParameters.UUID).orElse(new UUID(0, 0));
	}

}
