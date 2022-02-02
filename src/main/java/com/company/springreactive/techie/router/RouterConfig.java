package com.company.springreactive.techie.router;

import com.company.springreactive.techie.handler.CustomerHandler;
import com.company.springreactive.techie.handler.CustomerStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    @Autowired
    private CustomerHandler handler;

    @Autowired
    private CustomerStreamHandler streamHandler;

    /**
     * Functional EndPoint
     */
    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .GET("/router/customers", e -> handler.loadCustomers(e))
                .GET("/router/customers/stream", streamHandler::getCustomers)
                .build();

    }
}
