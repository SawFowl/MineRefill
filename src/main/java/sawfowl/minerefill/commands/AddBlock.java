package sawfowl.minerefill.commands;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.blockray.RayTraceResult;
import org.spongepowered.api.util.locale.LocaleSource;
import org.spongepowered.api.world.LocatableBlock;

import net.kyori.adventure.audience.Audience;

import sawfowl.localeapi.api.services.LocaleService;
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.Permissions;
import sawfowl.minerefill.api.Mine;
import sawfowl.minerefill.configure.LocalesPaths;
import sawfowl.minerefill.configure.ReplaceKeys;
import sawfowl.minerefill.data.MineBlock;

public class AddBlock extends AbstractCommand {

	public AddBlock(MineRefill plugin) {
		super(plugin);
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		Audience audience = context.cause().audience();
		Locale locale = audience instanceof LocaleSource ? ((LocaleSource) audience).locale() : LocaleService.getInstance().getSystemOrDefaultLocale();
		if(!(audience instanceof ServerPlayer)) exception(plugin.getLocales().getComponent(locale, LocalesPaths.ONLY_PLAYER));
		ServerPlayer player = (ServerPlayer) audience;
		if(!plugin.getMineAPI().getEditableMine(player.uniqueId().toString()).isPresent()) exception(plugin.getLocales().getComponent(locale, LocalesPaths.NOT_SELECTED));
		if(!context.one(CommandParameters.CHANCE).isPresent()) exception(plugin.getLocales().getComponent(locale, LocalesPaths.ADD_BLOCK_CHANCE_NOT_PRESENT));
		Mine mine = plugin.getMineAPI().getEditableMine(player.uniqueId().toString()).get();
		double chance = BigDecimal.valueOf(context.one(CommandParameters.CHANCE).get()).setScale(3, RoundingMode.HALF_UP).doubleValue();
		Optional<RayTraceResult<LocatableBlock>> blockRay = getLocatableBlock(player);
		if(!blockRay.isPresent()) exception(plugin.getLocales().getComponent(locale, LocalesPaths.ADD_BLOCK_BLOCK_NOT_PRESENT));
		MineBlock mineBlock = new MineBlock(blockRay.get().selectedObject().blockState(), chance);
		if(mine.getBlocks().contains(mineBlock)) exception(plugin.getLocales().getComponent(locale, LocalesPaths.ADD_BLOCK_ALREADY_EXIST));
		mine.addBlock(mineBlock);
		player.sendMessage(getText(locale, LocalesPaths.ADD_BLOCK_SUCCESS).replace(new String[] {ReplaceKeys.BLOCK, ReplaceKeys.CHANCE}, mineBlock.getBlockId(), chance).get());
		return success();
	}

	@Override
	public Parameterized build() {
		return builder()
				.permission(Permissions.EDIT)
				.addParameter(CommandParameters.CHANCE)
				.executor(this)
				.build();
	}
}
