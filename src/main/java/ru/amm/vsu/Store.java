package ru.amm.vsu;

import ru.amm.vsu.fishes.Fish;

import java.util.*;

public class Store {
    private final float DEFAULT_DISCOUNT = 1.0f;

    private String name;
    private int money;

    private Map<Class<? extends Fish>, List<Box>> shelf = new HashMap<>();
    private Map<Class<? extends Fish>, Float> discount = new HashMap<>();

    public Store(String name, int money) {
        this.name = name;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public Map<Class<? extends Fish>, List<Box>> getShelf() {
        return shelf;
    }

    public Map<Class<? extends Fish>, Float> getDiscount() {
        return discount;
    }

    public Box sell(Class<? extends Fish> fish, int count) {
        throwAwayRottenFish(fish);
        if (count <= 0) {
            throw new IllegalArgumentException("You try to by nothing");
        }

        if (!shelf.containsKey(fish)) {
            throw new IllegalArgumentException("There is no" + fish + "in the store");
        }

        Box purchase = new Box();
        purchase.setCount(count);

        int emptyBoxesCount = 0;
        int needToSaleCount = count;

        for (Box b : shelf.get(fish)) {
            // If you put old fish in a box with fresh fish,
            // they will go rot at the same time
            if (purchase.getFish() == null || purchase.getFish().getShelfLife().before(b.getFish().getShelfLife())) {
                purchase.setFish(b.getFish());
            }

            if (needToSaleCount >= b.getCount()) {
                needToSaleCount -= b.getCount();
                emptyBoxesCount++;
            } else {
                b.setCount(b.getCount() - needToSaleCount);
                needToSaleCount = 0;
                break;
            }
        }

        if (needToSaleCount > 0) {
            throw new IllegalArgumentException(name + " cannot sell " + count + " " + fish.getSimpleName());
        }

        if (emptyBoxesCount > 0) {
            shelf.put(fish, shelf.get(fish).subList(emptyBoxesCount, shelf.get(fish).size()));
        }

        money += purchase.getFish().getPrice() * purchase.getCount() * discount.getOrDefault(purchase.getFish().getClass(), DEFAULT_DISCOUNT);
        return purchase;
    }

    public int buy(Box... boxes) {
        int costs = Arrays.stream(boxes).mapToInt(b -> b.getFish().getPrice() * b.getCount()).sum();
        return buy(Arrays.asList(boxes), costs);
    }

    public int buy(Order order) {
        return buy(order.getBoxes(), order.getPrice());
    }

    private int buy(List<Box> boxes, int price) {
        if (money < price) {
            throw new IllegalArgumentException(name + " is missing " + (price - money) + " money for the deal");
        }

        for (Box b : boxes) {
            if (shelf.containsKey(b.getFish().getClass())) {
                shelf.get(b.getFish().getClass()).add(b);
            } else {
                List<Box> newSubShelf = new ArrayList<>();
                shelf.put(b.getFish().getClass(), newSubShelf);
                newSubShelf.add(b);
            }
        }

        money -= price;
        return price;
    }

    private void throwAwayRottenFish(Class<? extends Fish> fish) {
        if (shelf.containsKey(fish)) {
            shelf.get(fish).removeIf(b -> !b.getFish().isFresh());
        }
    }
}
