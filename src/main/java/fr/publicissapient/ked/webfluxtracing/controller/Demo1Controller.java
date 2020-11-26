package fr.publicissapient.ked.webfluxtracing.controller;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.stream.IntStream;

@RestController
public class Demo1Controller {

    private final Logger logger;
    private final DummyWebClient dummyWebClient;

    Demo1Controller(Logger logger, DummyWebClient dummyWebClient) {
        this.logger = logger;
        this.dummyWebClient = dummyWebClient;
    }

    Mono<ResponseEntity<Object>> accessTest1(@RequestParam(name = "id") Integer id) {
        try (MDC.MDCCloseable mdcCloseable = MDC.putCloseable("correlationId", id.toString())) {
            logger.info("Received query for id {}", id);
            return performCount(id)
                    .map(c -> ResponseEntity.ok().build());
        }
    }

    private Mono<Integer> performCount(Integer id) {
        return Mono.just(id)
                .map(i -> {
                    var count = IntStream.range(0, 10000)
                            .boxed()
                            .reduce(0, Integer::sum);
                    logger.info("Got count {} for id: {}", count, i);
                    return count;
                });
    }

    @RequestMapping(value = "/test",
    method = RequestMethod.GET)
    Mono<ResponseEntity<Object>> accessTest2(@RequestParam(name = "id") Integer id) {
        MDC.put("correlationId", id.toString());

        logger.info("Received query for id {}", id);
        return performQuery(id)
                .map(c -> ResponseEntity.ok().build())
                .doAfterTerminate(MDC::clear);
    }

    private Mono<String> performQuery(Integer id) {
        return dummyWebClient.performRequest(id);
    }

}
