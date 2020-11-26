package fr.publicissapient.ked.webfluxtracing.controller;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static fr.publicissapient.ked.webfluxtracing.util.Logging.logOnNext;


@Component
public class SmartWebClient {
    private final WebClient webClient;
    private final Logger logger;

    public SmartWebClient(Logger logger) {
        this.logger = logger;
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    public Mono<String> performRequest(Integer id) {
        return webClient.get().uri("/")
                .retrieve()
                .toBodilessEntity()
                .doOnEach(logOnNext(clientResponse -> logger.info("Got Response for id: {}", id)))
                .map(resp -> "");
    }
}
