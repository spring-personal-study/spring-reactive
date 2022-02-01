package com.company.springreactive.techie.controller;

import com.company.springreactive.techie.dto.Customer;
import com.company.springreactive.techie.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    /**
     * not using webFlux
     */
    @GetMapping(value = "/not-stream")
    public List<Customer> getAllCustomer() {
        return service.loadAllCustomers();
    }

    /**
     * using webFlux
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Customer> getAllCustomerStream() {
        return service.loadAllCustomersStream();
    }
}
