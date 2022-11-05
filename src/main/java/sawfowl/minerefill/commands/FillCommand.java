package sawfowl.minerefill.commands;

import java.util.Arrays;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
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

public class FillCommand extends AbstractCommand {

	public FillCommand(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		SourceData sourceData = createSourceData(context.cause());
		Locale locale = sourceData.getLocaleSource().locale();
		if(!plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.NOT_SELECTED));
		Mine mine = plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).get();
		Sponge.asyncScheduler().executor(plugin.getPluginContainer()).execute(() -> {
			mine.fill(plugin.getPluginContainer(), plugin.getConfig().isActionBarMessages(), plugin.getConfig().isDebug(), sourceData);
			sourceData.sendMessage(plugin.getLocales().getTextReplaced2(locale, TextUtils.replaceMapComponents(Arrays.asList(ReplaceKeys.NAME), Arrays.asList(mine.getDisplayName(locale))), LocalesPaths.FILL_SUCCESS));
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
