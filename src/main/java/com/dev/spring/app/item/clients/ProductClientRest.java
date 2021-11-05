package com.dev.spring.app.item.clients;

import com.dev.spring.app.item.models.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// Connect -> product-service
@FeignClient(name = "product-service/api")
public interface ProductClientRest {

    @GetMapping("/list")
    List<Product> list();

    @GetMapping("/view/{id}")
    Product detail(@PathVariable Long id);
}
