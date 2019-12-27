package ru.amm.vsu.fishes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Herring extends Fish {
    public static final int PRICE = 30;
    private static final int DAYS_COUNT = 4;

    private Date shelfLife;

    public Herring() {
        shelfLife = Date.from(LocalDateTime.now().plusDays(DAYS_COUNT)
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    @Override
    public int getPrice() {
        return PRICE;
    }

    @Override
    public Date getShelfLife() {
        return shelfLife;
    }
}
