package com.dev.spring.app.item.services;

import com.dev.spring.app.item.clients.ProductClientRest;
import com.dev.spring.app.item.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("serviceFeign")
public class ItemServiceFeignImpl implements ItemService {

    @Autowired
    private ProductClientRest productClientRest;

    @Override
    public List<Item> findAll() {
        return productClientRest.list().stream().map(product -> new Item(product, 1)).collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer amount) {
        return new Item(productClientRest.detail(id), amount);
    }
}
