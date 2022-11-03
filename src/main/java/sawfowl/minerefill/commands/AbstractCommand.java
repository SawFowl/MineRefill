package sawfowl.minerefill.commands;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.Command.Builder;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.blockray.RayTrace;
import org.spongepowered.api.util.blockray.RayTraceResult;
import org.spongepowered.api.util.locale.LocaleSource;
import org.spongepowered.api.world.LocatableBlock;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.api.SourceData;

public abstract class AbstractCommand implements CommandExecutor {

	final MineRefill plugin;
	public AbstractCommand(MineRefill plugin) {
		this.plugin = plugin;
	}

	Component toText(String string) {
		return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
	}

	CommandResult success() {
		return CommandResult.success();
	}

	CommandException exception(Component text) throws CommandException {
		throw new CommandException(text);
	}

	String blockID(BlockState block) {
		return Sponge.game().registry(RegistryTypes.BLOCK_TYPE).valueKey(block.type()).asString();
	}

	Optional<RayTraceResult<LocatableBlock>> getLocatableBlock(ServerPlayer player) {
		return RayTrace.block()
				.world(player.world())
				.sourceEyePosition(player)
				.limit(5)
				.direction(player)
				.select(RayTrace.nonAir())
				.execute();
	}

	void sendPagination(Audience audience, Locale locale, List<Component> list, Component title, Component padding) {
		PaginationList.builder()
		.contents(list)
		.linesPerPage(20)
		.padding(padding)
		.title(title)
		.sendTo(audience);
	}

	public abstract Command.Parameterized build();

	Builder builder() {
		return Command.builder();
	}

	SourceData createSourceData(CommandCause commandCause) {
		return new SourceData() {

			@Override
			public Subject getSubject() {
				return commandCause.subject();
			}
			
			@Override
			public LocaleSource getLocaleSource() {
				return commandCause.audience() instanceof LocaleSource ? (LocaleSource) commandCause.audience() : Sponge.systemSubject();
			}
			
			@Override
			public Audience getAudience() {
				return commandCause.audience();
			}
		};
	}

}
