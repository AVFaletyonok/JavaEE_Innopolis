package org.example.functional.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.example.kafka.KafkaProducer;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaHandler {

    private final KafkaProducer kafkaProducer;

    public Mono<ServerResponse> sendMessageToKafka(ServerRequest serverRequest) {

        var count = Integer.valueOf(serverRequest
                .queryParam("count")
                .orElse("10"));

        return kafkaProducer.sendMessage(count)
                .then()
                .flatMap(e -> ServerResponse.ok()
                        .body(Mono.just("Отправлено сообщение"), String.class));
    }

//    public Mono<ServerResponse> hello2(ServerRequest serverRequest) {
//
//        // до 8 параллельных операций одновременно
//        // например сходить в 8 разных систем и затем консолидировать
//        return Mono.zip(Mono.just("hello!"), Mono.just("World!"))
//                .map(tuple -> {
//                    var s1 = tuple.getT1();
//                    var s2 = tuple.getT2();
//
//                    return s1 + " " + s2;
//                })
//                .flatMap(e -> ServerResponse.ok()
//                        .body(Mono.just(e), String.class));
//    }

}
