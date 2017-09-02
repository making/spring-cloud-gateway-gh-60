package com.example.demogw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.factory.WebFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.tuple.Tuple;
import org.springframework.web.server.WebFilter;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class DemoGwApplication {

    @Bean
    WebFilterFactory foo() {
        return new WebFilterFactory() {
            @Override
            public String name() {
                return "MyRemoveResponseHeader";
            }

            @Override
            public List<String> argNames() {
                return Collections.singletonList(NAME_KEY);
            }

            @Override
            public WebFilter apply(Tuple args) {
                final String header = args.getString(NAME_KEY);
                return (exchange, chain) -> chain.filter(exchange)
                        .doFinally(v -> exchange.getResponse().getHeaders().remove(header));
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoGwApplication.class, args);
    }
}
