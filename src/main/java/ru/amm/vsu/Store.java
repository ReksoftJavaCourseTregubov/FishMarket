package ru.amm.vsu;

import java.util.*;

public class Store {
    private final float DEFAULT_DISCOUNT = 1.0f;

    private String name;
    private int money;

    private List<Box> shelf = new ArrayList<>();
    private Map<Class, Float> discount = new HashMap<>();

    public Store(String name, int money) {
        this.name = name;
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public List<Box> getShelf() {
        return shelf;
    }

    public Map<Class, Float> getDiscount() {
        return discount;
    }

    public Box sell(Class fish, int count) {
        shelf.removeIf(b -> !b.getFish().isFresh());
        if (count <= 0 || shelf.stream().mapToInt(b -> (b.getFish().getClass() == fish) ? b.getCount() : 0).sum() < count) {
            throw new IllegalArgumentException(name + " cannot sell " + count + " " + fish.getSimpleName());
        }

        Box purchase = new Box();
        purchase.setCount(count);

        List<Box> boxesForSale = new ArrayList<>();

        for (Box b : shelf) {
            if (count > 0) {
                if (b.getFish().getClass() == fish) {
                    // If you put old fish in a box with fresh fish,
                    // they will go rot at the same time
                    if (purchase.getFish() == null || purchase.getFish().getShelfLife().before(b.getFish().getShelfLife())) {
                        purchase.setFish(b.getFish());
                    }
                    if (count >= b.getCount()) {
                        count -= b.getCount();
                        boxesForSale.add(b);
                    } else {
                        b.setCount(b.getCount() - count);
                        break;
                    }
                }
            } else {
                break;
            }
        }

        shelf.removeAll(boxesForSale);

        money += purchase.getFish().getPrice() * purchase.getCount() * discount.getOrDefault(purchase.getFish().getClass(), DEFAULT_DISCOUNT);
        return purchase;
    }

    public int buy(Box... boxes) {
        int costs = Arrays.stream(boxes).mapToInt(b -> b.getFish().getPrice() * b.getCount()).sum();
        if (money < costs) {
            throw new IllegalArgumentException(name + " is missing " + (costs - money) + " money for the deal");
        }

        shelf.addAll(Arrays.asList(boxes));
        money -= costs;
        return costs;
    }
}
