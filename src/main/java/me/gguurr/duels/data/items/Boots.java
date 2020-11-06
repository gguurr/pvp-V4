package me.gguurr.duels.data.items;

public interface Boots extends Armor {
    @Override
    default Slots slots() {
        return Slots.FEET;
    }
}
