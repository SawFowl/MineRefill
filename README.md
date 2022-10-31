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
/minerefill reload - Reloading the plugin.
```

**Permissions:**
```
minerefill.user.maincommand - Access to the main command of the plugin.
minerefill.staff.edit - Access to the commands for configuring mines.
minerefill.staff.save - Access to the `/minerefill save` command
minerefill.staff.fill - Access to the `/minerefill fill` command
minerefill.staff.reload - Access to the `/minerefill reload` command
minerefill.user.list - Access to the `/minerefill list` command
minerefill.user.info - Access to the `/minerefill info` command
minerefill.user.teleport.<MineUUID> - Permission to teleport to the mine.
```

**Information about how the plugin works:**
1. When teleporting to the mine, the player moves to the coordinates where all 3 axes have the highest value. Teleportation is available when using the `/minerefill list` command.
2. Blocks added to the mine can be removed. Use the `/minerefill info` command to view the lists of blocks. If the player does not have permission to edit the mine, he will not see the delete button. Localized mine names can be deleted in the same way. 
3. Mine does not have a limit of blocks to be added. All blocks have about a 50% chance of installation. When a block is added, the chance of its selection is indicated. If several blocks meet the selection condition, the block with the lowest probability of selection is chosen. If after the previous selections there are several blocks that have the same chance of selection, then one random block will be selected. The chance of block selection cannot be lower than `0.001%`.
4. The automatic mine update only works if both positions are set, the mine contains at least 1 block, the interval is set and the auto update is enabled. When you set the position, a block of colored glass will appear. It will be displayed on the client, but it will not appear in the game world.
5. There is one minor bug in the work of the plugin, which could not be fixed. When displayed in the console message to update the mine, its localized name is not cleared of the scenery. You can manually edit the config mine and set the localization `CONSOLE` without text decoration. There seems to be some error in the text library.
