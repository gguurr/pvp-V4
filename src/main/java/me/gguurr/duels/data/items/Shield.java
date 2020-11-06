package me.gguurr.duels.data.items;

public interface Shield extends Armor{
    @Override
    default Slots slots() {
        return Slots.OFF_HAND;
    }
}
