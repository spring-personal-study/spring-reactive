package com.company.springreactive.techie.service;

import com.company.springreactive.techie.service.dao.CustomerDao;
import com.company.springreactive.techie.domain.dto.CustomerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerDao dao;

    public List<CustomerDTO> loadAllCustomers() {
        long start = System.currentTimeMillis();
        List<CustomerDTO> customers = dao.getCustomers();
        long end = System.currentTimeMillis();
        System.out.println("Total execution time: " + (end - start));
        return customers;
    }

    public Flux<CustomerDTO> loadAllCustomersStream() {
        long start = System.currentTimeMillis();
        Flux<CustomerDTO> customers = dao.getCustomersStream();
        long end = System.currentTimeMillis();
        System.out.println("Total execution time: " + (end - start));
        return customers;
    }
}
