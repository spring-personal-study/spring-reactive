package com.company.springreactive.techie.router;

import com.company.springreactive.techie.router.handler.CustomerHandler;
import com.company.springreactive.techie.router.handler.CustomerStreamHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class RouterConfig {

    private final CustomerHandler handler;
    private final CustomerStreamHandler streamHandler;

    /**
     * Functional EndPoint
     */
    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .GET("/router/customers", e -> handler.loadCustomers(e))
                .GET("/router/customers/stream", streamHandler::getCustomers)
                .GET("/router/customer/{input}", handler::findCustomer)
                .POST("/router/customer/save", handler::saveCustomer)
                .build();

    }
}
