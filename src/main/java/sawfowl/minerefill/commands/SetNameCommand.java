package sawfowl.minerefill.commands;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.util.locale.Locales;

import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.api.Mine;
import sawfowl.minerefill.api.SourceData;
import sawfowl.minerefill.configure.LocalesPaths;

public class SetNameCommand extends AbstractCommand {

	private final Parameter.Value<String> locale;

	public SetNameCommand(MineRefill plugin) {
		super(plugin);
		List<String> locales = plugin.getLocales().getLocaleService().getLocalesList().stream().map(Locale::toLanguageTag).collect(Collectors.toList());
		locales.add("CONSOLE");
		locale = Parameter.choices(locales.toArray(new String[]{})).optional().key("Locale").build();
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		SourceData sourceData = createSourceData(context.cause());
		Locale locale = sourceData.getLocaleSource().locale();
		if(!plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.NOT_SELECTED));
		if(!context.one(this.locale).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.SET_NAME_LOCALE_NOT_PRESENT));
		if(!context.one(CommandParameters.NAME).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.SET_NAME_NAME_NOT_PRESENT));
		Mine mine = plugin.getMineAPI().getEditableMine(sourceData.getIdentifier()).get();
		Locale selectedLocale = plugin.getLocales().getLocaleService().getLocalesList().stream().filter(l -> (l.toLanguageTag().equals(context.one(this.locale).get()))).findFirst().orElse(Locales.DEFAULT);
		mine.addDisplayName(selectedLocale, context.one(CommandParameters.NAME).get());
		sourceData.sendMessage(plugin.getLocales().getText(locale, LocalesPaths.SET_NAME_SUCCESS));
		return success();
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.EDIT)
				.addParameters(locale, CommandParameters.NAME)
				.executor(this)
				.build();
	}

}
