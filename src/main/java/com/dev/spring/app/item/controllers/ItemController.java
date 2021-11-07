package com.dev.spring.app.item.controllers;

import com.dev.spring.app.item.models.Item;
import com.dev.spring.app.item.models.Product;
import com.dev.spring.app.item.services.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemController {

    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    @Qualifier("serviceFeign")
    private ItemService itemService;

    @GetMapping("/list")
    public List<Item> list(
            @RequestParam(name = "name", required = false) String name,
            @RequestHeader(name = "token-request", required = false) String token) {
        System.out.println("name = " + name);
        System.out.println("token = " + token);
        return itemService.findAll();
    }

    @GetMapping("/view/{id}/amount/{amount}")
    public Item detail(@PathVariable Long id, @PathVariable Integer amount) {
        return circuitBreakerFactory.create("items")
                .run(() -> itemService.findById(id, amount), throwable -> alternativeMethod(id, amount, throwable));
    }

    public Item alternativeMethod(Long id, Integer amount, Throwable throwable) {
        logger.info(throwable.getMessage());

        Product product = new Product();
        product.setId(id);
        product.setName("Sony camera");
        product.setPrice(500.00);

        Item item = new Item();
        item.setAmount(amount);
        item.setProduct(product);
        return item;
    }
}
