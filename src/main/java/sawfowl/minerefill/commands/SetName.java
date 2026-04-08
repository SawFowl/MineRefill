package sawfowl.minerefill.commands;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import sawfowl.localeapi.api.TextUtils;
import sawfowl.localeapi.api.services.LocaleService;
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.api.Mine;
import sawfowl.minerefill.api.SourceData;
import sawfowl.minerefill.configure.LocalesPaths;

public class SetName extends AbstractCommand {

	public SetName(MineRefill plugin) {
		super(plugin);
		List<String> locales = LocaleService.getInstance().getLocalesList().stream().map(Locale::toLanguageTag).collect(Collectors.toList());
		locales.add("CONSOLE");
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		SourceData sourceData = createSourceData(context.cause());
		Locale locale = sourceData.getLocale();
		if(!plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).isPresent()) exception(plugin.getLocales().getComponent(locale, LocalesPaths.NOT_SELECTED));
		if(!context.one(CommandParameters.LOCALE).isPresent()) exception(plugin.getLocales().getComponent(locale, LocalesPaths.SET_NAME_LOCALE_NOT_PRESENT));
		if(!context.one(CommandParameters.NAME).isPresent()) exception(plugin.getLocales().getComponent(locale, LocalesPaths.SET_NAME_NAME_NOT_PRESENT));
		Mine mine = plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).get();
		mine.addDisplayName(context.one(CommandParameters.LOCALE).get(), TextUtils.deserialize(context.one(CommandParameters.NAME).get()));
		sourceData.sendMessage(plugin.getLocales().getComponent(locale, LocalesPaths.SET_NAME_SUCCESS));
		return success();
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.EDIT)
				.addParameters(CommandParameters.LOCALE, CommandParameters.NAME)
				.executor(this)
				.build();
	}

}
