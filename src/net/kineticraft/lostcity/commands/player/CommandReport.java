package net.kineticraft.lostcity.commands.player;

import net.kineticraft.lostcity.commands.PlayerCommand;
import net.kineticraft.lostcity.discord.DiscordAPI;
import net.kineticraft.lostcity.discord.DiscordChannel;
import net.kineticraft.lostcity.mechanics.Callbacks;
import net.kineticraft.lostcity.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CommandReport extends PlayerCommand {

    public CommandReport() {
        super("<player|location|glitch|other>", "Submit a report.","report");
    }

    private static List<String> types = Arrays.asList("Player", "Location", "Glitch", "Staff", "Other");
    private static String prefix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "DOG" + ChatColor.GREEN + " "
            + "Report" + ChatColor.GRAY + ": " + ChatColor.WHITE;
    private static List<String> greetingMessages = Arrays.asList(
            "Hi there!",
            "Hey!",
            "Hello there!",
            "Hi :)",
            "Bonjour!",
            "Sup dawg."
    );
    private static List<String> exitMessages = Arrays.asList(
            "Thanks for reporting, have a pleasant day!",
            "Your report has been processed, big brother is watching!",
            "Thanks report, have happy joy-joy day.",
            "Your Pokemon are now healed, we hope to see you again soon!",
            "Compiling tattle...transfluxing signal...report sent! Thanks!",
            "Report sent, don't forget to cookie your Mod.",
            "Thanks for looking out homie, we got your report!"
    );

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        reportStep(0, 0, player, false, null, null);

        /*String type = args[0].toLowerCase();
        if (types.contains(type)) {
            sender.sendMessage(prefix +
                    Utils.randElement(greetingMessages) +
                    " Everything said from now until the end of the report remains between us.");
            if (type.equals("player")) {
                sender.sendMessage(prefix + "Who are you reporting?");
                Callbacks.listenForChat(player, subject -> {
                    sender.sendMessage(prefix + "What did " + subject + " do, exactly?");
                    Callbacks.listenForChat(player, description -> submitReport(player, type, subject, description));
                });
            } else {
                sender.sendMessage(prefix + "Please describe the situation in as much detail as possible.");
                Callbacks.listenForChat(player, description -> submitReport(player, type, null, description));
            }
        } else {
            showUsage(sender);
        }*/
    }

    private void reportStep(int step, int meta, Player player, boolean anon, String type, String subject) {
        switch(step) {
            case 0:
                player.sendMessage(ChatColor.WHITE + "Do you wish to submit this report anonymously?");
                Callbacks.promptConfirm(player, () -> {
                    reportStep(step + 1, 0, player, true, type, subject);
                }, () -> {
                    reportStep(step + 1, 0, player, false, type, subject);
                }, "Yes", "No");
                break;
            case 1:
                if(anon)
                    player.sendMessage(ChatColor.WHITE + "You are submitting an anonymous report, do you wish to continue?");
                else
                    player.sendMessage(ChatColor.WHITE + "This report will contain your in-game name, do you wish to continue?");
                Callbacks.promptConfirm(player, () -> {
                    reportStep(step + 1, 0, player, anon, type, subject);
                }, () -> {
                    player.sendMessage(ChatColor.WHITE + "Alrighty, then. Have a good day!");
                }, "Yes", "No");
                break;
            case 2:
                player.sendMessage(ChatColor.WHITE + "Please select what type of report you are submitting (" + String.join("|", types.toArray(new String[types.size()])) + "):");
                Callbacks.promptConfirm(player, () -> {
                    if(!"Player".equalsIgnoreCase(types.get(meta)))
                        reportStep(step + 1, 0, player, anon, types.get(meta).toLowerCase(), subject);
                    else{
                        player.sendMessage(ChatColor.WHITE + "Please type the name of the player that you are reporting:");
                        Callbacks.listenForChat(player, subj -> reportStep(step + 1, 0, player, anon, types.get(meta).toLowerCase(), subj));
                    }
                }, () -> {
                    if(meta == types.size() - 1)
                        reportStep(step + 1, 0, player, anon, types.get(types.size() - 1).toLowerCase(), subject);
                    else
                        reportStep(step, meta + 1, player, anon, type, subject);
                }, types.get(meta), meta == types.size() - 1 ? types.get(types.size() - 1) : "Next");
                break;
            case 3:
                player.sendMessage("Please start typing your report:");
                Callbacks.listenForChat(player, descr -> submitReport(player, anon, type, subject, descr));
                break;
        }
    }

    private static void submitReport(Player reporter, boolean anonymous, String type, String subject, String description) {
        Location loc = reporter.getLocation();
        StringBuilder sb = new StringBuilder();
        sb.append("New **").append(type).append("**");
        if(!anonymous)
            sb.append(" report from `").append(reporter.getName()).append('`');
        else
            sb.append(" report (anonymous)");
        sb.append('\n');
        sb.append("Time: `").append(new Date()).append("`\n");
        if(subject != null)
            sb.append("Subject: `").append(subject).append("`\n");
        sb.append("Teleport: `/tl ").append(loc.getBlockX()).append(' ').append(loc.getBlockY()).append(' ').append(loc.getBlockZ())
          .append(' ').append((int)loc.getYaw()).append(' ').append((int)loc.getPitch()).append(' ').append(loc.getWorld().getName()).append("`\n");
        sb.append("Description:\n```\n").append(description).append("\n```");
        DiscordAPI.sendMessage(DiscordChannel.REPORTS, sb.toString());
        reporter.sendMessage(prefix + Utils.randElement(exitMessages));
    }

}