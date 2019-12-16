package ru.amm.vsu;

import ru.amm.vsu.fishes.Fish;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class OrderBuilder {
    private Order order = new Order();

    public OrderBuilder add(Box box) {
        order.getBoxes().add(box);
        order.setPrice(order.getPrice() + box.getFish().getPrice() * box.getCount());
        return this;
    }

    public OrderBuilder add(Fish fish, int count) {
        order.getBoxes().add(new Box(fish, count));
        order.setPrice(order.getPrice() + fish.getPrice() * count);
        return this;
    }

    public Order build() {
        order.setDeliveryDate(Date.from(LocalDateTime.now().plusDays(Order.DELIVERY_DAYS)
                .atZone(ZoneId.systemDefault())
                .toInstant()));
        return order;
    }
}
