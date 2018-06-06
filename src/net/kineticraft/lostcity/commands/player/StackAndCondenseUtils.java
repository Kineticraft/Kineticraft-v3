package net.kineticraft.lostcity.commands.player;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

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

    public static int getOrganizationLvl(Material material) {
        if(material.isEdible())
            return 0;
        int idx = ORGANIZER.indexOf(material);
        return idx == -1 ? 1 : -((ORGANIZER.size() + 1) - idx);
    }

    public static int compareOrganizationLevels(ItemStack a, ItemStack b) {
        return getOrganizationLvl(a.getType()) - getOrganizationLvl(b.getType());
    }
}
