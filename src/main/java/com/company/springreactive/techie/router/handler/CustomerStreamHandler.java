package com.company.springreactive.techie.router.handler;

import com.company.springreactive.techie.service.dao.CustomerDao;
import com.company.springreactive.techie.domain.dto.CustomerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerStreamHandler {

    private final CustomerDao dao;

    public Mono<ServerResponse> getCustomers(ServerRequest request) {
        Flux<CustomerDTO> customersStream = dao.getCustomersStream();
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(customersStream, CustomerDTO.class);
    }
}
