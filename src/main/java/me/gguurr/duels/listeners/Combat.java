package me.gguurr.duels.listeners;

import me.gguurr.duels.data.PlayerData;
import me.gguurr.duels.data.items.*;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class Combat implements Listener {


    private static HashMap<UUID, Long> lastAttack = new HashMap<>();

    @EventHandler
    public void heldItemChange(PlayerItemHeldEvent event){
        ItemStack main = event.getPlayer().getInventory().getItem(event.getNewSlot());
        Item i = ItemRegister.FromItemStack(main);
        if(i != null){
            if(i.slots() == Slots.BOTH_HANDS && event.getPlayer().getInventory().getItemInOffHand().getType() != Material.AIR)
            {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack main = event.getWhoClicked().getInventory().getItemInMainHand();
        Item im = ItemRegister.FromItemStack(main);
        if(im != null) {
            if (event.getSlotType() == InventoryType.SlotType.QUICKBAR && (event.getSlot() == 40 || event.getSlot() < 9) &&
                    (event.getAction() == InventoryAction.PLACE_ALL ||
                            event.getAction() == InventoryAction.PLACE_ONE ||
                            event.getAction() == InventoryAction.PLACE_SOME ||
                            event.getAction() == InventoryAction.SWAP_WITH_CURSOR ||
                            event.getAction() == InventoryAction.HOTBAR_SWAP)) {
                ItemStack off = event.getCursor();
                Item io = ItemRegister.FromItemStack(off);
                if(io != null) {
                    if ((im.slots() == Slots.BOTH_HANDS || io.slots() != Slots.OFF_HAND) && event.getSlot() == 40){
                        event.setCancelled(true);
                    }
                    if (io.slots() == Slots.BOTH_HANDS && event.getSlot() < 9) {
                        if (!(event.getWhoClicked().getInventory().getItemInOffHand().getType() == Material.AIR)) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        ItemStack main = event.getOffHandItem();
        Item im = ItemRegister.FromItemStack(main);
        if(im != null) {
            if (im.slots() != Slots.OFF_HAND) {event.setCancelled(true);}
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        PlayerData data = PlayerData.getDataForPlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        PlayerData data = PlayerData.getDataForPlayer(e.getPlayer());
    }

    @EventHandler()
    public void onPlayerAttack(PlayerInteractEvent event) {
        Player attacker = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            ItemStack main = attacker.getInventory().getItemInMainHand();
            ItemStack off = attacker.getInventory().getItemInOffHand();
            Item im = ItemRegister.FromItemStack(main);
            Item io = ItemRegister.FromItemStack(off);
            double damage = 0;
            Entity damagedEntity;
            double speed = 0;
            double range = 0;
            boolean isCrit = false;
            if(im instanceof Melee) {
                Melee w = ((Melee)im);
                damage = w.getDamage();
                speed = w.getSpeed();
                range = w.getRange();
            }
            if(io instanceof Melee){
                Melee w = ((Melee)io);
                damage += w.getDamage();
                speed = Math.max(w.getSpeed(),speed);
                range =  Math.max(w.getRange(),range);
            }
            damagedEntity = getFirst(attacker.getEyeLocation(),0.1,range,EntityType.PLAYER,attacker);
            if(damagedEntity instanceof Player){
                Player damaged = (Player) damagedEntity;
                if  (!damaged.getUniqueId().equals(attacker.getUniqueId()) && damaged.getGameMode() == GameMode.SURVIVAL &&
                    ((lastAttack.containsKey(attacker.getUniqueId()) && System.currentTimeMillis() - lastAttack.get(attacker.getUniqueId()) > speed * 1000) ||
                    !lastAttack.containsKey(attacker.getUniqueId()))){
                    PlayerData data = PlayerData.getDataForPlayer(damaged);

                    Vector v = genVec(attacker.getLocation(), damaged.getLocation());
                    if (attacker.getVelocity().getY() < 0) {
                        v.multiply(0.85);
                        damage *= 1.5;
                        isCrit = true;
                    }

                    data.damage(damage, isCrit);
                    damaged.setVelocity(v.multiply(0.5));
                    lastAttack.put(attacker.getUniqueId(), System.currentTimeMillis());
                }

            }
        }else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack main = attacker.getInventory().getItemInMainHand();
            ItemStack off = attacker.getInventory().getItemInOffHand();
            Item im = ItemRegister.FromItemStack(main);
            Item io = ItemRegister.FromItemStack(off);
            if(im instanceof Range) {
                attacker.getInventory().setItemInMainHand(((Range)im).onClick(event));
            }
            if(io instanceof Range) {
                attacker.getInventory().setItemInOffHand(((Range)io).onClick(event));
            }
            if(im instanceof Magical) {
                attacker.getInventory().setItemInMainHand(((Magical)im).cast(event));
            }
            if(io instanceof Magical) {
                attacker.getInventory().setItemInOffHand(((Magical)io).cast(event));
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        PlayerData data = PlayerData.getDataForPlayer(event.getPlayer());
        data.respawn();
    }



    public static Entity getFirst(Location loc, double acc, double distace, EntityType et, Entity caster){
        Location c = loc.clone();
        Vector dir = loc.getDirection();
        for(double i = 0; loc.distance(c) < distace; i += acc){
            c.add(dir.clone().multiply(i));
            for(Entity en : loc.getWorld().getNearbyEntities(c, acc, acc, acc)){
                if(en.getType() == et && !en.getUniqueId().equals(caster.getUniqueId())){
                    return en;
                }
            }
            if(c.getBlock().getType().isSolid()){
                break;
            }
        }
        return null;
    }

    public static Vector genVec(Location a, Location b) {
        double dX = a.getX() - b.getX();
        double dY = a.getY() - b.getY();
        double dZ = a.getZ() - b.getZ();
        double yaw = Math.atan2(dZ, dX);
        double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
        double x = Math.sin(pitch) * Math.cos(yaw);
        double y = Math.sin(pitch) * Math.sin(yaw);
        double z = Math.cos(pitch);

        Vector vector = new Vector(x, z, y);
        vector = vector.normalize();

        return vector;
    }
}
