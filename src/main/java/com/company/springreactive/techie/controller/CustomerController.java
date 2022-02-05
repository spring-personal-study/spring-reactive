package com.company.springreactive.techie.controller;

import com.company.springreactive.techie.domain.dto.CustomerDTO;
import com.company.springreactive.techie.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    /**
     * not using webFlux
     */
    @GetMapping(value = "/not-stream")
    public List<CustomerDTO> getAllCustomer() {
        return service.loadAllCustomers();
    }

    /**
     * using webFlux
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CustomerDTO> getAllCustomerStream() {
        return service.loadAllCustomersStream();
    }
}
