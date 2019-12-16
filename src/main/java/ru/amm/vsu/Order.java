package ru.amm.vsu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    static final int DELIVERY_DAYS = 1;

    private List<Box> boxes = new ArrayList<>();
    private int price;
    private Date deliveryDate;

    public List<Box> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<Box> boxes) {
        this.boxes = boxes;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
