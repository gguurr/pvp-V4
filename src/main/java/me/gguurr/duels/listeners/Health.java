package me.gguurr.duels.listeners;

import me.gguurr.duels.data.PlayerData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Health implements Listener {
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        e.setFoodLevel(20);
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent e) {
        e.setCancelled(true);
    }
    @EventHandler
    public void onMobSpawn(EntitySpawnEvent e){
        if (e.getEntity() instanceof Item) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity().getType() == EntityType.PLAYER) {
            e.setCancelled(true);
            e.setDamage(0.0);
            Player d = (Player) e.getEntity();
            PlayerData data = PlayerData.getDataForPlayer(d);
            data.update();
        }
    }
}
