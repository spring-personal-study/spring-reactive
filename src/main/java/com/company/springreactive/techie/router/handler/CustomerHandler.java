package com.company.springreactive.techie.router.handler;

import com.company.springreactive.techie.service.dao.CustomerDao;
import com.company.springreactive.techie.domain.dto.CustomerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerHandler {

    private final CustomerDao dao;

    public Mono<ServerResponse> loadCustomers(ServerRequest request) {
        Flux<CustomerDTO> customerList = dao.getCustomerList();
        return ServerResponse.ok().body(customerList, CustomerDTO.class);
    }

    public Mono<ServerResponse> findCustomer(ServerRequest request) {
        String input = request.pathVariable("input");
        int customerId = Integer.parseInt(input);
        //Mono<Customer> single = dao.getCustomerList().filter(c -> c.getId() == customerId).take(1).single();
        Mono<CustomerDTO> result = dao.getCustomerList().filter(c -> c.getId() == customerId).next(); //same as beyond
        return ServerResponse.ok().body(result, CustomerDTO.class);
    }

    /**
     * @param request .body {
     *                        "id":6,
     *                        "name":"customerId6"
     *                      }
     */
    public Mono<ServerResponse> saveCustomer(ServerRequest request) {
        Mono<CustomerDTO> customerMono = request.bodyToMono(CustomerDTO.class);
        Mono<String> map = customerMono.map(dto -> dto.getId() + ":" + dto.getName());
        return ServerResponse.ok().body(map, CustomerDTO.class);
    }
}
