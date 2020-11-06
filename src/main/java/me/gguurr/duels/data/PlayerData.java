package me.gguurr.duels.data;

import me.gguurr.duels.data.items.Armor;
import me.gguurr.duels.data.items.Item;
import me.gguurr.duels.data.items.ItemRegister;
import me.gguurr.duels.data.items.Slots;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    public static HashMap<UUID, PlayerData> cashe = new HashMap<>();

    public static final double MAX_HEALTH = 100;
    public static final double MAX_PSI = 100;
    public static final double HEAL_REGEN_AMOUNT = 5.0;
    public static final double REGINE_PSI_REGEN_AMOUNT = 2.5;
    public static final String[] PSI_BAR_COLORS = new String[]{
            "#47D5FF",
            "#59C4FF",
            "#6CB4FF",
            "#7EA4FF",
            "#9194FF",
            "#A384FF",
            "#B674FF",
            "#C963FF",
            "#DB53FF",
            "#EE44FF"
    };


    public long lastDamaged;
    public UUID uuid;
    public double health;
    public double psi;

    public static PlayerData getDataForPlayer(OfflinePlayer p){
        if(cashe.containsKey(p.getUniqueId())){
            return cashe.get(p.getUniqueId());
        }
        return new PlayerData(p.getUniqueId());
    }

    private PlayerData(UUID uuid){
        this.uuid = uuid;
        health = getMaxHealth();
        psi = getMaxPsi();
        cashe.put(uuid, this);
    }

    public void update(){
        Player player = Bukkit.getPlayer(uuid);
        if(player != null && player.isOnline()) {
            double scale = health / getMaxHealth() * 20.0;
            player.setHealthScale(20.0);
            player.setHealth(scale);
            double psiScale = psi / getMaxPsi() * 10.0;
            TextComponent psiBar = new TextComponent("Psi: ");
            psiBar.setColor(ChatColor.of("#c2c2c2"));
            for (int i = 0; i < PSI_BAR_COLORS.length; i++){
                TextComponent nugget = new TextComponent("█");
                if(i <= psiScale){
                    nugget.setColor(ChatColor.of(PSI_BAR_COLORS[i]));
                }else {
                    nugget.setColor(ChatColor.of("#c2c2c2"));
                }
                psiBar.addExtra(nugget);
            }
            player.sendActionBar(psiBar);
        }
    }

    public void regen(){
        long timeDiff = System.currentTimeMillis() - lastDamaged;
        double inSecs = timeDiff / 1000.0;
        if(inSecs >= 5.5) {
            heal(HEAL_REGEN_AMOUNT);
            reginePsi(REGINE_PSI_REGEN_AMOUNT);
        }
        update();
    }

    public void respawn(){
        Player player = Bukkit.getPlayer(uuid);
        if(player != null && player.isOnline()) {
            health = getMaxHealth();
            update();
        }
    }

    public double getMaxHealth(){
        double r = MAX_HEALTH;
        Player player = Bukkit.getPlayer(uuid);
        if(player != null && player.isOnline()) {
            Item ih = ItemRegister.FromItemStack(player.getInventory().getHelmet());
            Item il = ItemRegister.FromItemStack(player.getInventory().getLeggings());
            Item ib = ItemRegister.FromItemStack(player.getInventory().getBoots());
            Item ic = ItemRegister.FromItemStack(player.getInventory().getChestplate());
            Item im = ItemRegister.FromItemStack(player.getInventory().getItemInMainHand());
            Item io = ItemRegister.FromItemStack(player.getInventory().getItemInOffHand());
            if (ih instanceof Armor && ih.slots() == Slots.HEAD) {
                r += ((Armor) ih).getExtraHealth();
            }
            if(il instanceof Armor && il.slots() == Slots.LEGS){
                r += ((Armor) il).getExtraHealth();
            }
            if(ib instanceof Armor && ib.slots() == Slots.FEET){
                r += ((Armor) ib).getExtraHealth();
            }
            if(ic instanceof Armor && ic.slots() == Slots.CHEST){
                r += ((Armor) ic).getExtraHealth();
            }
            if(im instanceof Armor && (im.slots() == Slots.MAIN_HAND || im.slots() == Slots.OFF_HAND || im.slots() == Slots.BOTH_HANDS)){
                r += ((Armor) im).getExtraHealth();
            }
            if(io instanceof Armor && io.slots() == Slots.OFF_HAND){
                r += ((Armor) io).getExtraHealth();
            }
        }
        return r;
    }

    public double getMaxPsi(){
        double r = MAX_PSI;
        Player player = Bukkit.getPlayer(uuid);
        if(player != null && player.isOnline()) {
            Item ih = ItemRegister.FromItemStack(player.getInventory().getHelmet());
            Item il = ItemRegister.FromItemStack(player.getInventory().getLeggings());
            Item ib = ItemRegister.FromItemStack(player.getInventory().getBoots());
            Item ic = ItemRegister.FromItemStack(player.getInventory().getChestplate());
            Item im = ItemRegister.FromItemStack(player.getInventory().getItemInMainHand());
            Item io = ItemRegister.FromItemStack(player.getInventory().getItemInOffHand());
            if (ih instanceof Armor && ih.slots() == Slots.HEAD) {
                r += ((Armor) ih).getExtraPsi();
            }
            if(il instanceof Armor && il.slots() == Slots.LEGS){
                r += ((Armor) il).getExtraPsi();
            }
            if(ib instanceof Armor && ib.slots() == Slots.FEET){
                r += ((Armor) ib).getExtraPsi();
            }
            if(ic instanceof Armor && ic.slots() == Slots.CHEST){
                r += ((Armor) ic).getExtraPsi();
            }
            if(im instanceof Armor && (im.slots() == Slots.MAIN_HAND || im.slots() == Slots.OFF_HAND || im.slots() == Slots.BOTH_HANDS)){
                r += ((Armor) im).getExtraPsi();
            }
            if(io instanceof Armor && io.slots() == Slots.OFF_HAND){
                r += ((Armor) io).getExtraPsi();
            }
        }
        return r;
    }

    public void heal(double d){
        Player player = Bukkit.getPlayer(uuid);
        if(player != null && player.isOnline()) {
            if(health + d <= getMaxHealth()){
                health += d;
            }else {
                health = getMaxHealth();
            }
        }
        update();
    }

    public void reginePsi(double d){
        Player player = Bukkit.getPlayer(uuid);
        if(player != null && player.isOnline()) {
            if(psi + d <= getMaxPsi()){
                psi += d;
            }else {
                psi = getMaxPsi();
            }
        }
        update();
    }

    public void usePsi(double d){
        Player player = Bukkit.getPlayer(uuid);
        if(player != null && player.isOnline()) {
            if(psi - d >= 0){
                psi -= d;
            }else {
                damageTrue(Math.abs(psi - d));
                psi = 0;
            }
        }
        update();
    }

    public void damageTrue(double d){
        Player player = Bukkit.getPlayer(uuid);
        if(player != null && player.isOnline()) {
            lastDamaged = System.currentTimeMillis();
            health -= d;
            if (health <= 0) {
                health = 0;
            }
            player.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR,player.getLocation(),(int) Math.round((d / getMaxHealth()) * 20.0),0.5,0.5,0.5, 0.001);
        }
        update();
    }

    public void damage(double d, boolean isCrit){
        Player player = Bukkit.getPlayer(uuid);
        if(player != null && player.isOnline()) {
            double armor = 0;
            Item ih = ItemRegister.FromItemStack(player.getInventory().getHelmet());
            Item il = ItemRegister.FromItemStack(player.getInventory().getLeggings());
            Item ib = ItemRegister.FromItemStack(player.getInventory().getBoots());
            Item ic = ItemRegister.FromItemStack(player.getInventory().getChestplate());
            Item im = ItemRegister.FromItemStack(player.getInventory().getItemInMainHand());
            Item io = ItemRegister.FromItemStack(player.getInventory().getItemInOffHand());
            if(ih instanceof Armor && ih.slots() == Slots.HEAD){
                armor += ((Armor) ih).getArmor();
            }
            if(il instanceof Armor && il.slots() == Slots.LEGS){
                armor += ((Armor) il).getArmor();
            }
            if(ib instanceof Armor && ib.slots() == Slots.FEET){
                armor += ((Armor) ib).getArmor();
            }
            if(ic instanceof Armor && ic.slots() == Slots.CHEST){
                armor += ((Armor) ic).getArmor();
            }
            if(im instanceof Armor && (im.slots() == Slots.MAIN_HAND || im.slots() == Slots.OFF_HAND || im.slots() == Slots.BOTH_HANDS)){
                armor += ((Armor) im).getArmor();
            }
            if(io instanceof Armor && io.slots() == Slots.OFF_HAND){
                armor += ((Armor) io).getArmor();
            }
            double trueD = Math.max(d - (armor / 5),0.5);
            damageTrue(trueD);
            if(isCrit){
                player.getWorld().spawnParticle(Particle.CRIT,player.getLocation(),15,0.5,0.5,0.5, 0.001);
            }
        }
    }

}
