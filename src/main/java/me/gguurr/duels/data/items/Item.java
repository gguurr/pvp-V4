package me.gguurr.duels.data.items;

import org.bukkit.inventory.ItemStack;

public interface Item {
    int cost();
    ItemStack toBukkitItemStack();
    Slots slots();
}