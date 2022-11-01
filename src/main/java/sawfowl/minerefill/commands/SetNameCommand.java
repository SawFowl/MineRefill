package sawfowl.minerefill.commands;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.locale.LocaleSource;
import org.spongepowered.api.util.locale.Locales;

import net.kyori.adventure.audience.Audience;
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.data.Mine;

public class SetNameCommand extends AbstractCommand {

	private final Parameter.Value<String> locale;

	public SetNameCommand(MineRefill plugin) {
		super(plugin);
		List<String> locales = plugin.getLocales().getLocaleService().getLocalesList().stream().map(Locale::toLanguageTag).collect(Collectors.toList());
		locales.add("CONSOLE");
		locale = Parameter.choices(locales.toArray(String[]::new)).optional().key("Locale").build();
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		Audience audience = context.cause().audience();
		Locale locale = audience instanceof LocaleSource ? ((LocaleSource) audience).locale() : Locales.DEFAULT;
		if(!(audience instanceof ServerPlayer)) exception(plugin.getLocales().getText(locale, LocalesPaths.ONLY_PLAYER));
		ServerPlayer player = (ServerPlayer) audience;
		if(!plugin.getEditableMine(player.uniqueId()).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.NOT_SELECTED));
		if(!context.one(this.locale).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.SET_NAME_LOCALE_NOT_PRESENT));
		if(!context.one(CommandParameters.NAME).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.SET_NAME_NAME_NOT_PRESENT));
		Mine mine = plugin.getEditableMine(player.uniqueId()).get();
		Locale selectedLocale = plugin.getLocales().getLocaleService().getLocalesList().stream().filter(l -> (l.toLanguageTag().equals(context.one(this.locale).get()))).findFirst().orElse(Locales.DEFAULT);
		mine.addDisplayName(selectedLocale, context.one(CommandParameters.NAME).get());
		player.sendMessage(plugin.getLocales().getText(locale, LocalesPaths.SET_NAME_SUCCESS));
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
