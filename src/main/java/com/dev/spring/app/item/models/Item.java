package com.dev.spring.app.item.models;

import lombok.Data;

@Data
public class Item {
    private Product product;
    private Integer amount;

    public Item() {
    }

    public Item(Product product, Integer amount) {
        this.product = product;
        this.amount = amount;
    }

    public Double total() {
        return product.getPrice() * amount.doubleValue();
    }
}
