package sawfowl.minerefill.commands;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.blockray.RayTraceResult;
import org.spongepowered.api.util.locale.LocaleSource;
import org.spongepowered.api.util.locale.Locales;
import org.spongepowered.api.world.LocatableBlock;

import net.kyori.adventure.audience.Audience;
import sawfowl.localeapi.utils.AbstractLocaleUtil;
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;
import sawfowl.minerefill.data.Mine;

public class AddReserveBlockCommand extends AbstractCommand {

	public AddReserveBlockCommand(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		Audience audience = context.cause().audience();
		Locale locale = audience instanceof LocaleSource ? ((LocaleSource) audience).locale() : Locales.DEFAULT;
		if(!(audience instanceof ServerPlayer)) exception(plugin.getLocales().getText(locale, LocalesPaths.ONLY_PLAYER));
		ServerPlayer player = (ServerPlayer) audience;
		if(!plugin.getEditableMine(player.uniqueId()).isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.NOT_SELECTED));
		Mine mine = plugin.getEditableMine(player.uniqueId()).get();
		Optional<RayTraceResult<LocatableBlock>> blockRay = getLocatableBlock(player);
		if(!blockRay.isPresent()) exception(plugin.getLocales().getText(locale, LocalesPaths.ADD_RESERVE_BLOCK_BLOCK_NOT_PRESENT));
		BlockState block = blockRay.get().selectedObject().blockState();
		if(mine.containsReserverBlock(block)) exception(plugin.getLocales().getText(locale, LocalesPaths.ADD_RESERVE_BLOCK_ALREADY_EXIST));
		mine.addReserveBlock(block);
		player.sendMessage(plugin.getLocales().getTextReplaced1(locale, AbstractLocaleUtil.replaceMap(Arrays.asList(ReplaceKeys.BLOCK), Arrays.asList(blockID(block))), LocalesPaths.ADD_RESERVE_BLOCK_SUCCESS));
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
