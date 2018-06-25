package net.kineticraft.lostcity.commands.player;

import net.kineticraft.lostcity.utils.Utils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class StackAndCondenseUtils {
    public static final List<Material> ORGANIZER = Arrays.asList(
            Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SPADE, Material.DIAMOND_SWORD, Material.DIAMOND_HOE,
            Material.GOLD_PICKAXE, Material.GOLD_AXE, Material.GOLD_SPADE, Material.GOLD_SWORD, Material.GOLD_HOE,
            Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SPADE, Material.IRON_SWORD, Material.IRON_HOE,
            Material.STONE_PICKAXE, Material.STONE_AXE, Material.STONE_SPADE, Material.STONE_SWORD, Material.STONE_HOE,
            Material.WOOD_PICKAXE, Material.WOOD_AXE, Material.WOOD_SPADE, Material.WOOD_SWORD, Material.WOOD_HOE,
            Material.BUCKET, Material.WATER_BUCKET, Material.LAVA_BUCKET, Material.MILK_BUCKET, Material.BLACK_SHULKER_BOX,
            Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.CYAN_SHULKER_BOX, Material.GRAY_SHULKER_BOX,
            Material.GREEN_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.LIME_SHULKER_BOX, Material.MAGENTA_SHULKER_BOX,
            Material.ORANGE_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.RED_SHULKER_BOX,
            Material.SILVER_SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX, Material.BOW
    );
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

    public static int getOrganizationLvl(Material material) {
        if(material.isEdible())
            return 0;
        int idx = ORGANIZER.indexOf(material);
        return idx == -1 ? 1 : -((ORGANIZER.size() + 1) - idx);
    }

    public static int compareOrganizationLevels(ItemStack a, ItemStack b) {
        return getOrganizationLvl(a.getType()) - getOrganizationLvl(b.getType());
    }

    public static boolean containsSimilarStack(Collection<ItemStack> c, ItemStack stack) {
        return c.stream().anyMatch(s -> stack.isSimilar(s));
    }

    public static ItemStack getCondensedForm(ItemStack stack) {
        for(Map.Entry<ItemStack, Material> e : CONDENSE.entrySet()) {
            if(stack.isSimilar(e.getKey()))
                return new ItemStack(e.getValue(), 0);
        }
        return null;
    }

    public static ItemStack getUncondensedForm(ItemStack stack) {
        for(Map.Entry<ItemStack, Material> e : CONDENSE.entrySet()) {
            if(stack.getType().equals(e.getValue())) {
                ItemStack copy = e.getKey().clone();
                copy.setAmount(0);
                return copy;
            }
        }
        return null;
    }

    public static boolean isCondenseItem(ItemStack stack) {
        return BLOCKS.contains(stack.getType()) || containsSimilarStack(CONDENSE.keySet(), stack);
    }

    public static void giveStack(Player player, int idx, ItemStack stack) {
        if(player.getInventory().getItem(idx) == null)
            player.getInventory().setItem(idx, stack);
        else
            Utils.giveItem(player, stack);
    }
}
