package fr.publicissapient.ked.webfluxtracing.controller;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.IntStream;

@RestController
public class Demo1Controller {

    private final Logger logger;

    Demo1Controller(Logger logger) {

        this.logger = logger;
    }

    @RequestMapping(value = "/test",
    method = RequestMethod.GET)
    Mono<ResponseEntity<Object>> accessTest1(@RequestParam(name = "id") Integer id, ServerWebExchange serverWebExchange) {
        try (MDC.MDCCloseable mdcCloseable = MDC.putCloseable("correlationId", id.toString())) {
            logger.info("Received query for id {}", id);
            var response = performCount(id)
            .map(c -> ResponseEntity.ok().build());

            logger.info("TADAM");
            return response;
        }
    }

    private Mono<Integer> performCount(Integer id) {
        return Mono.just(id)
                .map(i -> {
                    var count = IntStream.range(0, 100000)
                            .boxed()
                            .reduce(0, Integer::sum);
                    logger.info("Got count {} for id: {}", count, i);
                    return count;
                });
    }
}
