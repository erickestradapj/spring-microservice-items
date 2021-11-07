package com.dev.spring.app.item.controllers;

import com.dev.spring.app.item.models.Item;
import com.dev.spring.app.item.models.Product;
import com.dev.spring.app.item.services.ItemService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    @CircuitBreaker(name = "items", fallbackMethod = "alternativeMethod")
    @GetMapping("/view2/{id}/amount/{amount}")
    public Item detail2(@PathVariable Long id, @PathVariable Integer amount) {
        return itemService.findById(id, amount);
    }

    @CircuitBreaker(name = "items", fallbackMethod = "alternativeMethod2")
    @TimeLimiter(name = "items")
    @GetMapping("/view3/{id}/amount/{amount}")
    public CompletableFuture<Item> detail3(@PathVariable Long id, @PathVariable Integer amount) {
        return CompletableFuture.supplyAsync(() -> itemService.findById(id, amount));
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

    public CompletableFuture<Item> alternativeMethod2(Long id, Integer amount, Throwable throwable) {
        logger.info(throwable.getMessage());

        Product product = new Product();
        product.setId(id);
        product.setName("Sony camera");
        product.setPrice(500.00);

        Item item = new Item();
        item.setAmount(amount);
        item.setProduct(product);
        return CompletableFuture.supplyAsync(() -> item);
    }
}
