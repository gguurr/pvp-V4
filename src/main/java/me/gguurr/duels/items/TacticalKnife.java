package me.gguurr.duels.items;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import me.gguurr.duels.data.items.Melee;
import me.gguurr.duels.data.items.Slots;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TacticalKnife implements Melee {
    @Override
    public double getDamage() {
        return 20.0;
    }

    @Override
    public double getRange() {
        return 3.0;
    }

    @Override
    public double getSpeed() {
        return 0.5;
    }

    @Override
    public int cost() {
        return 0;
    }

    @Override
    public ItemStack toBukkitItemStack() {
        ItemStack is = new ItemStack(Material.STONE_SWORD);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.RESET + "Tactical Knife");
        im.setUnbreakable(true);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        is.setItemMeta(im);
        NBTItem nbti = new NBTItem(is);
        NBTCompoundList attribute = nbti.getCompoundList("AttributeModifiers");
        NBTListCompound mod1 = attribute.addCompound();
        mod1.setInteger("Amount", Integer.MAX_VALUE);
        mod1.setString("AttributeName", "generic.attackSpeed");
        mod1.setString("Name", "generic.attackSpeed");
        mod1.setInteger("Operation", 0);
        mod1.setInteger("UUIDLeast", 59664);
        mod1.setInteger("UUIDMost", 31453);
        is = nbti.getItem();
        return is;
    }

    @Override
    public Slots slots() {
        return Slots.OFF_HAND;
    }
}
