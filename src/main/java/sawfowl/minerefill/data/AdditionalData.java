package sawfowl.minerefill.data;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.plugin.PluginContainer;

@ConfigSerializable
public abstract class AdditionalData {

	PluginContainer container;
	String plugin;
	String key;
	public AdditionalData(PluginContainer plugin, String key) throws NullPointerException {
		if(plugin == null) throw new NullPointerException("You must specify the PluginContainer!");
		if(key == null) throw new NullPointerException("You must specify the data key!");
		this.container = plugin;
		this.plugin = plugin.metadata().id();
		this.key = key;
	}

	/**
	 * Getting a class to deserialize a custom data.
	 * 
	 * @param clazz - The class is a descendant of CompoundTag.
	 */
	@SuppressWarnings({"unchecked" }) 
	public static Class<AdditionalData> getClass(Class<?> clazz) {
		try {
			return (Class<AdditionalData>) (Object) clazz;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return null;
	}

}
