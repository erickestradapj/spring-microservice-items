package com.dev.spring.app.item.services;

import com.dev.spring.app.item.models.Item;

import java.util.List;

public interface ItemService {

    List<Item> findAll();

    Item findById(Long id, Integer amount);
}
