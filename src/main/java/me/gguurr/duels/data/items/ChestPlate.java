package me.gguurr.duels.data.items;

public interface ChestPlate extends Armor {
    @Override
    default Slots slots() {
        return Slots.CHEST;
    }
}
