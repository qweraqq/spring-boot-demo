package com.shen1991.demo.repository;

import org.springframework.data.repository.CrudRepository;
import com.shen1991.demo.entity.Customer;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    List<Customer> findByLastName(String lastName);
}