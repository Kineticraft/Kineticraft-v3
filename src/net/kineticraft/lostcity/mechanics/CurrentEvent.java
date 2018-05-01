package net.kineticraft.lostcity.mechanics;

import net.kineticraft.lostcity.Core;
import net.kineticraft.lostcity.config.Configs;
import net.kineticraft.lostcity.mechanics.system.Mechanic;
import net.kineticraft.lostcity.utils.Utils;
import net.minecraft.server.v1_12_R1.*;
import net.minecraft.server.v1_12_R1.Entity;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.*;

public class CurrentEvent extends Mechanic {
    private static final Location QUEUE_ROOM_MIN = new Location(Bukkit.getWorlds().get(0), -19791, 87, 19400);
    private static final Location QUEUE_ROOM_MAX = new Location(Bukkit.getWorlds().get(0), -19777, 102, 19425);
    private static final Location BUILDOFF_QUEUE_ROOM_MIN = new Location(Bukkit.getWorlds().get(0), -19776, 87, 19373);
    private static final Location BUILDOFF_QUEUE_ROOM_MAX = new Location(Bukkit.getWorlds().get(0), -19754, 99, 19390);
    // BQR = Buildoff Queue Room
    private static final Location BQR_PRESSURE_PLATE = new Location(Bukkit.getWorlds().get(0), -19760, 88, 19378);
    private static final Location BMN = new Location(Bukkit.getWorlds().get(0), -19724, 22, 19496); // buildoff room min
    private static final Location BMX = new Location(Bukkit.getWorlds().get(0), -19632, 42, 19552); // buildoff room max
    private static final Location PARKOUR_ROOM_MIN = new Location(Bukkit.getWorlds().get(0), -19841, 28, 19496);
    private static final Location PARKOUR_ROOM_MAX = new Location(Bukkit.getWorlds().get(0), -19762, 43, 19550);

    // static (permanent) methods
    public static boolean isEventActive() {
        return Configs.getMainConfig().isCurrentEventActive(); /** Make sure to update this! **/
    }

    public static boolean toggleEventActive() {
        boolean active = !Configs.getMainConfig().isCurrentEventActive();
        Configs.getMainConfig().setCurrentEventActive(active);
        return active;
    }

    public static Location getEventWarpLocation() {
        return new Location(Bukkit.getWorlds().get(0), -19772.5, 70.1, 19399.5, 270, 0); // start location
    }

    public static String getPartyMessage() {
        return ChatColor.COLOR_CHAR + "3Happy Easter!! " + ChatColor.GOLD + "Make sure to check out the " + ChatColor.DARK_GREEN +
                "Kineticraft Easter Event " + ChatColor.GOLD + "going on now! " + ChatColor.RED + "Get there with /party!";
    }

    public static boolean canPlayerTPOut(Player player) {
        return !isLocInZone(player.getLocation(), QUEUE_ROOM_MIN, QUEUE_ROOM_MAX) && player.getGameMode() == GameMode.SURVIVAL &&
                !isLocInZone(player.getLocation(), BMN, new Location(BMX.getWorld(), BMX.getX(), BMX.getY() + 10, BMX.getZ())) &&
                !isLocInZone(player.getLocation(), PARKOUR_ROOM_MIN, PARKOUR_ROOM_MAX);
    }

    public static boolean canFly(Player player) {
        return isLocInZone(player.getLocation(), BMN, new Location(BMX.getWorld(), BMX.getX(), BMX.getY() + 10, BMX.getZ()));
    }



