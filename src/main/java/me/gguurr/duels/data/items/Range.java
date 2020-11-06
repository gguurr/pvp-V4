package me.gguurr.duels.data.items;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface Range extends Weapon {
    ItemStack onClick(PlayerInteractEvent event);
}
