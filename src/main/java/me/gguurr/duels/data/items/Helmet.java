package me.gguurr.duels.data.items;

public interface Helmet extends Armor {
    @Override
    default Slots slots() {
        return Slots.HEAD;
    }
}
