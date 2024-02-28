package sawfowl.minerefill.commands;

import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;

import sawfowl.localeapi.api.EnumLocales;
import sawfowl.minerefill.MineRefill;
import sawfowl.minerefill.api.Mine;

import java.time.Duration;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandParameters {

	private static final Map<String, Locale> LOCALES_MAP = Stream.of(EnumLocales.values()).collect(Collectors.toMap(entry -> entry.getTag(), entry -> entry.get()));

	private static final java.util.List<CommandCompletion> POSITIONS = Arrays.asList(CommandCompletion.of("1"), CommandCompletion.of("2"));

	public static final Parameter.Value<Integer> POSITION = Parameter.rangedInteger(1, 2).completer((context, currentInput) -> POSITIONS).optional().key("Position").build();

	public static final Parameter.Value<Double> CHANCE = Parameter.rangedDouble(0, 100).optional().key("Chance").build();

	public static final Parameter.Value<Duration> TIME = Parameter.duration().optional().key("Time").build();

	public static final Parameter.Value<Boolean> SCHEDULE = Parameter.bool().optional().key("Schedule").build();

	public static final Parameter.Value<String> NAME = Parameter.remainingJoinedStrings().optional().key("Name").build();

	public static final Parameter.Value<Mine> MINE = Parameter.builder(Mine.class).key("Mine").completer((context, currentInput) -> MineRefill.getInstance().getMineAPI().getMines().stream().filter(mine -> (mine.getUniqueid().toString().contains(currentInput))).map(mine -> CommandCompletion.of(mine.getUniqueid().toString())).toList()).addParser(VariableValueParameters.dynamicChoicesBuilder(Mine.class).choices(() -> MineRefill.getInstance().getMineAPI().getMines().stream().map(mine -> mine.getUniqueid().toString()).toList()).results(key -> MineRefill.getInstance().getMineAPI().getMines().stream().filter(mine -> mine.getUniqueid().toString().equals(key)).findFirst().orElse(null)).build()).build();

	public static final Parameter.Value<Locale> LOCALE = Parameter.builder(Locale.class).key("Locale").completer((context, input) -> LOCALES_MAP.values().stream().filter(locale -> input.isEmpty() || locale.toLanguageTag().contains(input)).map(Locale::toLanguageTag).map(CommandCompletion::of).toList()).addParser(VariableValueParameters.dynamicChoicesBuilder(Locale.class).choices(() -> LOCALES_MAP.keySet()).results(key -> LOCALES_MAP.containsKey(key) ? LOCALES_MAP.get(key) : null).build()).build();

}
