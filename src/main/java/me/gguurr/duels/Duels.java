package me.gguurr.duels;

import me.gguurr.duels.cmds.TestCMD;
import me.gguurr.duels.data.PlayerData;
import me.gguurr.duels.data.items.ItemRegister;
import me.gguurr.duels.items.Chainsaw;
import me.gguurr.duels.items.EnergySword;
import me.gguurr.duels.items.TacticalKnife;
import me.gguurr.duels.listeners.Combat;
import me.gguurr.duels.listeners.Health;
import me.gguurr.duels.listeners.Magic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Duels extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        Bukkit.getPluginManager().registerEvents(new Health(), this);
        Bukkit.getPluginManager().registerEvents(new Combat(), this);
        Bukkit.getPluginManager().registerEvents(new Magic(), this);

        ItemRegister.register("space:chainsaw", new Chainsaw());
        ItemRegister.register("space:energy_sword", new EnergySword());
        ItemRegister.register("space:tactical_knife", new TacticalKnife());

        getCommand("test").setExecutor(new TestCMD());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerData pd = PlayerData.getDataForPlayer(player);
                    pd.regen();
                }
            }
        },0,5);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
