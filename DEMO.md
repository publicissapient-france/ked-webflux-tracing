# Demo 2

## Logging

Open: `Logging.java`

## Demo1Controller

Open: `Demo1Controller.java`

```java
return Mono.just(id)
  .doOnEach(logOnNext(cId -> logger.info("Received query for id {}", cId)))
  .flatMap(dummyWebClient::performRequest)
  .map(c -> ResponseEntity.ok().build())
  .contextWrite(Context.of("correlationId", id.toString()));
```

## DummyWebClient

Open: `DummyWebClient.java`

```java
.doOnEach(logOnNext(clientResponse -> logger.info("Got Response for id: {}", id)))
```

## SmartWebClient

Open: `SmartWebClient.java`

```java
return Mono.deferContextual(Mono::just)
    .map(contextView -> contextView.<String>get("correlationId"))
                .flatMap(...);

.uri(String.format("/testCorrelation?correlationId=%s", correlationId))
```
