package net.kineticraft.lostcity.commands.player;

import net.kineticraft.lostcity.EnumRank;
import net.kineticraft.lostcity.commands.PlayerCommand;
import net.kineticraft.lostcity.utils.Pair;
import net.kineticraft.lostcity.utils.Utils;
import org.bukkit.DyeColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

/**
 * Allow players to condense their items.
 * Created by Kneesnap on 6/16/2017.
 */
public class CommandCondense extends PlayerCommand {
    public CommandCondense() {
        super(EnumRank.ALPHA, "", "Condense all items in your inventory.", "condense");
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        PlayerInventory inv = player.getInventory();
        HashMap<Integer, Pair<ItemStack, ItemStack>> ninv = new HashMap<>();
        outer:
        for(int i = 0;i < 36;++ i) {
            ItemStack istack = inv.getItem(i);
            if(istack == null || !StackAndCondenseUtils.isCondenseItem(istack))
                continue;
            else
                inv.setItem(i, null);
            for(int j : ninv.keySet()) {
                if(ninv.get(j).fst.isSimilar(istack)) {
                    ninv.get(j).fst.setAmount(ninv.get(j).fst.getAmount() + istack.getAmount());
                    continue outer;
                }else if(ninv.get(j).snd.isSimilar(istack)) {
                    ninv.get(j).snd.setAmount(ninv.get(j).snd.getAmount() + istack.getAmount());
                    continue outer;
                }
            }
            Pair<ItemStack, ItemStack> entry = StackAndCondenseUtils.getCondensedForm(istack) == null
                    ? new Pair<>(istack, StackAndCondenseUtils.getUncondensedForm(istack))
                    : new Pair<>(StackAndCondenseUtils.getCondensedForm(istack), istack);
            ninv.put(i, entry);
        }
        ninv.entrySet().stream().forEach(e -> {
            int condensedAmt = e.getValue().fst.getAmount() + (e.getValue().snd.getAmount() / 9),
                extraAmt = e.getValue().snd.getAmount() % 9;
            ItemStack condensed = e.getValue().fst, extra = e.getValue().snd;
            extra.setAmount(extraAmt);
            int cidx = e.getKey();
            while(condensedAmt > 0) {
                condensed.setAmount(condensedAmt > 64 ? 64 : condensedAmt);
                condensedAmt -= condensed.getAmount();
                StackAndCondenseUtils.giveStack(player, cidx++, condensed.clone());
            }
            if(extraAmt > 0)
                StackAndCondenseUtils.giveStack(player, cidx, extra.clone());
        });
        player.updateInventory();
    }
}