    // -19547 19604, -19999 19279
    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent evt) {
        Location l = evt.getLocation();
        if(evt.getEntityType() != EntityType.RABBIT && evt.getEntityType() != EntityType.ARMOR_STAND &&
           evt.getEntityType() != EntityType.DROPPED_ITEM && evt.getEntityType() != EntityType.PLAYER &&
           evt.getEntityType() != EntityType.VILLAGER && l.getX() > -19999 && l.getZ() > 19279 && l.getX() < -19547 &&
           l.getZ() < 19604) {
            evt.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPressurePlaceActivate(final PlayerInteractEvent evt) {
        if(evt.getAction() == Action.PHYSICAL && evt.getClickedBlock().getType() == Material.GOLD_PLATE &&
                evt.getPlayer().getGameMode() == GameMode.SURVIVAL && evt.getClickedBlock().getLocation().equals(BQR_PRESSURE_PLATE)) {
            evt.getPlayer().getInventory().clear();
            evt.getPlayer().teleport(new Location(Bukkit.getWorlds().get(0), -19676.5, 24.1, 19524.5));
            NBTTagCompound tag = new NBTTagCompound();
            tag.setByte("Unbreakable", (byte) 1);
            ItemStack pick = new ItemStack(Items.DIAMOND_PICKAXE, 1);
            pick.setTag(tag);
            ItemStack axe = new ItemStack(Items.DIAMOND_AXE, 1);
            axe.setTag(tag);
            ItemStack shovel = new ItemStack(Items.DIAMOND_SHOVEL, 1);
            shovel.setTag(tag);
            ItemStack shears = new ItemStack(Items.SHEARS, 1);
            shears.setTag(tag);
            Utils.giveItems(evt.getPlayer(), CraftItemStack.asBukkitCopy(pick), CraftItemStack.asBukkitCopy(axe),
                    CraftItemStack.asBukkitCopy(shovel), CraftItemStack.asBukkitCopy(shears), new org.bukkit.inventory.ItemStack(Material.SIGN, 1));
        }
    }

    /** Handles the Egg Hunt game **/
    // Dev server coords: (-19762, 19311),(-19587,19494)
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent evt) {
        Location loc = evt.getBlock().getLocation();
        if(loc.getX() > -19762 && loc.getX() < -19587 && loc.getZ() > 19311 && loc.getZ() < 19494 && !Material.BEDROCK.equals(evt.getPlayer().getItemInHand().getType())) {
            NBTTagCompound ntc;
            CraftWorld cw = (CraftWorld)evt.getPlayer().getWorld();
            TileEntity te = cw.getTileEntityAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            if(te != null) {
                ntc = new NBTTagCompound();
                te.save(ntc);
                if("73f9c460-ef07-4bf0-a7c2-c08118937ed6".equals(ntc.getCompound("Owner").getString("Id"))) {
                    evt.setDropItems(false);
                    if(evt.getPlayer().getGameMode() != GameMode.CREATIVE) {
                        NBTTagCompound tag = new NBTTagCompound();
                        NBTTagCompound display = new NBTTagCompound();
                        display.setString("Name", "Easter Egg");
                        NBTTagList lore = new NBTTagList();
                        lore.add(new NBTTagString("From the Easter Egg hunt!"));
                        display.set("Lore", lore);
                        tag.set("display", display);
                        net.minecraft.server.v1_12_R1.ItemStack dropCB = new net.minecraft.server.v1_12_R1.ItemStack(Items.EGG, 1);
                        dropCB.setTag(tag);
                        Utils.giveItem(evt.getPlayer(), CraftItemStack.asBukkitCopy(dropCB));
                        evt.getPlayer().playSound(evt.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 6.0F, 1.0F);
                    }

                    final byte data = evt.getBlock().getData();
                    Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> {
                        loc.getBlock().setType(Material.SKULL);
                        loc.getBlock().setData(data);
                        TileEntity te0 = cw.getTileEntityAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                        if(te0 != null)
                            te0.load(ntc);
                    }, Utils.randInt(2400, 6000));
                }else if("41c8a8bb-e251-430c-8bfe-27fb974cca55".equals(ntc.getCompound("Owner").getString("Id"))) {
                    evt.setDropItems(false);
                    if(evt.getPlayer().getGameMode() != GameMode.CREATIVE) {
                        giveSkull(evt.getPlayer(), "41c8a8bb-e251-430c-8bfe-27fb974cca55", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY3OWVmYzU5ZTIyZmNmMzRmNzQ0OGJmN2FiNjY2NGY3OTljM2RmZjY1NmNmNDgzMDk4YmUzNmM5YWUxIn19fQ==", ChatColor.RESET.toString() + ChatColor.GOLD + "Golden Egg" + ChatColor.RESET, "This egg, which accidentally fell into a", "pot of gold, became indestructible to even", "the strongest hammer.");
                        evt.getPlayer().playSound(evt.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 1.0F);
                        evt.getPlayer().sendMessage(ChatColor.GOLD + "You have picked up the one and only golden egg! Keep it as a rare collectible.");
                    }
                }
            }
        }

        if(loc.getX() > BMN.getX() && loc.getZ() > BMN.getZ() && loc.getX() < BMX.getX() && loc.getZ() < BMX.getZ() &&
                !(loc.getY() > BMN.getY() && loc.getY() < BMX.getY()) && evt.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            evt.setCancelled(true);
            evt.getPlayer().sendMessage(ChatColor.RED + "You cannot break that block.");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent evt) {
        Location loc = evt.getBlockReplacedState().getLocation();
        if(loc.getX() > BMN.getX() && loc.getZ() > BMN.getZ() && loc.getX() < BMX.getX() && loc.getZ() < BMX.getZ() &&
                !(loc.getY() > BMN.getY() && loc.getY() < BMX.getY()) && evt.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            evt.setCancelled(true);
            evt.getPlayer().sendMessage(ChatColor.RED + "You cannot place that block there.");
        }
    }

    public static void giveSkull(Player player, String ownerID, String texturs, String name, String... lores) {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound display = new NBTTagCompound();
        display.setString("Name", name);
        NBTTagList lore = new NBTTagList();
        for(String ls : lores)
            lore.add(new NBTTagString(ls));
        display.set("Lore", lore);
        tag.set("display", display);
        NBTTagCompound skullOwner = new NBTTagCompound();
        skullOwner.setString("Id", ownerID);
        NBTTagCompound properties = new NBTTagCompound();
        NBTTagList textures = new NBTTagList();
        NBTTagCompound textures_0 = new NBTTagCompound();
        textures_0.setString("Value", texturs);
        textures.add(textures_0);
        properties.set("textures", textures);
        skullOwner.set("Properties", properties);
        tag.set("SkullOwner", skullOwner);
        net.minecraft.server.v1_12_R1.ItemStack dropCB = new net.minecraft.server.v1_12_R1.ItemStack(Items.SKULL, 1, 3);
        dropCB.setTag(tag);
        Utils.giveItem(player, CraftItemStack.asBukkitCopy(dropCB));
    }

    /** Parkour Code **/
    // From: -19838 30 19497, To: -19764 54 19550
    @EventHandler(ignoreCancelled = true)
    public void onEntityHurt(EntityDamageByEntityEvent evt) {
        if(evt.getEntity().getType() != EntityType.PLAYER)
            return;
        Location l = evt.getEntity().getLocation();
        if(l.getX() > -19838 && l.getY() >= 30 && l.getZ() > 19497 && l.getX() < -19764 && l.getY() < 54 && l.getZ() < 19550 &&
           ((Player)evt.getEntity()).getHealth() - evt.getDamage() < 1.0) {
            evt.setCancelled(true);
            evt.getEntity().teleport(new Location(Bukkit.getWorlds().get(0), -19839, 39, 19541, 270, 0));
            ((Player)evt.getEntity()).setHealth(20.0);
        }
    }

    /** Lost rabbit code **/
    private static final LostBunnyGame lostBunnyGame = new LostBunnyGame();

    // Starting room: -19791 87 19400 to -19777 102 19425
    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent evt) {
        if(isLocInZone(evt.getFrom(), QUEUE_ROOM_MIN, QUEUE_ROOM_MAX) && !isLocInZone(evt.getTo(), QUEUE_ROOM_MIN, QUEUE_ROOM_MAX))
            lostBunnyGame.removeFromQueue(evt.getPlayer());
        else if(!isLocInZone(evt.getFrom(), BUILDOFF_QUEUE_ROOM_MIN, BUILDOFF_QUEUE_ROOM_MAX) && isLocInZone(evt.getTo(), BUILDOFF_QUEUE_ROOM_MIN, BUILDOFF_QUEUE_ROOM_MAX) &&
                evt.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            for(org.bukkit.inventory.ItemStack stack : evt.getPlayer().getInventory().getContents()) {
                if(stack != null) {
                    evt.setCancelled(true);
                    evt.getPlayer().sendMessage(ChatColor.RED + "To enter this event, your inventory must be empty, as your inventory is cleared when you enter and exit the event.");
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityInteract(PlayerInteractEntityEvent evt) {
        if(evt.getHand().equals(EquipmentSlot.OFF_HAND))
            return;
        if(evt.getRightClicked().getType() == EntityType.RABBIT) {
            if(isLocInZone(evt.getRightClicked().getLocation(), QUEUE_ROOM_MIN, QUEUE_ROOM_MAX)) // Thumper
                lostBunnyGame.addToQueue(evt.getPlayer());
            else if(lostBunnyGame.active.containsKey(evt.getPlayer()))
                lostBunnyGame.onRabbitCollected((Rabbit)evt.getRightClicked(), evt.getPlayer());
        }
    }

    @Override
    public void onQuit(Player player) {
        if(lostBunnyGame.active.containsKey(player)) {
            lostBunnyGame.active.remove(player);
            net.minecraft.server.v1_12_R1.EntityPlayer handle = ((CraftPlayer) player).getHandle();
            handle.dimension = 0;
            handle.world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
            handle.setPositionRotation(LostBunnyGame.ENTRANCE.getX(), LostBunnyGame.ENTRANCE.getY(),
                    LostBunnyGame.ENTRANCE.getZ(), LostBunnyGame.ENTRANCE.getYaw(), LostBunnyGame.ENTRANCE.getPitch());
        }else if(lostBunnyGame.queue.contains(player)) {
            lostBunnyGame.queue.remove(player);
            lostBunnyGame.updateQueue();
        }else if(isLocInZone(player.getLocation(), BMN, new Location(BMX.getWorld(), BMX.getX(), BMX.getY() + 10, BMX.getZ())) &&
                player.getGameMode() == GameMode.SURVIVAL) {
            net.minecraft.server.v1_12_R1.EntityPlayer handle = ((CraftPlayer) player).getHandle();
            handle.dimension = 0;
            handle.world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
            handle.setPositionRotation(getEventWarpLocation().getX(), getEventWarpLocation().getY(),
                    getEventWarpLocation().getZ(), getEventWarpLocation().getYaw(), getEventWarpLocation().getPitch());
            handle.inventory.clear();
        }
    }

    /*@Override
    public void onJoin(Player player) {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound display = new NBTTagCompound();
        String name = "҉҉҉҉ Easter Basket ҉҉҉҉";
        String fname = "";
        for(int i = 0;i < name.length();++ i)
            fname += (i % 2 == 0 ? ChatColor.LIGHT_PURPLE.toString() : ChatColor.DARK_GREEN.toString()) + name.charAt(i);
        display.setString("Name", fname);
        tag.set("display", display);
        net.minecraft.server.v1_12_R1.ItemStack dropCB = new net.minecraft.server.v1_12_R1.ItemStack(Blocks.WHITE_SHULKER_BOX, 1);
        dropCB.setTag(tag);
        Utils.giveItem(player, CraftItemStack.asBukkitCopy(dropCB));
    }*/

    private static boolean isLocInZone(Location l, Location m, Location x) {
        return l.getX() > m.getX() && l.getY() > m.getY() && l.getZ() > m.getZ() && l.getX() < x.getX() && l.getY() < x.getY() && l.getZ() < x.getZ();
    }

    private static final class LostBunnyGame {
        private final HashMap<Player, Integer> active;
        private final List<Player> queue;
        private final List<Rabbit> rabbits;
        private int timer;
        private int mode; // 0: queueing, 1: starting, 2: running, 3: ending

        private static final List<Location> BUNNY_SPAWNS = Arrays.asList(
                new Location(Bukkit.getWorlds().get(0), -19880, 23, 19381), new Location(Bukkit.getWorlds().get(0), -19875, 22, 19380),
                new Location(Bukkit.getWorlds().get(0), -19883, 25, 19391), new Location(Bukkit.getWorlds().get(0), -19887, 21, 19391),
                new Location(Bukkit.getWorlds().get(0), -19855, 24, 19379), new Location(Bukkit.getWorlds().get(0), -19850, 21, 19391),
                new Location(Bukkit.getWorlds().get(0), -19859, 21, 19391), new Location(Bukkit.getWorlds().get(0), -19858, 21, 19411),
                new Location(Bukkit.getWorlds().get(0), -19877, 26, 19412), new Location(Bukkit.getWorlds().get(0), -19858, 20, 19449),
                new Location(Bukkit.getWorlds().get(0), -19863, 18, 19433), new Location(Bukkit.getWorlds().get(0), -19881, 18, 19434),
                new Location(Bukkit.getWorlds().get(0), -19858, 18, 19434), new Location(Bukkit.getWorlds().get(0), -19858, 39, 19407),
                new Location(Bukkit.getWorlds().get(0), -19871, 39, 19394), new Location(Bukkit.getWorlds().get(0), -19871, 39, 19387),
                new Location(Bukkit.getWorlds().get(0), -19871, 39, 19382), new Location(Bukkit.getWorlds().get(0), -19871, 41, 19379),
                new Location(Bukkit.getWorlds().get(0), -19861, 42, 19379), new Location(Bukkit.getWorlds().get(0), -19852, 42, 19379),
                new Location(Bukkit.getWorlds().get(0), -19851, 42, 19390), new Location(Bukkit.getWorlds().get(0), -19852, 39, 19392),
                new Location(Bukkit.getWorlds().get(0), -19858, 39, 19390), new Location(Bukkit.getWorlds().get(0), -19871, 43, 19384),
                new Location(Bukkit.getWorlds().get(0), -19874, 39, 19403), new Location(Bukkit.getWorlds().get(0), -19885, 39, 19402),
                new Location(Bukkit.getWorlds().get(0), -19886, 39, 19395), new Location(Bukkit.getWorlds().get(0), -19887, 42, 19400),
                new Location(Bukkit.getWorlds().get(0), -19886, 46, 19396), new Location(Bukkit.getWorlds().get(0), -19887, 43, 19379),
                new Location(Bukkit.getWorlds().get(0), -19874, 42, 19379), new Location(Bukkit.getWorlds().get(0), -19875, 44, 19387),
                new Location(Bukkit.getWorlds().get(0), -19874, 39, 19384), new Location(Bukkit.getWorlds().get(0), -19875, 39, 19429),
                new Location(Bukkit.getWorlds().get(0), -19887, 39, 19429), new Location(Bukkit.getWorlds().get(0), -19887, 39, 19411),
                new Location(Bukkit.getWorlds().get(0), -19882, 39, 19406), new Location(Bukkit.getWorlds().get(0), -19880, 39.5, 19408),
                new Location(Bukkit.getWorlds().get(0), -19875, 39.5, 19407), new Location(Bukkit.getWorlds().get(0), -19865, 39, 19429),
                new Location(Bukkit.getWorlds().get(0), -19885, 21, 19427), new Location(Bukkit.getWorlds().get(0), -19883, 24, 19428),
                new Location(Bukkit.getWorlds().get(0), -19883, 21, 19429), new Location(Bukkit.getWorlds().get(0), -19885, 25, 19425),
                new Location(Bukkit.getWorlds().get(0), -19887, 21, 19416), new Location(Bukkit.getWorlds().get(0), -19883, 23, 19416),
                new Location(Bukkit.getWorlds().get(0), -19884, 26, 19416), new Location(Bukkit.getWorlds().get(0), -19884, 29, 19416),
                new Location(Bukkit.getWorlds().get(0), -19878, 30, 19416), new Location(Bukkit.getWorlds().get(0), -19884, 18, 19450),
                new Location(Bukkit.getWorlds().get(0), -19866, 21, 19432), new Location(Bukkit.getWorlds().get(0), -19856, 21, 19386),
                new Location(Bukkit.getWorlds().get(0), -19853, 23, 19429), new Location(Bukkit.getWorlds().get(0), -19884, 21, 19416),
                new Location(Bukkit.getWorlds().get(0), -19851, 21, 19406)
        );
        private static final Location START_LOCATION = new Location(Bukkit.getWorlds().get(0), -19886, 21.2, 19404, 270, 0);
        private static final Location ENTRANCE = new Location(Bukkit.getWorlds().get(0), -19783, 88.2, 19412, 0, 0);
        private static final int REQUIRED_PLAYER_AMT = 4;
        private static final int GAME_TIMER = 2400;
        private static final int START_TIMER = 100;
        private static final int END_TIMER = 100;

        public LostBunnyGame() {
            this.active = new HashMap<>(4);
            this.queue = new ArrayList<>();
            this.rabbits = new ArrayList<>();
            this.timer = 0;
            this.mode = 0;

            Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), () -> onServerTick(), 0, 1);
        }

        public void addToQueue(Player player) {
            if(!queue.contains(player)) {
                queue.add(player);
                player.sendMessage(ChatColor.GOLD + "You have been added to the queue. If you wish to leave the queue, exit the room.");
                if(mode != 0) {
                    int gamesAhead = (queue.size() - 1) / REQUIRED_PLAYER_AMT;
                    if(gamesAhead == 0)
                        player.sendMessage(ChatColor.GOLD + "Another game is currently in progress, you are in the next game.");
                    else
                        player.sendMessage(ChatColor.GOLD + "Another game is currently in progress, there " + (gamesAhead > 1 ? "are " : "is ") + Integer.toString(gamesAhead) + " game" + (gamesAhead > 1 ? "s" : "") + " ahead of you.");
                }
            }else{
                player.sendMessage(ChatColor.RED + "You cannot re-enter the queue. If you wish to leave the queue, exit the room.");
                return;
            }
            updateQueue();
        }

        public void removeFromQueue(Player player) {
            if(queue.remove(player)) {
                player.sendMessage(ChatColor.BLUE + "You have left the queue.");
                updateQueue();
            }
        }

        public void onRabbitCollected(Rabbit rabbit, Player player) {
            active.put(player, active.get(player) + 1);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0F, 1.0F);
            rabbits.remove(rabbit);
            rabbit.remove();
        }

        private void updateQueue() {
            if(mode == 0) {
                if(queue.size() >= REQUIRED_PLAYER_AMT) {
                    for(int i = 0;i < REQUIRED_PLAYER_AMT;++ i)
                        active.put(queue.remove(0), 0);

                    timer = START_TIMER;
                    mode = 1;
                }else{
                    int left = REQUIRED_PLAYER_AMT - queue.size();
                    queue.forEach(p -> p.sendMessage(ChatColor.GOLD + Integer.toString(left) + " more player" + (left > 1 ? "s" : "") + " must join for the game to start."));
                }
            }
        }

        private void onServerTick() {
            switch(mode) {
                case 1:
                {
                    if(timer == START_TIMER) {
                        Random rng = new Random();
                        List<Location> spawns = new ArrayList<>(BUNNY_SPAWNS);
                        for(int i = 0;i < 14;++ i) {
                            Location l = spawns.remove(rng.nextInt(spawns.size()));
                            org.bukkit.entity.Entity ent = Bukkit.getWorlds().get(0).spawnEntity(new Location(l.getWorld(), l.getX() + 0.5, l.getY(), l.getZ() + 0.5), EntityType.RABBIT);
                            Entity rabbit = ((CraftEntity)ent).getHandle();
                            NBTTagCompound nbt = new NBTTagCompound();
                            rabbit.c(nbt);
                            nbt.setByte("Silent", (byte)1);
                            nbt.setByte("NoAI", (byte)1);
                            nbt.setInt("RabbitType", rng.nextInt(6));
                            nbt.setByte("Invulnerable", (byte)1);
                            NBTTagList rotation = new NBTTagList();
                            rotation.add(new NBTTagFloat(rng.nextFloat() * 360.0F));
                            nbt.set("Rotation", rotation);
                            rabbit.f(nbt);
                            rabbits.add((Rabbit)ent);
                        }
                    }
                    if(timer % 20 == 0) {
                        active.keySet().forEach(p -> {
                            p.sendMessage(ChatColor.GOLD + "Game starting in " + Integer.toString(timer / 20) + "...");
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 10.0F, 1.0F);
                        });
                    }
                    -- timer;
                    if(timer == 0) {
                        active.keySet().forEach(p -> {
                            p.sendMessage(ChatColor.GOLD + "The game has started! You have 2 minutes to collect (right-click) as many bunnies as you can!");
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 1.0F);
                            p.teleport(START_LOCATION);
                        });
                        timer = GAME_TIMER;
                        mode = 2;
                    }
                    break;
                }
                case 2:
                {
                    if(timer == 600) {
                        active.keySet().forEach(p -> {
                            p.sendMessage(ChatColor.GOLD + "30 seconds remaining.");
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 10.0F, 1.0F);
                        });
                    }else if(timer <= START_TIMER && timer > 0) {
                        if(timer % 20 == 0) {
                            active.keySet().forEach(p -> {
                                p.sendMessage(ChatColor.GOLD + "Game ends in " + Integer.toString(timer / 20) + "...");
                                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 10.0F, 1.0F);
                            });
                        }
                    }
                    -- timer;
                    if(timer == 0 || rabbits.isEmpty() || active.isEmpty()) {
                        active.forEach((p, n) -> {
                            if(n > 1) {
                                int c = n / 2; // cookies received
                                p.sendMessage(ChatColor.GREEN + "You have received " + c + " cookie" + (c > 1 ? "s" : "") + " for helping Matilda find her bunnies!");
                                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 1.0F);
                                NBTTagCompound tag = new NBTTagCompound();
                                NBTTagCompound display = new NBTTagCompound();
                                display.setString("Name", "Matilda's Homemade Cookie");
                                NBTTagList lore = new NBTTagList();
                                lore.add(new NBTTagString("Made with a secret recipe with magical items."));
                                display.set("Lore", lore);
                                tag.set("display", display);
                                net.minecraft.server.v1_12_R1.ItemStack dropCB = new net.minecraft.server.v1_12_R1.ItemStack(Items.COOKIE, c);
                                dropCB.setTag(tag);
                                Utils.giveItem(p, CraftItemStack.asBukkitCopy(dropCB));
                            }else {
                                p.sendMessage(ChatColor.RED + "You did not find enough bunnies to receive a cookie :(");
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10.0F, 1.0F);
                            }
                        });
                        queue.forEach(p -> {
                            p.sendMessage(ChatColor.GOLD + "The current game has finished. The arena will reset shortly.");
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 10.0F, 1.0F);
                        });
                        rabbits.forEach(r -> r.remove());
                        rabbits.clear();
                        timer = END_TIMER;
                        mode = 3;
                    }
                    break;
                }
                case 3:
                {
                    -- timer;
                    if(timer == 0) {
                        mode = 0;
                        active.keySet().forEach(p -> p.teleport(ENTRANCE));
                        active.clear();
                        updateQueue();
                    }
                    break;
                }
                default: break;
            }
        }
    }
}
