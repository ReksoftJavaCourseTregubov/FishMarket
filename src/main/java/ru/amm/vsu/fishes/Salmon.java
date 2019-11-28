package ru.amm.vsu.fishes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Salmon extends Fish {
    private static final int DAYS_COUNT = 3;

    private Date shelfLife;

    public Salmon() {
        shelfLife = Date.from(LocalDateTime.now().plusDays(DAYS_COUNT)
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    @Override
    public int getPrice() {
        return 100;
    }

    @Override
    public Date getShelfLife() {
        return shelfLife;
    }
}
