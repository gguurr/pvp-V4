package me.gguurr.duels.data.items;

public interface Leggings extends Armor {
    @Override
    default Slots slots() {
        return Slots.LEGS;
    }
}
