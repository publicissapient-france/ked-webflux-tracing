package fr.publicissapient.ked.webfluxtracing.controller;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
public class DummyWebClient {
    private final WebClient webClient;
    private final Logger logger;

    public DummyWebClient(Logger logger) {
        this.logger = logger;
        this.webClient = WebClient.builder()
                .baseUrl("https://www.google.com")
                .build();
    }

    public Mono<String> performRequest(Integer id) {
        return webClient.get().uri("/")
                .retrieve()
                .bodyToMono(ClientResponse.class)
                .map(clientResponse -> {
                            logger.info("Got Response for id: {}", id);
                            return clientResponse;
                        }
                )
                .map(resp -> "");
    }
}
