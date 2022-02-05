package com.company.springreactive.techie.service.dao;

import com.company.springreactive.techie.domain.dto.CustomerDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CustomerDao {

    private static void sleepExecution(int i) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * not using flux
     */
    public List<CustomerDTO> getCustomers() {
        return IntStream.rangeClosed(1, 10)
                .peek(CustomerDao::sleepExecution)
                .peek(i -> System.out.println("processing count : " + i))
                .mapToObj(i -> new CustomerDTO(i, "customer" + i))
                .collect(Collectors.toList());
    }

    public Flux<CustomerDTO> getCustomersStream() {
        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(i -> System.out.println("processing count in stream flow : " + i))
                .map(i -> new CustomerDTO(i, "customer" + i));
    }

    public Flux<CustomerDTO> getCustomerList() {
        return Flux.range(1, 50)
                .doOnNext(i -> System.out.println("processing count in stream flow : " + i))
                .map(i -> new CustomerDTO(i, "customer" + i));
    }

}
