package com.example.demogw;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoGwApplicationTests {
    @LocalServerPort
    int port = 0;

    @Test
    public void original() {
        WebClient webClient = WebClient.create();
        Mono<ResponseEntity<String>> res = webClient.get()
                .uri("http://localhost:{port}/get", port)
                .header("Host", "original.example.com")
                .exchange().flatMap(x -> x.toEntity(String.class));

        StepVerifier.create(res)
                .consumeNextWith(
                        response -> {
                            HttpHeaders headers = response.getHeaders();
                            // Fail!!!
                            assertThat(headers).doesNotContainKey("X-Powered-By");
                            assertThat(headers).doesNotContainKey("Via");
                            assertThat(headers).doesNotContainKey("Server");
                        })
                .expectComplete()
                .verify();
    }

    @Test
    public void fixed() {
        WebClient webClient = WebClient.create();
        Mono<ResponseEntity<String>> res = webClient.get()
                .uri("http://localhost:{port}/get", port)
                .header("Host", "fixed.example.com")
                .exchange().flatMap(x -> x.toEntity(String.class));

        StepVerifier.create(res)
                .consumeNextWith(
                        response -> {
                            HttpHeaders headers = response.getHeaders();
                            assertThat(headers).doesNotContainKey("X-Powered-By");
                            assertThat(headers).doesNotContainKey("Via");
                            assertThat(headers).doesNotContainKey("Server");
                        })
                .expectComplete()
                .verify();
    }
}
