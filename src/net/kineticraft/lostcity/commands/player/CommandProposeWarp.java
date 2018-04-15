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
            "Sup, dawg, "
    );

    private static List<String> exitMessages = Arrays.asList(
            "Thanks for submitting; have a pleasant day!",
            "Your submission has been processed...big brother is watching!",
            "Thanks for your submission. Have a Happy-Happy-Joy-Joy day.",
            "Your Pokemon are now healed. We hope to see you again soon!",
            "Compiling submission...transfluxing signal...submission sent! Thanks!",
            "Submission sent, don't forget to give a cookie to your Mod.",
            "Thanks for looking out, Homie, we got your submission!"
    );

    private static List<String> declineMessages  = Arrays.asList(
            "Thanks for looking, have a pleasant day!",
            "Have a great day!",
            "Let us know what you think!",
            "Thanks again. Now go find some candy!"
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
            () -> { sender.sendMessage(  ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + " Please stand where you would like your ideal warp before continuing.");
                //if accepting, continue warpform
                sender.sendMessage(prefix +  ChatColor.BOLD + " \n Warp Requirements: \n" +
                        ChatColor.AQUA + "1) A safe spawn-in and warp area (well-lit, no mobs, no player harm). \n" +
                        ChatColor.DARK_AQUA + "2) Warps must be fully claimed. \n" +
                        ChatColor.AQUA + "3) You need to apply as the claim owner and you need to be an active player. \n" +
                        ChatColor.DARK_AQUA + "4) Warp Owners will reapply for warp permits each quarter to prevent dead warps. A quarter takes place Jan-March, April-June, July-Sept, Oct-Dec. \n" +
                        ChatColor.AQUA + "5) If you fail to pass an application more than 2 times in one quarter, you must wait to reapply next quarter. \n" + ChatColor.RESET);

                sender.sendMessage(prefix + ChatColor.GOLD + "Do you accept all requirements for the warps? " + ChatColor.RESET);

                Callbacks.promptConfirm(player,
                    () -> {
                        sender.sendMessage(prefix +
                        Utils.randElement(greetingMessages) + player.getDisplayName() + ChatColor.RESET +
                        ", which type of warp permit are you applying for? \n " +
                        "( " + ChatColor.LIGHT_PURPLE + "Event" + ChatColor.RESET + " | " +
                        ChatColor.DARK_GREEN + "Shop" + ChatColor.RESET + " | " +
                        ChatColor.GOLD + "Showcase" + ChatColor.RESET + " | " +
                        ChatColor.RED + "Town" + ChatColor.RESET + " | " +
                        ChatColor.YELLOW + "Public Farm" + ChatColor.RESET + " | " +
                        ChatColor.DARK_AQUA +"Exit" + ChatColor.RESET + " ) \n Click Next to go to next option. " );

                        //If Accepting, follow this line of code
                        Callbacks.promptConfirm(player,
                            () -> formWarp(sender, "event"),
                            //If they hit Next: then they will open another Prompt
                            () -> Callbacks.promptConfirm(player,
                                () -> formWarp(sender, "shop"),
                                () -> Callbacks.promptConfirm(player,
                                    () -> formWarp(sender, "showcase"),
                                    () -> Callbacks.promptConfirm(player,
                                        () -> formWarp(sender, "town"),
                                        () -> Callbacks.promptConfirm(player,
                                            () -> formWarp(sender, "farm"),
                                            () -> { sender.sendMessage(prefix + Utils.randElement(declineMessages)); },
                                            "Public Farm", "Exit Submission"
                                        ), "Town", "Public Farm"
                                    ), "Showcase", "Next"
                                ),"Shop", "Next"
                            ), "Event", "Next"
                        );
                    },
                    () -> { sender.sendMessage(prefix + Utils.randElement(declineMessages)); },
                    ChatColor.GREEN.BOLD + "Accept", ChatColor.DARK_RED.BOLD + "Decline"
                );
            },
            () -> { sender.sendMessage(prefix + Utils.randElement(declineMessages)); },
            ChatColor.GREEN.BOLD + "Accept", ChatColor.DARK_RED.BOLD + "Decline"
        );
    }

    private static void formWarp(CommandSender sender, String type ) {
        Player player = (Player) sender;
        if (types.contains(type)) {

            if (type.equals("event")) {
                sender.sendMessage(prefix + " Event Requirements: \n" +
                                    ChatColor.AQUA + "1) Event is publicly posted in #Get-Togethers channel on Discord. \n" +
                                    ChatColor.DARK_AQUA + "2) An Info Board on how to play. \n" +
                                    ChatColor.AQUA + "3) A finished build, fully claimed, and fully built. \n" +
                                    ChatColor.DARK_AQUA + "4) Please state the dates needed for the warp. \n"
                        +
                        prefix + "Describe your Event: ");
                Callbacks.listenForChat(player, description -> submitWarp(player, type, description));

            } else if (type.equals("shop")) {
                sender.sendMessage(prefix + " Shop Requirements: \n" +
                                    ChatColor.AQUA +"1) A Rules and Info Board. \n" +
                                    ChatColor.DARK_AQUA +"2) At least 6 active players who own a plot/build and keep items stocked. \n" +
                                    ChatColor.AQUA +"3) A selection of empty and ready plots available. \n" +
                                    ChatColor.DARK_AQUA +"4) Skip 2-3 if this is not a community/plot shop. Your build must be of VERY excellent quality and be complete. \n" +
                                    ChatColor.AQUA + "5) Name a successor in case you become inactive. \n"
                        +
                prefix + "Describe your Shop: ");
                Callbacks.listenForChat(player, description -> submitWarp(player, type, description));

            } else if (type.equals("showcase")) {
                sender.sendMessage(prefix + " Showcase Requirements: \n" +
                                    ChatColor.AQUA + "1) An Info Board. \n" +
                                    ChatColor.DARK_AQUA + "2) Majority of build (over 70%) must be complete. \n" +
                                    ChatColor.AQUA + "3) Excellent build quality. \n" +
                                    ChatColor.DARK_AQUA + "4) Fully claimed. \n"
                        +
                prefix + "Describe your Showcase: ");
                Callbacks.listenForChat(player, description -> submitWarp(player, type, description));

            } else if (type.equals("town")) {
                sender.sendMessage(prefix + " Town Requirements: \n" +
                                    ChatColor.AQUA + "1) A Rules and Info Board. \n" +
                                    ChatColor.DARK_AQUA + "2) At least 6 active players who own a plot/build. \n" +
                                    ChatColor.AQUA + "3) A selection of empty and ready plots available. \n" +
                                    ChatColor.DARK_AQUA + "4) Name a successor in case you become inactive. \n"
                        +
                prefix + "Describe your Town: ");
                Callbacks.listenForChat(player, description -> submitWarp(player, type, description));

            } else if(type.equals("farm")) {
                sender.sendMessage(prefix + " Public Farm Requirements: \n" +
                        ChatColor.AQUA + "1) Player safety from redstone devices or mobs. \n" +
                        ChatColor.DARK_AQUA + "2) A publically trusted chest for drops (commands: /subdivideclaim /ct public). \n" +
                        ChatColor.AQUA + "3) Excellent build quality. \n" +
                        ChatColor.DARK_AQUA + "4) Cannot be lag-inducing, cannot be used to bypass AFK timer. \n" +
                        ChatColor.AQUA + "5) Build must be complete. \n"
                        +
                        prefix + "Describe your Town: ");
                Callbacks.listenForChat(player, description -> submitWarp(player, type, description));

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
