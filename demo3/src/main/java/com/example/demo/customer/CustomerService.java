package com.example.demo.customer;

import com.example.demo.customer.web.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService  implements CommandLineRunner {

    private final CustomerRepository repository;


    public List<Customer> findAll() {
        return repository.findAll();
    }

    public Customer save(Customer customer) {
        return repository.save(customer);
    }

    public Customer findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public Customer update(String id, Customer newCustomer) {
        return repository.findById(id)
                .map(customer -> {
                    customer = new Customer(id, newCustomer.firstName(), newCustomer.lastName());
                    return repository.save(customer);
                })
                .orElseGet(() -> repository.save(newCustomer));
    }

    @Override
    public void run(String... args) throws Exception {
        repository.deleteAll();

        // save a couple of customers
        repository.save(new Customer(null, "Alice", "Smith"));
        repository.save(new Customer(null, "Bob", "Smith"));

        // fetch all customers
        log.info("Customers found with findAll():");
        log.info("-------------------------------");
        for (Customer customer : repository.findAll()) {
            log.info("{}", customer);
        }
        log.info("");

        // fetch an individual customer
        log.info("Customer found with findByFirstName('Alice'):");
        log.info("--------------------------------");
        log.info("{}", repository.findByFirstName("Alice"));

        log.info("Customers found with findByLastName('Smith'):");
        log.info("--------------------------------");
        for (Customer customer : repository.findByLastName("Smith")) {
            log.info("{}", customer);
        }
    }
}
