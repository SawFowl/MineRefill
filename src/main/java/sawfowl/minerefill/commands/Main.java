package sawfowl.minerefill.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.util.locale.LocaleSource;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import sawfowl.localeapi.api.services.LocaleService;
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.configure.LocalesPaths;

public class Main extends AbstractCommand {

	public Main(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		Audience audience = context.cause().audience();
		Locale locale = audience instanceof LocaleSource ? ((LocaleSource) audience).locale() : LocaleService.getInstance().getSystemOrDefaultLocale();
		Collection<Component> help = new ArrayList<>();
		if(context.cause().hasPermission(Permissions.EDIT)) {
			help.add(toText("&a/minerefill create").clickEvent(ClickEvent.runCommand("/minerefill create")).append(toText(" ")).append(plugin.getLocales().getComponent(locale, LocalesPaths.MAIN_CREATE)));
			help.add(toText("&a/minerefill delete").clickEvent(ClickEvent.runCommand("/minerefill create")).append(toText(" ")).append(plugin.getLocales().getComponent(locale, LocalesPaths.MAIN_DELETE)));
			help.add(toText("&a/minerefill setpos &b<Position>").clickEvent(ClickEvent.suggestCommand("/minerefill setpos ")).append(toText(" ")).append(plugin.getLocales().getComponent(locale, LocalesPaths.MAIN_SETPOS)));
			help.add(toText("&a/minerefill addblock &b<Chance>").clickEvent(ClickEvent.suggestCommand("/minerefill addblock ")).append(toText(" ")).append(plugin.getLocales().getComponent(locale, LocalesPaths.MAIN_ADD_BLOCK)));
			help.add(toText("&a/minerefill addreserveblock").clickEvent(ClickEvent.runCommand("/minerefill addreserveblock")).append(toText(" ")).append(plugin.getLocales().getComponent(locale, LocalesPaths.MAIN_ADD_RESERVE_BLOCK)));
			help.add(toText("&a/minerefill interval &b<Time>").clickEvent(ClickEvent.suggestCommand("/minerefill interval ")).append(toText(" ")).append(plugin.getLocales().getComponent(locale, LocalesPaths.MAIN_INTERVAL)));
			help.add(toText("&a/minerefill schedule &b<Value>").clickEvent(ClickEvent.suggestCommand("/minerefill schedule ")).append(toText(" ")).append(plugin.getLocales().getComponent(locale, LocalesPaths.MAIN_SCHEDULE)));
			help.add(toText("&a/minerefill setname &b<Locale> <Name>").clickEvent(ClickEvent.suggestCommand("/minerefill setname ")).append(toText(" ")).append(plugin.getLocales().getComponent(locale, LocalesPaths.MAIN_SET_NAME)));
		}
		if(context.cause().hasPermission(Permissions.INFO)) help.add(toText("&a/minerefill info").clickEvent(ClickEvent.runCommand("/minerefill info")).append(toText(" ")).append(plugin.getLocales().getComponent(locale, LocalesPaths.MAIN_INFO)));
		if(context.cause().hasPermission(Permissions.SAVE)) help.add(toText("&a/minerefill save").clickEvent(ClickEvent.runCommand("/minerefill save")).append(toText(" ")).append(plugin.getLocales().getComponent(locale, LocalesPaths.MAIN_SAVE)));
		if(context.cause().hasPermission(Permissions.FILL)) help.add(toText("&a/minerefill fill").clickEvent(ClickEvent.runCommand("/minerefill fill")).append(toText(" ")).append(plugin.getLocales().getComponent(locale, LocalesPaths.MAIN_FILL)));
		if(context.cause().hasPermission(Permissions.LIST)) {
			help.add(toText("&a/minerefill list").clickEvent(ClickEvent.runCommand("/minerefill list")).append(toText(" ")).append(plugin.getLocales().getComponent(locale, LocalesPaths.MAIN_LIST)));
			help.add(toText("&a/minerefill select <UUID>").clickEvent(ClickEvent.suggestCommand("/minerefill select")).append(toText(" ")).append(plugin.getLocales().getComponent(locale, LocalesPaths.MAIN_SELECT)));
		}
		if(context.cause().hasPermission(Permissions.RELOAD)) help.add(toText("&a/minerefill reload").clickEvent(ClickEvent.runCommand("/minerefill reload")).append(toText(" ")).append(plugin.getLocales().getComponent(locale, LocalesPaths.MAIN_RELOAD)));
		sendPagination(audience, locale, help, plugin.getLocales().getComponent(locale, LocalesPaths.MAIN_TITLE), plugin.getLocales().getComponent(locale, LocalesPaths.PADDING));
		return success();
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.MAIN_COMMAND)
				.executor(this)
				.addChild(new Create(plugin).build(), "create")
				.addChild(new Delete(plugin).build(), "delete")
				.addChild(new SetPos(plugin).build(), "setpos")
				.addChild(new AddBlock(plugin).build(), "addblock")
				.addChild(new AddReserveBlock(plugin).build(), "addreserveblock")
				.addChild(new SetInterval(plugin).build(), "interval")
				.addChild(new SetSchedule(plugin).build(), "schedule")
				.addChild(new SetName(plugin).build(), "setname")
				.addChild(new Info(plugin).build(), "info")
				.addChild(new Save(plugin).build(), "save")
				.addChild(new Fill(plugin).build(), "fill")
				.addChild(new List(plugin).build(), "list")
				.addChild(new Select(plugin).build(), "select")
				.addChild(new Reload(plugin).build(), "reload")
				.build();
	}

}
