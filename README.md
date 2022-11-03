# MineRefill
####  ***[LocaleAPI](https://ore.spongepowered.org/Semenkovsky_Ivan/LocaleAPI) plugin is required.***

**Commands:**
```
/minerefill create - Create mine.
/minerefill delete - Delete selected mine.
/minerefill setpos <Position> - Set position 1 or 2.
/minerefill addblock <Chance> - Add a block to the mine that you are looking at.
/minerefill addreserveblock - Add a reserve block to the mine that you are looking at.
/minerefill interval <Time> - Time in seconds between mine updates.
/minerefill schedule <Value> - Enable/disable automatic filling of the mine.
/minerefill setname <Locale> <Name> - Setting the name of the mine.
/minerefill info - Information about the mine.
/minerefill save - Save the selected mine.
/minerefill fill - Fill the mine.
/minerefill list - List of mines.
/minerefill select <UUID> - Selecting a mine by its UUID.
/minerefill reload - Reloading the plugin.
```

**Permissions:**
```
minerefill.user.maincommand - Access to the main command of the plugin.
minerefill.staff.edit - Access to the commands for configuring mines.
minerefill.staff.save - Access to the `/minerefill save` command.
minerefill.staff.fill - Access to the `/minerefill fill` command.
minerefill.staff.reload - Access to the `/minerefill reload` command.
minerefill.user.list - Access to the `/minerefill list` and `/minerefill select` commands.
minerefill.user.info - Access to the `/minerefill info` command.
minerefill.user.teleport.<MineUUID> - Permission to teleport to the mine.
```

**Information about how the plugin works:**
1. When teleporting to the mine, the player moves to the coordinates where all 3 axes have the highest value. Teleportation is available when using the `/minerefill list` command.
2. Blocks added to the mine can be removed. Use the `/minerefill info` command to view the lists of blocks. If the player does not have permission to edit the mine, he will not see the delete button. Localized mine names can be deleted in the same way. 
3. There is no limit to the blocks that can be added to the mine. All blocks have about a 50% chance of being set. When a block is added, the chance of its selection is indicated. The first step in selecting a block to set up determines whether the main block or the reserve block is chosen, both have a 50% chance. If the main block is selected, then an attempt is made to calculate which main block will be selected for installation. If more than one block meets the selection condition, the block with the lowest probability of selection is selected. If after the previous selections there are several blocks with the same probability of choice, then one random block is chosen. Block selection probability cannot be lower than `0.001%`. If there are no reserve blocks, the air block is taken instead.
4. The automatic mine update only works if both positions are set, the mine contains at least 1 block, the interval is set and the auto update is enabled. When you set the position, a block of colored glass will appear. It will be displayed on the client, but it will not appear in the game world.


**For developers:** \
**Get API:**
```java
@Plugin("pluginid")
public class Main {
	private MineAPI mineapi;

	// Get API.
	@Listener
	public void onPostMineAPIEvent(PostMineAPIEvent event) {
		mineapi = event.getAPI();
	}

}
```
**Gradle:**
```gradle
repositories {
	...
	maven { 
		name = "JitPack"
		url 'https://jitpack.io' 
	}
}
dependencies {
	...
	implementation 'com.github.SawFowl:MineRefill:1.1.0'
}
```
