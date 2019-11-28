package ru.amm.vsu;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.amm.vsu.fishes.Fish;
import ru.amm.vsu.fishes.Herring;
import ru.amm.vsu.fishes.Roach;
import ru.amm.vsu.fishes.Salmon;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FishStoreTest {
    private static Store poorFisherman;
    private static Store luckyFisher;
    private static Store fishParadise;

    @BeforeAll
    public static void setup() {
        poorFisherman = new Store("Poor Fisherman", 100);
        luckyFisher = new Store("Lucky Fisher", 100000);
        fishParadise = new Store("Fish Paradise", 150000);

        final int SALMON_COUNT = 10;
        final int ROACH_COUNT = 15;
        final int HERRING_COUNT = 20;

        Fish salmon = new Salmon();
        Box boxOfSalmons = new Box(salmon, SALMON_COUNT);

        luckyFisher.buy(boxOfSalmons, new Box(new Roach(), ROACH_COUNT));
        fishParadise.buy(new Box(new Herring(), HERRING_COUNT));
        fishParadise.buy(new Box(new Herring(), HERRING_COUNT));
    }

    @Test
    public void buyFishMoneyDeduct() {
        final int SALMON_COUNT = 10;
        final int ROACH_COUNT = 15;

        int moneyBeforeDeal = luckyFisher.getMoney();
        int costs = new Salmon().getPrice() * SALMON_COUNT + new Roach().getPrice() * ROACH_COUNT;

        luckyFisher.buy(new Box(new Salmon(), SALMON_COUNT), new Box(new Roach(), ROACH_COUNT));
        assertEquals(moneyBeforeDeal - costs, luckyFisher.getMoney(), "Costs should be deducted");
    }

    @Test
    public void buyFishPutInTheShelf() {
        final int SALMON_COUNT = 10;
        final int ROACH_COUNT = 15;

        int sizeBefore = luckyFisher.getShelf().size();

        luckyFisher.buy(new Box(new Salmon(), SALMON_COUNT), new Box(new Roach(), ROACH_COUNT));
        assertEquals(sizeBefore + 2, luckyFisher.getShelf().size(), "Boxes should be in the shelf");
    }

    @Test
    public void buyFishReturnCosts() {
        final int SALMON_COUNT = 10;
        final int ROACH_COUNT = 15;

        int costs = new Salmon().getPrice() * SALMON_COUNT + new Roach().getPrice() * ROACH_COUNT;

        luckyFisher.buy(new Box(new Salmon(), SALMON_COUNT), new Box(new Roach(), ROACH_COUNT));
        assertEquals(costs, luckyFisher.buy(new Box(new Salmon(), SALMON_COUNT), new Box(new Roach(), ROACH_COUNT)), "Costs should be returned");
    }

    @Test
    public void buyFishMissingMoney() {
        try {
            poorFisherman.buy(new Box(new Salmon(), 10));
        } catch (IllegalArgumentException e) {
            return;
        }
        throw new AssertionError("Poor Fisherman have no many for the deal");
    }

    @Test
    public void sellFishMoneyIncrease() {
        final int SALMON_COUNT = 5;

        int moneyBeforeDeal = luckyFisher.getMoney();
        int price = new Salmon().getPrice() * SALMON_COUNT;

        luckyFisher.sell(Salmon.class, SALMON_COUNT);
        assertEquals(moneyBeforeDeal + price, luckyFisher.getMoney(), "Money should be paid");
    }

    @Test
    public void sellFishRemoveFromTheShelf() {
        final int SALMON_COUNT = 5;
        luckyFisher.buy(new Box(new Salmon(), SALMON_COUNT), new Box(new Salmon(), SALMON_COUNT));

        int sizeBefore = luckyFisher.getShelf().size();
        Box firstSalmonBox = luckyFisher.getShelf().stream()
                .filter(b -> b.getFish().getClass().equals(Salmon.class))
                .findFirst()
                .get();

        luckyFisher.sell(Salmon.class, firstSalmonBox.getCount() - 1);
        assertEquals(sizeBefore, luckyFisher.getShelf().size(), "Shelf size should not change");
        assertEquals(1, firstSalmonBox.getCount(), "There should be one fish in the first salmon box");

        sizeBefore = luckyFisher.getShelf().size();
        firstSalmonBox = luckyFisher.getShelf().stream()
                .filter(b -> b.getFish().getClass().equals(Salmon.class))
                .findFirst()
                .get();

        luckyFisher.sell(Salmon.class, firstSalmonBox.getCount());
        assertEquals(sizeBefore - 1, luckyFisher.getShelf().size(), "Shelf size decrease by 1");
    }

    @Test
    public void sellFishReturnBoxWithCorrectFish() {
        final int SALMON_COUNT = 5;

        luckyFisher.buy(new Box(new Salmon(), SALMON_COUNT));
        Box salmonForDinner = luckyFisher.sell(Salmon.class, SALMON_COUNT);

        assertEquals(salmonForDinner.getClass(), Box.class, "Should return Box");
        assertEquals(salmonForDinner.getFish().getClass(), Salmon.class, "Should return Box of Salmon");
    }

    @Test
    public void sellFishNoAmountOfFish() {
        try {
            luckyFisher.sell(Herring.class, 10);
        } catch (IllegalArgumentException e) {
            return;
        }
        throw new AssertionError("lucky Fisherman have no herring");
    }

    @Test
    public void sellFishNoTypeOfFish() {
        try {
            luckyFisher.sell(Salmon.class, 100);
        } catch (IllegalArgumentException e) {
            return;
        }
        throw new AssertionError("lucky Fisherman have no so much salmon");
    }

    @Test
    public void sellFisDiscount() {
        final int HERRING_COUNT = 20;
        final float DISCOUNT = 0.7f;
        fishParadise.buy(new Box(new Herring(), HERRING_COUNT * 2));

        int price = new Herring().getPrice() * HERRING_COUNT;
        int moneyBeforeDeal = fishParadise.getMoney();

        fishParadise.sell(Herring.class, HERRING_COUNT);
        assertEquals(moneyBeforeDeal + price, fishParadise.getMoney(), "Discount not applied yet");

        fishParadise.getDiscount().put(Herring.class, DISCOUNT);

        price *= DISCOUNT;
        moneyBeforeDeal = fishParadise.getMoney();

        fishParadise.sell(Herring.class, HERRING_COUNT);
        assertEquals(moneyBeforeDeal + price, fishParadise.getMoney(), "Discount should be applied");
    }
}
