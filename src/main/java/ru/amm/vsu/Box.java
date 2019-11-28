package ru.amm.vsu;

import ru.amm.vsu.fishes.Fish;

public class Box {
    private Fish fish;
    private int count;

    public Box() {
    }

    public Box(Fish fish, int count) {
        this.fish = fish;
        this.count = count;
    }

    public Fish getFish() {
        return fish;
    }

    public void setFish(Fish fish) {
        this.fish = fish;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
