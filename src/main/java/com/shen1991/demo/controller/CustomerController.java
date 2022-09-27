package com.shen1991.demo.controller;
import com.shen1991.demo.entity.Customer;

import com.shen1991.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CustomerController {
    private final CustomerRepository customerDao;

    @Autowired
    public CustomerController(CustomerRepository customerDao){
        this.customerDao = customerDao;
    }

    @PostMapping("/customers")
    ResponseEntity<String> addCustomerDao(@Valid @RequestBody Customer customer) { // the most relevant part is the use of the @Valid annotation.
        // persisting the user
        Customer savedCustomer =customerDao.save(customer);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCustomer.getId()).toUri();
        return ResponseEntity.created(location).build();
    }


    @GetMapping("/customers")
    public List<Customer> getCustomer() {
        return (List<Customer>) customerDao.findAll();
    }

    @GetMapping("/customers/{id}")
    public Customer getCustomerById(@PathVariable long id) {
        Optional<Customer> customer = customerDao.findById(id);
        if (!customer.isPresent())
            return null;
        return customer.get();
    }

    // standard constructors / other methods
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
