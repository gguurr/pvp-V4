package me.gguurr.duels.cmds;

import me.gguurr.duels.data.items.Item;
import me.gguurr.duels.data.items.ItemRegister;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
        }
        return false;
    }
}
