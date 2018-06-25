package net.kineticraft.lostcity.commands.player;

import net.kineticraft.lostcity.EnumRank;
import net.kineticraft.lostcity.commands.PlayerCommand;
import net.kineticraft.lostcity.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

public class CommandStack extends PlayerCommand {
    public CommandStack() {
        super(EnumRank.ALPHA, "", "Stack all items of a similar type in your inventory.", "stack");
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        PlayerInventory inv = player.getInventory();
        HashMap<Integer, ItemStack> ninv = new HashMap<>();
        outer:
        for(int i = 0;i < 36;++ i) {
            ItemStack istack = inv.getItem(i);
            if(istack == null)
                continue;
            else
                inv.setItem(i, null);
            for(int j : ninv.keySet()) {
                if(ninv.get(j).isSimilar(istack)) {
                    ninv.get(j).setAmount(ninv.get(j).getAmount() + istack.getAmount());
                    continue outer;
                }
            }
            ninv.put(i, istack);
        }
        ninv.entrySet().stream().sorted((a, b) -> StackAndCondenseUtils.compareOrganizationLevels(a.getValue(), b.getValue())).forEach(s -> {
            ItemStack stack = s.getValue();
            int cidx = s.getKey(), stackSize = stack.getAmount();
            while(stackSize > 0) {
                stack.setAmount(stackSize > 64 ? 64 : stackSize);
                stackSize -= stack.getAmount();
                StackAndCondenseUtils.giveStack(player, cidx++, stack);
            }
        });
        player.updateInventory();
    }
}
