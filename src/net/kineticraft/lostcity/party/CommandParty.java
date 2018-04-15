package net.kineticraft.lostcity.party;

import net.kineticraft.lostcity.commands.PlayerCommand;
import net.kineticraft.lostcity.mechanics.CurrentEvent;
import net.kineticraft.lostcity.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sun.util.resources.cldr.tn.CurrencyNames_tn;

/**
 * Teleport to a party.
 * Created by Kneesnap on 9/14/2017.
 */
public class CommandParty extends PlayerCommand {
    public CommandParty() {
        super("", "Warp to the active party.", "party");
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        if(CurrentEvent.isEventActive())
            p.teleport(CurrentEvent.getEventWarpLocation());
        else
            p.sendMessage(ChatColor.RED + "There is no active event at this time.");
    }
}
