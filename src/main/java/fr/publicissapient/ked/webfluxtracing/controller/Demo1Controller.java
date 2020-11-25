package fr.publicissapient.ked.webfluxtracing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class Demo1Controller {


    @RequestMapping(value = "/test")
    Mono<ResponseEntity<Object>> accessTest1(ServerWebExchange serverWebExchange) {
        return Mono.just(ResponseEntity.ok().build());
    }
}
