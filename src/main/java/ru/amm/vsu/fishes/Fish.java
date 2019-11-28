package ru.amm.vsu.fishes;

import java.util.Date;

abstract public class Fish {
    abstract public int getPrice();

    abstract public Date getShelfLife();

    public boolean isFresh() {
        return new Date().before(getShelfLife());
    }
}
