package co.com.emorae.api;

import co.com.emorae.model.sequence.Sequence;
import co.com.emorae.usecase.validatedna.ValidateDNAUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class Handler {

    private  final ValidateDNAUseCase useCase;

    public Mono<ServerResponse> listenGETStats(ServerRequest serverRequest) {

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(useCase.getStats());
    }

    public Mono<ServerResponse> listenPOSTMutant(ServerRequest serverRequest) {
        Mono<Sequence> sequenceMono = serverRequest.bodyToMono(Sequence.class);
        return sequenceMono.flatMap(sequence ->{
                    if(Boolean.TRUE.equals(useCase.isMutant(sequence))){
                        return  ServerResponse.ok().build();
                    }
                    return ServerResponse.status(HttpStatus.FORBIDDEN).build();
        }).onErrorResume(error -> ServerResponse.badRequest().build());
    }
}
