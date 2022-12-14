package sawfowl.minerefill.commands;

import java.util.Arrays;
import java.util.Locale;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.locale.LocaleSource;
import org.spongepowered.api.util.locale.Locales;

import net.kyori.adventure.audience.Audience;
import sawfowl.localeapi.api.TextUtils;
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.api.Mine;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;

public class SetPosCommand extends AbstractCommand {

	public SetPosCommand(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		Audience audience = context.cause().audience();
		Locale locale = audience instanceof LocaleSource ? ((LocaleSource) audience).locale() : Locales.DEFAULT;
		if(!(audience instanceof ServerPlayer)) exception(plugin.getLocales().getText(locale, LocalesPaths.ONLY_PLAYER));
		ServerPlayer player = (ServerPlayer) audience;
		if(!plugin.getMineAPI().getEditableMine(player.uniqueId().toString()).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.NOT_SELECTED));
		if(!context.one(CommandParameters.POSITION).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.SETPOS_UNSELECTED));
		Mine mine = plugin.getMineAPI().getEditableMine(player.uniqueId().toString()).get();
		int position = context.one(CommandParameters.POSITION).get();
		boolean first = position == 1;
		mine.getPositions().setPosition(player.blockPosition(), first);
		if(mine.getPositions().isSet()) {
			mine.getPositions().getAllCorners().forEach(corner -> {
				player.sendBlockChange(corner, BlockTypes.YELLOW_STAINED_GLASS.get().defaultState());
			});
		} else player.sendBlockChange(player.blockPosition(), (first ? BlockTypes.RED_STAINED_GLASS : BlockTypes.LIGHT_BLUE_STAINED_GLASS).get().defaultState());
		player.sendMessage(plugin.getLocales().getTextReplaced1(locale, TextUtils.replaceMap(Arrays.asList(ReplaceKeys.POSITION), Arrays.asList(position)), LocalesPaths.SETPOS_SUCCESS));
		return success();
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.EDIT)
				.addParameter(CommandParameters.POSITION)
				.executor(this)
				.build();
	}

}
