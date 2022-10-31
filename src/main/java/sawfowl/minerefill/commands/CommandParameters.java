package sawfowl.minerefill.commands;

import org.spongepowered.api.command.parameter.Parameter;

import sawfowl.minerefill.MineRefill;

import java.util.Locale;

public class CommandParameters {

	public static final Parameter.Value<Integer> POSITION = Parameter.rangedInteger(1, 2).optional().key("Position").build();

	public static final Parameter.Value<Double> CHANCE = Parameter.rangedDouble(0, 100).optional().key("Chance").build();

	public static final Parameter.Value<Integer> TIME = Parameter.integerNumber().optional().key("Time").build();

	public static final Parameter.Value<Boolean> SCHEDULE = Parameter.bool().optional().key("Schedule").build();

	public static final Parameter.Value<String> LOCALE = Parameter.choices(MineRefill.getInstance().getLocales().getLocaleService().getLocalesList().stream().map(Locale::toLanguageTag).toArray(String[]::new)).optional().key("Locale").build();

	public static final Parameter.Value<String> NAME = Parameter.remainingJoinedStrings().optional().key("Name").build();

}
