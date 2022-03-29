package co.com.emorae.api;

import co.com.emorae.model.sequence.Sequence;
import co.com.emorae.usecase.validatedna.ValidateDNAUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.management.RuntimeErrorException;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RouterRest.class, Handler.class, ExceptionHandler.class})
class RouterRestTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private ValidateDNAUseCase useCase;

    @Value("${server.port}")
    private String port;

    @Test
    public void test_is_mutant_sequence(){
        when(useCase.isMutant(any(Sequence.class))).thenReturn(true);
        testClient.post().uri("http://localhost:8080/mutant").contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\n" +
                        "    \"dna\": [\n" +
                        "        \"ATGCGA\",\n" +
                        "        \"CAGTGC\",\n" +
                        "        \"TTATGT\",\n" +
                        "        \"AGAAGG\",\n" +
                        "        \"CCCCTA\",\n" +
                        "        \"TCACTG\"\n" +
                        "    ]\n" +
                        "}").exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test_is_Human_sequence(){
        when(useCase.isMutant(any(Sequence.class))).thenReturn(false);
        testClient.post().uri("http://localhost:8080/mutant").contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\n" +
                        "    \"dna\": [\n" +
                        "        \"ATGCGA\",\n" +
                        "        \"CAGTGC\",\n" +
                        "        \"TTATGT\",\n" +
                        "        \"AGAAGG\",\n" +
                        "        \"CCCCTA\",\n" +
                        "        \"TCACTG\"\n" +
                        "    ]\n" +
                        "}").exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void test_is_invalid_sequence(){
        when(useCase.isMutant(any(Sequence.class))).thenThrow(new RuntimeErrorException(new Error("bad request")));
        testClient.post().uri("http://localhost:8080/mutant").contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\n" +
                        "    \"dna\": [\n" +
                        "        \"ATGCGA\",\n" +
                        "        \"CAGTGC\",\n" +
                        "        \"TTATGT\",\n" +
                        "        \"AGAAGG\",\n" +
                        "        \"CCCCTA\",\n" +
                        "        \"TCACTG\"\n" +
                        "    ]\n" +
                        "}").exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void test_get_stats(){
        when(useCase.getStats()).thenReturn(new HashMap<>());
        testClient.get().uri("http://localhost:8080/stats")
                .exchange()
                .expectStatus().isOk();
    }

}