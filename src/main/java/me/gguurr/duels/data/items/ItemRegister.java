package me.gguurr.duels.data.items;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemRegister {
    static Map<String,Item> items = new HashMap<>();

    public static void register(String key,Item item){
        items.put(key,item);
    }

    public static Item get(String key){
        return items.get(key);
    }

    public static List<Item> getBySlotAndSet(Slots slot,String set){
        List<Item> r = new ArrayList<>();
        for (Map.Entry<String,Item> re : items.entrySet()) {
            String setName = re.getKey().split(":")[0];
            if (re.getValue().slots() == slot && setName.equals(set)) {
                r.add(re.getValue());
            }
        }
        return  r;
    }

    public static Item FromItemStack(ItemStack itemStack){
        for (Item re : items.values()) {
            if (re.toBukkitItemStack().equals(itemStack)) {
                return re;
            }
        }
        return null;
    }
}
