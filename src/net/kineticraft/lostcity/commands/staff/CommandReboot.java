package net.kineticraft.lostcity.commands.staff;

import net.kineticraft.lostcity.EnumRank;
import net.kineticraft.lostcity.commands.PlayerCommand;
import net.kineticraft.lostcity.commands.StaffCommand;
import net.kineticraft.lostcity.utils.ServerUtils;
import net.kineticraft.lostcity.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Reboots the server, or show how much time there is until a reboot.
 * Created by Kneesnap on 6/1/2017.
 */
public class CommandReboot extends StaffCommand {

    public CommandReboot() {
        super(EnumRank.HR, "<delay>", "See how much time is left until the next reboot.", "reboot", "shutdown");
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        ServerUtils.reboot(Integer.parseInt(args[0]));
    }
}
