package com.example.demo.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    List<Customer> findAll() {
        return customerService.findAll();
    }

    @PostMapping
    Customer save(@RequestBody Customer newCustomer) {
        return customerService.save(newCustomer);
    }

    @GetMapping("/{id}")
    Customer findById(@PathVariable String id) {
        return customerService.findById(id);
    }

    @PutMapping("/{id}")
    Customer update(@PathVariable String id, @RequestBody Customer customer) {
        return customerService.update(id, customer);
    }

    @DeleteMapping("/{id}")
    void deleteById(@PathVariable String id) {
        customerService.deleteById(id);
    }

}
