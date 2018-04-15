package net.kineticraft.lostcity.commands.staff;

import net.kineticraft.lostcity.Core;
import net.kineticraft.lostcity.EnumRank;
import net.kineticraft.lostcity.commands.StaffCommand;
import net.kineticraft.lostcity.data.KCPlayer;
import net.kineticraft.lostcity.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Allows media+ to vanish.
 * Created by Kneesnap on 6/11/2017.
 */
public class CommandVanish extends StaffCommand {

    public CommandVanish() {
        super(EnumRank.MEDIA, "", "Vanish from the game.", "vanish", "unvanish");
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        KCPlayer player = KCPlayer.getWrapper(p);
        boolean vanished = !player.isVanished();
        player.vanish(vanished);
        Core.broadcast(" " + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "> " + ChatColor.RESET + player.getRank().getNameColor() +
                player.getUsername() + ChatColor.YELLOW + " has " + (vanished ? "left." : "joined."));
        sender.sendMessage(Utils.formatToggle("Vanished", player.isVanished()));
        Core.alertStaff(player.getColoredName() + ChatColor.GRAY + " has " + (player.isVanished() ? "" : "un") + "vanished.");
    }
}