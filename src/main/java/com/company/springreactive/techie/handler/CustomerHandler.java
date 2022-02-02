package com.company.springreactive.techie.handler;

import com.company.springreactive.techie.dao.CustomerDao;
import com.company.springreactive.techie.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerHandler {

    @Autowired
    private CustomerDao dao;

    public Mono<ServerResponse> loadCustomers(ServerRequest request) {
        Flux<Customer> customerList = dao.getCustomerList();
        return ServerResponse.ok().body(customerList, Customer.class);
    }

    public Mono<ServerResponse> findCustomer(ServerRequest request) {
        String input = request.pathVariable("input");
        int customerId = Integer.parseInt(input);
        //Mono<Customer> single = dao.getCustomerList().filter(c -> c.getId() == customerId).take(1).single();
        Mono<Customer> result = dao.getCustomerList().filter(c -> c.getId() == customerId).next(); //same as beyond
        return ServerResponse.ok().body(result, Customer.class);
    }

    /**
     * @param request .body {
     *                        "id":6,
     *                        "name":"customerId6"
     *                      }
     */
    public Mono<ServerResponse> saveCustomer(ServerRequest request) {
        Mono<Customer> customerMono = request.bodyToMono(Customer.class);
        Mono<String> map = customerMono.map(dto -> dto.getId() + ":" + dto.getName());
        return ServerResponse.ok().body(map, Customer.class);
    }
}
