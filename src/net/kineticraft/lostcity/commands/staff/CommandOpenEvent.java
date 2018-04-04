package net.kineticraft.lostcity.commands.staff;

import net.kineticraft.lostcity.EnumRank;
import net.kineticraft.lostcity.commands.StaffCommand;
import net.kineticraft.lostcity.mechanics.CurrentEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandOpenEvent extends StaffCommand {
    public CommandOpenEvent() {
        super(EnumRank.JR_BUILDER, "", "Toggle whether or not the current event is open to players.", "openevent");
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        if(CurrentEvent.toggleEventActive())
            sender.sendMessage(ChatColor.BLUE + "The current event has been opened to players.");
        else
            sender.sendMessage(ChatColor.BLUE + "The current event has been closed to players.");
    }
}
