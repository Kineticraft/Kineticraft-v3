package net.kineticraft.lostcity.commands.player;

import net.kineticraft.lostcity.Core;
import net.kineticraft.lostcity.EnumRank;
import net.kineticraft.lostcity.commands.PlayerCommand;

import net.kineticraft.lostcity.discord.DiscordAPI;
import net.kineticraft.lostcity.discord.DiscordChannel;
import net.kineticraft.lostcity.mechanics.Callbacks;
import net.kineticraft.lostcity.mechanics.Chat;
import net.kineticraft.lostcity.utils.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CommandProposeWarp extends PlayerCommand {
    /*
    This Class is to prompt the user for understanding our terms
    Once they accept both agreements, then they can choose which catagory their warp will fall under
    Then they will explain why they want a warp.
    We will then grab the location, and what they had to say and input it into our discord channel
     */

    public CommandProposeWarp() {
        super( EnumRank.GAMMA, "[command]", "Submit a warp permit!","warpform");
    }
    //initializing variables
    private static List<String> types = Arrays.asList("event", "shop", "showcase", "town", "other");

    private static List<String> greetingMessages = Arrays.asList(
            "Hi there, ",
            "Hey, ",
            "Hello there, ",
            "Hi, ",
            "Bonjour, ",
            "Sup dawg, "
    );

    private static List<String> exitMessages = Arrays.asList(
            "Thanks for submitting, have a pleasant day!",
            "Your submission has been processed, big brother is watching!",
            "Thanks for your submission, have a happy happy joy-joy day.",
            "Your Pokemon are now healed, we hope to see you again soon!",
            "Compiling submission...transfluxing signal...submission sent! Thanks!",
            "Submission sent, don't forget to give a cookie to your Mod.",
            "Thanks for looking out homie, we got your submission!"
    );

    private static List<String> declineMessages  = Arrays.asList(
            "Thanks for looking, have a pleasant day!",
            "Have a great day!",
            "Let us know what you think!",
            "Thanks again, Now go find some candy"
    );

    private static String prefix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Borkley" + ChatColor.GREEN + " "
            + "Warp Permit" + ChatColor.GRAY + ": " + ChatColor.WHITE;

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        // you can get more properties by casting as player
        Player player = (Player) sender;

        sender.sendMessage(prefix + "This is to apply for a warp permit. Do you wish to continue? ");

        //This command is to give a prompt to confirm or cancel this request (if confirm continue, if cancel it sends back the usage to the user?)
        Callbacks.promptConfirm(player,
                //either need to test this code into the accept function or come up with a way to store a variable to pass through here
                () -> { sender.sendMessage(  ChatColor.GOLD +" Please stand where you would like your ideal warp before continuing.");
                    //if accepting, continue warpform
                    sender.sendMessage(prefix +  ChatColor.BOLD + " \n Warp Requirements: \n" +
                            ChatColor.AQUA + "1) A safe spawn-in and warp area (well-lit, no mobs, no player harm). \n" +
                            ChatColor.DARK_AQUA + "2) Warps must be fully claimed. \n" +
                            ChatColor.AQUA + "3) You need to apply as a warp owner and need be an active player. \n" +
                            ChatColor.DARK_AQUA + "4) Warp Owners will reapply for warp permits each quarter to prevent dead warps. A quarter takes place Jan-March, April-June, July-Sept, Oct-Dec. \n" + 
                            ChatColor.AQUA + "5) If you fail to pass an application more than 2 times in one quarter, you must wait to reapply next quarter. \n" + ChatColor.RESET);
                       
                    sender.sendMessage(prefix + ChatColor.GOLD + "Do you accept all requirements for the warps? " + ChatColor.RESET);

                    Callbacks.promptConfirm(player,
                            () -> { sender.sendMessage(prefix +
                                    Utils.randElement(greetingMessages) + player.getDisplayName() + ChatColor.RESET +
                                    ", which type of warp permit are you applying for? \n " +
                                    "( " + ChatColor.LIGHT_PURPLE + "Event" + ChatColor.RESET + " | " +
                                    ChatColor.DARK_GREEN + "Shop" + ChatColor.RESET + " | " +
                                    ChatColor.GOLD + "Showcase" + ChatColor.RESET + " | " +
                                    ChatColor.RED + "Town" + ChatColor.RESET + " | " +
                                    ChatColor.DARK_AQUA +"Exit" + ChatColor.RESET + " ) \n Click Next to go to next option. " );
                                    //If Accepting, follow this line of code
                                    Callbacks.promptConfirm(player,
                                        () -> formWarp(sender, "event"),
                                        //If they hit Next: then they will open another Prompt
                                        () ->
                                                Callbacks.promptConfirm(player,
                                                        () -> formWarp(sender, "shop"),
                                                        () ->
                                                                Callbacks.promptConfirm(player,
                                                                        () -> formWarp(sender, "showcase"),
                                                                        () -> Callbacks.promptConfirm(player,
                                                                                () -> formWarp(sender, "town"),
                                                                                () -> Callbacks.promptConfirm(player,
                                                                                        () -> formWarp(sender, "other"),
                                                                                        () -> { sender.sendMessage(prefix + Utils.randElement(declineMessages)); },
                                                                                        "Other Ideas!",
                                                                                         "Exit Submission"
                                                                                ),
                                                                                 "Town",
                                                                                "Other"
                                                                        ),
                                                                         "Showcase",
                                                                        "Next"
                                                                )
                                                        ,
                                                         "Shop",
                                                        "Next"
                                                )
                                        ,
                                              "Event",
                                        "Next"
                                    );

                                    },
                            () -> { sender.sendMessage(prefix + Utils.randElement(declineMessages)); },
                            ChatColor.GREEN.BOLD + "Accept",
                            ChatColor.DARK_RED.BOLD + "Decline"
                            );
                },

                () -> { sender.sendMessage(prefix + Utils.randElement(declineMessages)); },
                ChatColor.GREEN.BOLD + "Accept",
                ChatColor.DARK_RED.BOLD + "Decline"
                );

    }

    private static void formWarp(CommandSender sender, String type ) {
        Player player = (Player) sender;
        if (types.contains(type)) {

            if (type.equals("event")) {
                sender.sendMessage(prefix + " Event Requirements: \n" +
                                    ChatColor.AQUA + "1) Event is publicly posted in #Get-Togethers channel on Discord. \n" +
                                    ChatColor.DARK_AQUA + "2) If temporary, please state the dates needed for the warp. \n"
                        +
                        prefix + "Describe your Event: ");
                Callbacks.listenForChat(player, description -> submitWarp(player, type, description));

            } else if (type.equals("shop")) {
                sender.sendMessage(prefix + " Shop Requirements: \n" +
                                    ChatColor.AQUA +"1) A Rules and information Board. \n" +
                                    ChatColor.DARK_AQUA +"2) At least 8 active players who own a plot/build and keep items stocked. If the build is a solo shop, it must fit into a Showcase quality standard. \n" +
                                    ChatColor.AQUA +"3) A selection of empty and ready plots available. \n" +
                                    ChatColor.DARK_AQUA +"4) If you are inactive, name a user who will take over your duties to prevent the warp from being removed. \n"
                        +
                prefix + "Describe your Shop: ");
                Callbacks.listenForChat(player, description -> submitWarp(player, type, description));

            } else if (type.equals("showcase")) {
                sender.sendMessage(prefix + " Showcase Requirements: \n" +
                                    ChatColor.AQUA + "1) An Info Board at warp-in. \n" +
                                    ChatColor.DARK_AQUA + "2) A finished/completed build. \n" +
                                    ChatColor.AQUA + "3) Build shows excellent build quality and standards. \n" +
                                    ChatColor.DARK_AQUA + "4) Build is fully claimed. \n"
                        +
                prefix + "Describe your Showcase: ");
                Callbacks.listenForChat(player, description -> submitWarp(player, type, description));

            } else if (type.equals("town")) {
                sender.sendMessage(prefix + " Town Requirements: \n" +
                                    ChatColor.AQUA + "1) A Rules and information Board. \n" +
                                    ChatColor.DARK_AQUA + "2) At least 6 active players who own a plot/build and keep items stocked. \n" +
                                    ChatColor.AQUA + "3) A selection of empty and ready plots available. \n" +
                                    ChatColor.DARK_AQUA + "4) If you are inactive, name a user who will take over your duties to prevent the warp from being removed. \n"
                        +
                prefix + "Describe your Town: ");
                Callbacks.listenForChat(player, description -> submitWarp(player, type, description));

            }
            else if (type.equals("other")) {
                sender.sendMessage(prefix + " Other Warp Requirements: \n" +
                                    ChatColor.AQUA + "1) A Rules and information Board. \n" +
                                    ChatColor.DARK_AQUA + "2) Build is fully claimed. \n" +
                                    ChatColor.AQUA + "3) If you are inactive, name a user who will take over your duties to prevent the warp from being removed. \n"
                        +
                prefix + "For Other Warp Options: Please place suggestions in the Suggestions Channel on our discord.");
                //Callbacks.listenForChat(player, description -> submitWarp(player, type, description));
                sender.sendMessage(prefix + Utils.randElement(declineMessages));
            }

        }
    }

    private static void submitWarp(Player reporter, String type, String description) {
        Location loc = reporter.getLocation();
        String message = "" +
                "New **" + type + "** Warp Permit submission from `" + reporter.getName() + "`\n" +
                "Time: `" + new Date().toString() + "`\n" +
                "Teleport: `/tl " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + " " + (int) loc.getYaw() + " " + (int) loc.getPitch() + " " + loc.getWorld().getName() + "`\n" +
                "Description:\n" +
                "```\n" +
                description + "\n" +
                "```";
        DiscordAPI.sendMessage(DiscordChannel.WARP_PROPOSALS, message);
        //Core.logInfo(message);
        reporter.sendMessage(prefix + Utils.randElement(exitMessages));
    }
    private static void endMessage(Player reporter){
        reporter.sendMessage(prefix + Utils.randElement(exitMessages));
    }

}
