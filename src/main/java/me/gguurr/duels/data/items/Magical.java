package me.gguurr.duels.data.items;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface Magical extends Item {
    ItemStack cast(PlayerInteractEvent event);
}
