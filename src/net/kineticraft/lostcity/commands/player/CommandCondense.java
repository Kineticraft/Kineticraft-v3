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
    private static final List<Material> BLOCKS = Arrays.asList(Material.REDSTONE, Material.COAL, Material.EMERALD,
            Material.IRON_INGOT, Material.GOLD_INGOT, Material.DIAMOND, Material.MELON, Material.SLIME_BALL);
    private static final Map<ItemStack, Material> CONDENSE = new HashMap<>();
    static {
        //noinspection deprecation
        CONDENSE.put(new ItemStack(Material.INK_SACK, 1, DyeColor.BLUE.getDyeData()), Material.LAPIS_BLOCK); // Lapis
        CONDENSE.put(new ItemStack(Material.WHEAT, 1), Material.HAY_BLOCK);

        // Take all of the blocks and add them to the condense list.
        for (Material m : BLOCKS)
            CONDENSE.put(new ItemStack(m, 1), Material.valueOf(m.name().split("_")[0] + "_BLOCK"));
    }

    public CommandCondense() {
        super(EnumRank.ALPHA, "", "Condense all items in your inventory.", "condense");
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        List<Pair<List<ItemStack>, Integer>> ninv = new ArrayList<>();
        PlayerInventory inv = player.getInventory();
        for (int i = 0;i < 36;++ i) {
            ItemStack item = invGetItem(inv, i);
            if (Utils.isAir(item)) {
                continue;
            }
            ItemStack uncondensed = getKeyFromCondense(item.getType());
            if(uncondensed != null) {
                int j = getFirstOccurrence(inv, uncondensed);
                if(j != -1) {
                    ninv.add(new Pair<>(condense(j, inv), i));
                    continue;
                }
            }

            ninv.add(new Pair<>(condense(i, inv), i));
        }
        ninv.stream().sorted((a, b) -> StackAndCondenseUtils.getOrganizationLvl(b.fst.get(0).getType()) -
                StackAndCondenseUtils.getOrganizationLvl(a.fst.get(0).getType())).forEach(e -> {
            if(StackAndCondenseUtils.getOrganizationLvl(e.fst.get(0).getType()) > -1)
                invSetItem(inv, e.snd, e.fst.get(0));
            else{
                for(ItemStack stack : e.fst)
                    Utils.giveItem(player, stack);
            }
        });

        player.updateInventory();
    }

    private static List<ItemStack> condense(int start, PlayerInventory inv) {
        ItemStack stack = invGetItem(inv, start), condense = getFromCondense(stack);
        List<ItemStack> occurrences = new ArrayList<>();
        occurrences.add(stack);
        invSetItem(inv, start, null);
        List<ItemStack> condensedOccurrences = new ArrayList<>();
        List<ItemStack> result = new ArrayList<>();

        for(int i = 0;i < 36;++ i) {
            ItemStack item = invGetItem(inv, i);
            if(Utils.isAir(item))
                continue;
            if(stack.isSimilar(item)) {
                occurrences.add(item);
                invSetItem(inv, i, null);
                continue;
            }
            if(condense != null && item.isSimilar(condense)) {
                condensedOccurrences.add(item);
                invSetItem(inv, i, null);
            }
        }

        if(occurrences.size() == 1 && condensedOccurrences.isEmpty() && condense == null)
            return occurrences;

        int occTotal = 0;
        occTotal = occurrences.stream().map(s -> s.getAmount()).reduce(occTotal, Integer::sum);
        if(condense == null) {
            while(occTotal > 0) {
                if(occTotal >= 64) {
                    result.add(Utils.resize(stack, 64));
                    occTotal -= 64;
                }else{
                    result.add(Utils.resize(stack, occTotal));
                    break;
                }
            }
            return result;
        }

        int cTotal = 0;
        if(!condensedOccurrences.isEmpty())
            cTotal = condensedOccurrences.stream().map(s -> s.getAmount()).reduce(cTotal, Integer::sum);
        int condensedToAdd = occTotal / 9, extra = occTotal % 9;
        cTotal += condensedToAdd;
        while(cTotal > 0) {
            if(cTotal >= 64) {
                result.add(Utils.resize(condense, 64));
                cTotal -= 64;
            }else{
                result.add(Utils.resize(condense, cTotal));
                break;
            }
        }
        result.add(Utils.resize(stack, extra));
        return result;
    }

    private static ItemStack getFromCondense(ItemStack key) {
        for(ItemStack key0 : CONDENSE.keySet()) {
            if(key0.isSimilar(key))
                return new ItemStack(CONDENSE.get(key0));
        }
        return null;
    }

    private static ItemStack getKeyFromCondense(Material value) {
        for(Map.Entry<ItemStack, Material> e : CONDENSE.entrySet()) {
            if(value.equals(e.getValue()))
                return e.getKey();
        }
        return null;
    }

    private static int getFirstOccurrence(PlayerInventory inv, ItemStack stack) {
        for(int i = 0;i < 36;++ i) {
            if(stack.isSimilar(invGetItem(inv, i)))
                return i;
        }
        return -1;
    }

    private static ItemStack invGetItem(PlayerInventory inv, int i) {
        if(i < 27)
            i += 9;
        else
            i -= 27;
        return inv.getItem(i);
    }

    private static void invSetItem(PlayerInventory inv, int i, ItemStack stack) {
        if(i < 27)
            i += 9;
        else
            i -= 27;
        inv.setItem(i, stack);
    }
}
